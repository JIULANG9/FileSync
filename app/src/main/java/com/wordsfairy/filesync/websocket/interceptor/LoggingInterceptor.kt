package com.wordsfairy.filesync.websocket.interceptor

import android.util.Log
import okhttp3.*

import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @author JIULANG
 */
class LoggingInterceptor(private val builder: Builder) : Interceptor {
    private val isDebug = builder.isDebug

    override fun intercept(chain: Interceptor.Chain): Response {
        // 请求
        var request = chain.request()
        // 增加头部信息
        if (builder.getHeaders().size > 0) {
            val newBuilder = request.newBuilder()
            // 遍历并添加
            val headers = builder.getHeaders()
            val names = headers.names()
            names.forEach { key ->
                val s = headers[key]
                if (!s.isNullOrEmpty()) {
                    newBuilder.addHeader(key, s)
                }
            }
            request = newBuilder.build()
        }
        if (!isDebug) {
            return chain.proceed(request)
        }

        // 请求体
        val requestBody = request.body

        var rContentType: MediaType? = null
        if (requestBody != null) {
            rContentType = requestBody.contentType()
        }

        var rSubtype: String? = null
        if (rContentType != null) {
            rSubtype = rContentType.subtype
            // form-data
            // x-www-form-urlencoded
        }

        if (rSubtype != null && (rSubtype.contains("json")
                    || rSubtype.contains("xml")
                    || rSubtype.contains("plain")
                    || rSubtype.contains("html")
                    || rSubtype.contains("form"))
        ) {
            printJsonRequest(builder, request)
        } else {
            printFileRequest(builder, request)
        }

        // 记录开始时间
        val st = System.nanoTime()
        // 响应
        val response = chain.proceed(request)

        val chainMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - st)
        val code = response.code
        val responseBody = response.body ?: return response

        val contentType = responseBody.contentType()

        var subtype: String? = null

        if (contentType != null) {
            subtype = contentType.subtype
        }

