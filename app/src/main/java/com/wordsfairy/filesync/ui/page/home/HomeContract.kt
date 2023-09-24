package com.wordsfairy.filesync.ui.page.home

import android.net.Uri
import android.os.Parcelable
import com.wordsfairy.filesync.base.mvi.*
import com.wordsfairy.filesync.data.AppSystemSetManage
import com.wordsfairy.filesync.ui.widgets.toast.ToastModel
import kotlinx.parcelize.Parcelize

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/9/18 20:18
 */

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/7/30 11:51
 */

sealed interface ViewIntent : MviIntent {
    data object Initial : ViewIntent
    data class SelectFolder(val folderUri: Uri) : ViewIntent
}

@Parcelize
data class ViewState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val title: String,
    val downloadProgress: Float,
    val syncProgress: Float,
    val isConnected: Boolean,
) : MviViewState, Parcelable {
    companion object {
        fun initial() = ViewState(
            isLoading = true,
            isRefreshing = false,
            title = AppSystemSetManage.fileSavePath,
            downloadProgress = 0f,
            syncProgress = 0f,
            isConnected = false
        )
    }
}

sealed interface SingleEvent : MviSingleEvent {
    sealed interface UI : SingleEvent {
        data object Success : UI
        data class ShowToast(val toastModel: ToastModel) : UI

    }
}

internal sealed interface PartialChange {
    fun reduce(vs: ViewState): ViewState
    sealed class UIData : PartialChange {
        override fun reduce(vs: ViewState): ViewState {
            return when (this) {
                is Success -> vs.copy(isRefreshing = false)

                is Loading -> vs.copy(isRefreshing = true)
                is Clipboard -> vs
                is DownloadProgress -> vs.copy(downloadProgress = progress)
                is SyncProgress -> vs.copy(syncProgress = progress)
                is ReceivingCompleted -> vs.copy(downloadProgress = 1F,syncProgress = 1F)
                is ConnectedSuccess -> vs.copy(isConnected = true)
                is UnzipSuccess -> vs
            }
        }

        data object Loading : UIData()
        data object Success : UIData()
        data object Clipboard : UIData()
        data class DownloadProgress(val progress: Float) : UIData()
        data class SyncProgress(val progress: Float) : UIData()
        //接收完成
        data object ReceivingCompleted : UIData()
        data object ConnectedSuccess : UIData()
        //解压完成
        data object UnzipSuccess : UIData()
    }

    sealed class Request : PartialChange {
        override fun reduce(vs: ViewState): ViewState {
            return when (this) {

                is Failure -> vs
                is Warning -> vs
            }
        }
        data class Failure(val message: String) : Request()
        data class Warning(val message: String) : Request()
    }

    sealed class UI : PartialChange {
        override fun reduce(vs: ViewState): ViewState {
            return when (this) {
                is ShowToast -> vs.copy(isRefreshing = false)
                is TitleChange -> vs.copy(title = title)

            }
        }

        data class ShowToast(val msg: String) : UI()
        data class TitleChange(val title: String) : UI()
    }
}