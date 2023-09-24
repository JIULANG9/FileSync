package com.wordsfairy.filesync.ext

import android.os.Parcelable
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/8/19 16:12
 */



inline fun <reified T> String?.toTypeEntity( deserializer: DeserializationStrategy<T>? = null): T? {
    return when {
        this.isNullOrBlank() -> null
        null != deserializer -> Json.decodeFromString(deserializer, this)
        else -> Json.decodeFromString(this)
    }
}

/**
 * 使用 [json] 以及 [serializer] 将数据实体 [T] 转换为 [String]
 * > 转换失败返回 `""`
 */
inline fun <reified T> T?.toJsonString(serializer: SerializationStrategy<T>? = null): String {
    return when {
        null == this -> ""
        null != serializer -> Json.encodeToString(serializer, this)
        else -> Json.encodeToString(this)
    }
}
