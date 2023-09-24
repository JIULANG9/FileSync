package com.wordsfairy.filesync.data.entity

import android.os.Parcelable
import com.wordsfairy.filesync.ext.toJsonString
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/8/18 14:35
 */

@Serializable
data class SocketResultDto(
    val type: String,
    val msg: String,
    val data: String
) :java.io.Serializable{
    companion object {
        fun create(
            type: SocketType,
            data: String
        ): String {
            return SocketResultDto(
                type.type, type.msg, data
            ).toJsonString()
        }

        fun heartbeat(data: String): String {
            return SocketResultDto(
                SocketType.HEARTBEAT.type, SocketType.HEARTBEAT.msg, data
            ).toJsonString()
        }
    }
}


enum class SocketType(val type: String, val msg: String) {
    FILE_SYNC("FILE_SYNC", "文件同步"),
    FOLDER_SYNC("FOLDER_SYNC", "文件夹同步"),
    CLIPBOARD_SYNC("CLIPBOARD_SYNC", "剪贴板同步"),

    HEARTBEAT("HEARTBEAT", "心跳"),

    FILE_SENDING("FILE_SENDING", "发送中"),
    FOLDER_SYNCING("FOLDER_SYNCING", "文件夹同步中"),
    FILE_SENDEND("FILE_SENDEND", "发送完成");
}

@Serializable
@Parcelize
data class FileSendingDot(
    val fileName: String,
    val bufferSize: Int,
    val total: Long,
    val sent: Long,
    val data: String
): Parcelable
