package com.wordsfairy.filesync.ui.page.home


import android.util.Base64
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.wordsfairy.filesync.base.BaseViewModel
import com.wordsfairy.filesync.constants.GlobalData
import com.wordsfairy.filesync.data.AppSystemSetManage
import com.wordsfairy.filesync.data.entity.FileSendingDot
import com.wordsfairy.filesync.data.entity.SocketResultDto
import com.wordsfairy.filesync.data.entity.SocketType
import com.wordsfairy.filesync.ext.toTypeEntity
import com.wordsfairy.filesync.ui.widgets.toast.ToastModelSuccess
import com.wordsfairy.filesync.utils.file.toFilePath
import com.wordsfairy.filesync.utils.file.unzipFile
import com.wordsfairy.filesync.utils.log.LogX
import com.wordsfairy.filesync.websocket.FilesyncClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/9/18 20:26
 */

@HiltViewModel
class HomeViewModel @Inject internal constructor(
) : BaseViewModel<ViewIntent, ViewState, SingleEvent>() {

    override val viewStateFlow: StateFlow<ViewState>
    private var webSocket: FilesyncClient? = null


    init {
        val initialVS = ViewState.initial()

        viewStateFlow = merge(
            intentSharedFlow.filterIsInstance<ViewIntent.Initial>().take(1),
            intentSharedFlow.filterNot { it is ViewIntent.Initial }
        )
            .toPartialChangeFlow()
            .sendSingleEvent()
            .scan(initialVS) { vs, change -> change.reduce(vs) }
            .catch {
                Log.e(logTag, "[CreateNoteViewModel] Throwable:", it)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialVS
            )

    }

    private fun Flow<PartialChange>.sendSingleEvent(): Flow<PartialChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialChange.UIData.Success -> SingleEvent.UI.ShowToast(ToastModelSuccess("成功"))
                is PartialChange.UIData.ReceivingCompleted -> SingleEvent.UI.ShowToast(
                    ToastModelSuccess("同步完成")
                )

                is PartialChange.UIData.UnzipSuccess -> SingleEvent.UI.ShowToast(ToastModelSuccess("解压完成"))

                else -> return@onEach
            }
            sendEvent(event)
        }
    }

    @OptIn(FlowPreview::class)
    private fun Flow<ViewIntent>.toPartialChangeFlow(): Flow<PartialChange> =
        shareWhileSubscribed().run {

            val initFlow = filterIsInstance<ViewIntent.Initial>()
                .log("init")
                .flatMapConcat { noteVo ->

                    flow {
                        webSocket = FilesyncClient(viewModelScope)
                        GlobalData.webSocket = webSocket
                        val result = webSocket!!.connect().flatMapConcat {
                            parseResponse(it)
                        }

                        emitAll(result)
                    }.flowOn(Dispatchers.IO)
                }

            //选择文件夹
            val selectFolderFlow = filterIsInstance<ViewIntent.SelectFolder>()
                .log("选择文件夹").map {
                    // 如果uri以file开头,直接获取路径
                    val filePath = it.folderUri.toFilePath()
                    AppSystemSetManage.fileSavePath = filePath
                    PartialChange.UI.TitleChange(filePath)
                }
            return merge(
                initFlow,
                selectFolderFlow
            )
        }


    private suspend fun parseResponse(
        json: String
    ) = flow {
        try {
            val result = json.toTypeEntity<SocketResultDto>()
            when (result?.type) {
                SocketType.FILE_SYNC.type -> {

                    emit(PartialChange.UIData.Success)
                }

                SocketType.CLIPBOARD_SYNC.type -> {

                    emit(PartialChange.UIData.Clipboard)
                }
                //保存文件
                SocketType.FILE_SENDING.type -> {
                    //接收文件名
                    val fileSendingDot = result.data.toTypeEntity<FileSendingDot>()
                    emitAll(saveFile(fileSendingDot!!,true))
                }
                //保存文件夹
                SocketType.FOLDER_SYNCING.type -> {
                    //接收文件名
                    val fileSendingDot = result.data.toTypeEntity<FileSendingDot>()
                    emitAll(saveFolder(fileSendingDot!!))
                }

                SocketType.FILE_SENDEND.type -> {

                    delay(1000)
                    emit(PartialChange.UIData.SyncProgress(0f))
                    emit(PartialChange.UIData.DownloadProgress(0f))
                }

                SocketType.HEARTBEAT.type -> {
                    //剪贴板同步，预留的接口，还没开发
                    emit(PartialChange.UIData.ConnectedSuccess)
                }

                else -> {
                    emit(PartialChange.UIData.Clipboard)
                }
            }

        } catch (e: Exception) {
            LogX.e("ViewModelExt", "Exception $json", e)
            PartialChange.Request.Failure("解析失败 ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 保存文件
     * @param dot FileSendingDot
     * @return Flow<ReceivingProgress>
     */
    private fun saveFile(dot: FileSendingDot,download:Boolean,callback: (File) -> Unit = {}) = flow {

        val fileName = dot.fileName
        val file = File(AppSystemSetManage.fileSavePath, fileName)
        val fileData = Base64.decode(dot.data, Base64.DEFAULT)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            FileOutputStream(file, true).use { fos ->
                fos.write(fileData)
                fos.flush()
            }
            val progress =dot.sent.toFloat() / dot.total.toFloat()
            if (download){
                emit(PartialChange.UIData.DownloadProgress(progress))
            }else{
                emit(PartialChange.UIData.SyncProgress(progress))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (dot.total == dot.sent) {
            callback.invoke(file)
            emit(PartialChange.UIData.UnzipSuccess)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 保存文件夹
     * @param dot FileSendingDot
     * @return Flow<ReceivingProgress>
     */
    private fun saveFolder(dot: FileSendingDot) = saveFile(dot,false) { file ->
        //解压文件
        unzipFile(file, dot.bufferSize)
    }

    fun filesyncs() {
        val dto = SocketResultDto.create(
            SocketType.FILE_SYNC,
            ""
        )
        webSocket?.send(dto)
    }

    fun folderyncs() {
        val dto = SocketResultDto.create(
            SocketType.FOLDER_SYNC,
            ""
        )
        webSocket?.send(dto)
    }
    fun heartbeat() {
        val dto = SocketResultDto.create(
            SocketType.HEARTBEAT,
            "hi"
        )
        webSocket?.send(dto)
    }
}