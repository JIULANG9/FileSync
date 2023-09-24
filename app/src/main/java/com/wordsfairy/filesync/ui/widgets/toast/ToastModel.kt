package com.wordsfairy.filesync.ui.widgets.toast

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/7/5 1:02
 */

open class ToastModel(
    open val message: String,
    val type: Type = Type.Normal,
    open val durationTime: Long? = null,
) {
    enum class Type {
        Normal, Success, Info, Warning, Error,
    }
}

fun toastModelSuccess(message: String) = ToastModelSuccess(message).showToast()
data class ToastModelSuccess(
    override val message: String,
) : ToastModel(message, Type.Success, null)

fun toastModelInfo(message: String) = ToastModelInfo(message).showToast()
data class ToastModelInfo(
    override val message: String,
) : ToastModel(message, Type.Info, null)

fun toastModelError(message: String) = ToastModelError(message).showToast()
data class ToastModelError(
    override val message: String,
) : ToastModel(message, Type.Error, null)

fun toastModelWarning(message: String) = ToastModelWarning(message).showToast()
data class ToastModelWarning(
    override val message: String,
) : ToastModel(message, Type.Warning, null)