        if (subtype != null && (subtype.contains("json")
                    || subtype.contains("xml")
                    || subtype.contains("plain")
                    || subtype.contains("html"))
        ) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)// Buffer the entire body.
            val buffer = source.buffer

            val bodyString = buffer.clone().readUtf8()// responseBody.string();
            printJsonResponse(builder, chainMs, code, bodyString, request.url)
            // body = ResponseBody.create(contentType, bodyString);
        } else {
            printFileResponse(builder, chainMs, code, request.url)
            return response
        }
        return response
    }

    class Builder {
        companion object {
            private var TAG = "LoggingE"
        }

        var isDebug = false
        private var requestTag: String? = null
        private var responseTag: String? = null
        private var builder: Headers.Builder = Headers.Builder()

        fun getHeaders(): Headers = builder.build()

        fun getTag(isRequest: Boolean): String {
            return if (isRequest) {
                requestTag ?: TAG
            } else {
                responseTag ?: TAG
            }
        }

        /**
         * @param name  Filed
         * @param value Value
         * @return Builder
         * Add a field with the specified value
         */
        fun addHeader(name: String, value: String): Builder = apply {
            builder[name] = value
        }

        /**
         * Set request and response each log tag
         *
         * @param tag general log tag
         * @return Builder
         */
        fun tag(tag: String): Builder = apply {
            TAG = tag
        }

        /**
         * Set request log tag
         *
         * @param tag request log tag
         * @return Builder
         */
        fun setRequestTag(tag: String): Builder = apply {
            requestTag = tag
        }

        /**
         * Set response log tag
         *
         * @param tag response log tag
         * @return Builder
         */
        fun setResponseTag(tag: String): Builder = apply {
            responseTag = tag
        }

        /**
         * @param isDebug set can sending log output
         * @return Builder
         */
        fun isDebug(isDebug: Boolean): Builder = apply {
            this.isDebug = isDebug
        }

        fun build(): LoggingInterceptor = LoggingInterceptor(this)
    }

    // ---------------------------------------------------------------------------------------------
    companion object {
        private const val JSON_INDENT = 3
        private val LINE_SEPARATOR = System.getProperty("line.separator")
        private val OMITTED_REQUEST = listOf(LINE_SEPARATOR, "Omitted request body")
        private val OMITTED_RESPONSE = listOf(LINE_SEPARATOR, "Omitted response body")
        private val DOUBLE_SEPARATOR = "${LINE_SEPARATOR}+${LINE_SEPARATOR}"
        private const val MAX_LONG_SIZE = 180

        private fun printJsonRequest(builder: Builder, request: Request) {
            var body = bodyToString(request)


            if (request.body is FormBody) {
                // FormBody(表单类)
                try {
                    body = URLDecoder.decode(body, Charset.forName("UTF-8").name())
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                // MultipartBody(多部分类)
            }

            val requestBody = "${LINE_SEPARATOR}Body:$LINE_SEPARATOR$body"
            val tag = builder.getTag(true)
            showLog(
                tag,
                "╔══════ 请求 ══════════════════════════════════════════════════════════════════════════════"
            )
//            logLines(tag, getRequest(request))
            logLines(tag, getHeaderLines(request))
            logLines(tag, requestBody.split(LINE_SEPARATOR!!))
            showLog(
                tag,
                "╚═════════════════════════════════════════════════════════════════════════════════════════"
            )
        }

        private fun printFileRequest(builder: Builder, request: Request) {
            val tag = builder.getTag(true)


            showLog(
                tag,
                "╔══════ 请求 ══════════════════════════════════════════════════════════════════════════════"
            )
//            logLines(tag, getRequest(request))
            logLines(tag, getHeaderLines(request))
            logLines(tag, OMITTED_REQUEST)
            showLog(
                tag,
                "╚═════════════════════════════════════════════════════════════════════════════════════════"
            )
        }

        private fun printJsonResponse(
            builder: Builder,
            chainMs: Long,
            code: Int,
            bodyString: String,
            url: HttpUrl
        ) {
            val responseBody = "${LINE_SEPARATOR}Body:$LINE_SEPARATOR${getJsonString(bodyString)}"
            val tag = builder.getTag(false)
            showLog(
                tag,
                "╔══════ 响应 ══════════════════════════════════════════════════════════════════════════════"
            )
            logLines(tag, getResponse(chainMs, code, url))
            logLines(tag, responseBody.split(LINE_SEPARATOR!!))
            showLog(tag, "║ ")
            showLog(tag, "║ ${bodyString.replace(LINE_SEPARATOR.toRegex(), "")}")
            showLog(
                tag,
                "╚═════════════════════════════════════════════════════════════════════════════════════════"
            )
        }

        private fun printFileResponse(
            builder: Builder,
            chainMs: Long,
            code: Int,
            url: HttpUrl
        ) {
            val tag = builder.getTag(false)
            showLog(
                tag,
                "╔══════ 响应 ══════════════════════════════════════════════════════════════════════════════"
            )
            logLines(tag, getResponse(chainMs, code, url))
            logLines(tag, OMITTED_RESPONSE)
            showLog(
                tag,
                "╚═════════════════════════════════════════════════════════════════════════════════════════"
            )
        }

        private fun getRequest(request: Request): List<String> {
            val header = request.headers.toString()
            val loggableHeader = true
            val message =
                "${request.url}\u3000${request.method}${DOUBLE_SEPARATOR}" + when {
                    header.isEmpty() -> ""
                    loggableHeader -> "Headers${LINE_SEPARATOR}${
                        dotHeaders(header)
                    }"
                    else -> ""
                }
            return message.split(LINE_SEPARATOR!!)
        }

        private fun getHeaderLines(request: Request): List<String> {
            // 获取请求头
            val headers = request.headers
            val headersMaxLength = headers.names().maxByOrNull { it.length }?.length ?: 0
            val headersLines: MutableList<String> = mutableListOf()
            headersLines.add("${request.url}\u3000${request.method}${LINE_SEPARATOR}")
            headersLines.add("")
            for (i in 0 until headers.size) {
                val name = headers.name(i)
                if (!"Content-Type".equals(name, true) && !"Content-Length".equals(name, true)) {
                    headersLines.add("-${name.fixLength(headersMaxLength)}\t: \t${headers.value(i)}")
                }
            }
            return headersLines
        }
        private fun getResponse(tookMs: Long, code: Int, url: HttpUrl): List<String> {
            val message = "${url}\u3000${code}\u3000${tookMs}ms"
            return message.split(LINE_SEPARATOR!!)
        }

        private fun dotHeaders(header: String): String {
            val headers = header.split(LINE_SEPARATOR!!)
            val builder = StringBuilder()
            headers.forEach { item ->
                arrayOf(builder.append("-").append(item).append("\n"))
            }
            return builder.toString()
        }

        private fun logLines(tag: String, lines: List<String>) {
            lines.forEach { line ->
                val lineLength = line.length
                for (i in 0..lineLength / MAX_LONG_SIZE) {
                    val start = i * MAX_LONG_SIZE
                    var end = (i + 1) * MAX_LONG_SIZE
                    end = if (end > line.length) line.length else end
                    showLog(tag, "║ " + line.substring(start, end))
                }
            }
        }

        private fun bodyToString(request: Request): String {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                val body = copy.body
                if (body == null) {
                    ""
                } else {
                    body.writeTo(buffer)
                    getJsonString(buffer.readUtf8())
                }
            } catch (e: IOException) {
                "{\"err\": \" ${e.message} \"}"
            }
        }

        private fun getJsonString(msg: String): String {
            return try {
                when {
                    msg.startsWith("{") -> {
                        val jsonObject = JSONObject(msg)
                        jsonObject.toString(JSON_INDENT)
                    }
                    msg.startsWith("[") -> {
                        val jsonArray = JSONArray(msg)
                        jsonArray.toString(JSON_INDENT)
                    }
                    else -> {
                        msg
                    }
                }
            } catch (e: JSONException) {
                msg
            }
        }

        private fun showLog(tab: String, msg: String) {
            val random = ((Math.random() * 9 + 1) * 10000).toInt()
            Log.i("${random}\u3000$tab", msg)
        }


        private fun String.fixLength(maxLength: Int): String {
            return if (this.length >= maxLength) {
                this
            } else {
                val sb = StringBuilder(this)
                for (i in 0 until maxLength - this.length) {
                    sb.append(" ")
                }
                sb.toString()
            }
        }

    }
}