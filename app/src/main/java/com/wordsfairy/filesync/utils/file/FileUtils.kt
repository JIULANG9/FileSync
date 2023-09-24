package com.wordsfairy.filesync.utils.file

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import com.wordsfairy.filesync.base.BaseApplication
import com.wordsfairy.filesync.data.AppSystemSetManage
import java.io.BufferedInputStream
import java.io.BufferedOutputStream

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt
import kotlin.random.Random

import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

/**
 * uri转File
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
fun uriToFile(uri: Uri): File? {
    val context = BaseApplication.CONTEXT
    var file: File? = null
    //android10以上转换
    if (uri.scheme == ContentResolver.SCHEME_FILE) {
        file = uri.path?.let { File(it) }
    } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //把文件复制到沙盒目录
        val contentResolver = context.contentResolver
        val displayName: String =
            (System.currentTimeMillis().toString() + ((Math.random() + 1) * 1000).roundToInt().toString() + "." + MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(uri)))
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val cache = File(context.cacheDir.absolutePath, displayName)
            val fos = FileOutputStream(cache)
            FileUtils.copy(inputStream!!, fos)
            file = cache
            fos.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return file
}


fun Uri.toFile(context: Context): File? {
    return when (scheme) {
        ContentResolver.SCHEME_FILE -> File(path!!)
        ContentResolver.SCHEME_CONTENT -> {
            val contentResolver = context.contentResolver
            val displayName = "${System.currentTimeMillis()}${Random.nextInt(1000)}.${
                contentResolver.getType(this)?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
            }"
            val cache = File(context.cacheDir.absolutePath, displayName)
            contentResolver.openInputStream(this)?.use { inputStream ->
                FileOutputStream(cache).use { fos ->
                    inputStream.copyTo(fos)
                }
            }
            cache
        }
        else -> null
    }
}

fun Uri.toFilePath(): String {

    val uriPath = this.path ?: return ""
    val path = uriPath.split(":")[1]
    return Environment.getExternalStorageDirectory().path + "/" + path
}


@Throws(IOException::class)
fun unzip(zipFilePath: File) {
    val buffer = 1024*1024

    val zipInputStream = ZipInputStream(FileInputStream(zipFilePath))
    //去除后缀
    val fileName  = zipFilePath.name.split(".")[0]
    var zipEntry = zipInputStream.nextEntry

    while (zipEntry != null) {
        val newFile = File(AppSystemSetManage.fileSavePath, fileName)

        if (zipEntry.isDirectory) {
            if (!newFile.isDirectory && !newFile.mkdirs()) {
                throw IOException("Failed to create directory: ${newFile.absolutePath}")
            }
        } else {
            // Create parent directories if they don't exist
            val parent = newFile.parentFile
            if (!parent.isDirectory && !parent.mkdirs()) {
                throw IOException("Failed to create directory: ${parent.absolutePath}")
            }

            // Write the file content
            FileOutputStream(newFile).use { fileOutputStream ->
                zipInputStream.copyTo(fileOutputStream, buffer)
            }
        }

        zipEntry = zipInputStream.nextEntry
    }

    zipInputStream.closeEntry()
    zipInputStream.close()
}

fun unzipFile(zipFile: File , bufferSize: Int = 1024*1024) {
    val buffer = ByteArray(bufferSize)
    try {

        try {
            // 创建目标文件夹
            val destDir = File(zipFile.parent, zipFile.nameWithoutExtension)
            if (!destDir.exists()) {
                destDir.mkdirs()
            }

            // 创建ZipInputStream对象
            val inputStream = FileInputStream(zipFile)
            val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))

            // 读取zip文件中的条目
            var entry: ZipEntry? = zipInputStream.nextEntry
            while (entry != null) {

                if (entry.name.startsWith(".")) {
                    // 跳过当前条目
                    entry = zipInputStream.nextEntry
                    continue
                }

                val newFile = File(destDir, entry.name)

                // 创建文件父目录
                val parent = newFile.parentFile
                if (!parent.exists()) {
                    parent.mkdirs()
                }

                // 创建目录或解压文件
                if (entry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    // 创建文件输出流
                    val fos = FileOutputStream(newFile)
                    val bos = BufferedOutputStream(fos)
                    var len: Int

                    // 将zip文件内容写入输出流
                    while (zipInputStream.read(buffer).also { len = it } > 0) {
                        bos.write(buffer, 0, len)
                    }

                    // 关闭输出流
                    bos.close()
                    fos.close()
                }

                // 关闭当前条目并定位到下一个条目
                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }

            // 关闭ZipInputStream
            zipInputStream.close()
            zipFile.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

