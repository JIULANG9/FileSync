package com.wordsfairy.filesync.websocket

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/8/15 22:46
 */


import com.wordsfairy.filesync.data.entity.SocketResultDto
import com.wordsfairy.filesync.data.entity.SocketType
import com.wordsfairy.filesync.utils.log.LogX
import com.wordsfairy.filesync.websocket.interceptor.LoggingInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry

import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit


class FilesyncClient(
    val viewModelScope: CoroutineScope
) {
    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
        private const val RECONNECT_INTERVAL = 5 * 1000L // 重新连接间隔时间（秒）
        private const val HEARTBEAT_INTERVAL = 60 * 1000L // 心跳检测间隔时间（秒）
        private const val HEARTBEAT_MESSAGE = "hi"
        private const val reconnectInterval: Long = 2
    }

    private var socket: WebSocket? = null

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(
            LoggingInterceptor.Builder()
                .isDebug(true)
                .setRequestTag("请求")// Request
                .setResponseTag("响应")// Response
                .build()
        )
        .retryOnConnectionFailure(true)
        .build()



     fun connect() =
        createSocketFlow()
            .onEach {
               LogX.i("WebSocket", "收到消息 $it")
            }.retry(reconnectInterval)



    private fun createSocketFlow(): Flow<String> = callbackFlow {

        val request = Request.Builder()
            .url("ws://192.168.0.102:9999")
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                trySend(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                LogX.i("WebSocket", "onClosed $reason")
                channel.close()
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                LogX.e("WebSocket", "onFailure $response",t)
                // 连接断开后，定时重新连接
                Thread.sleep(RECONNECT_INTERVAL)
                channel.close()
                connect()
            }
        }

        socket = client.newWebSocket(request, listener)
        launchHeartbeat()
        awaitClose { socket?.cancel() }
    }.flowOn(Dispatchers.IO)


    private fun launchHeartbeat() {
        viewModelScope.launch {
            while (isActive) {
                val dto = SocketResultDto.create(
                    SocketType.HEARTBEAT,
                    HEARTBEAT_MESSAGE
                )
                send(dto)
                delay(HEARTBEAT_INTERVAL)
            }
        }
    }

    fun send(message: String) {
        socket?.send(message)
    }

    fun close() {
        socket?.close(NORMAL_CLOSURE_STATUS, null)
    }
}
