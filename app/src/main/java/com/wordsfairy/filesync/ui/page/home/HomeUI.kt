package com.wordsfairy.filesync.ui.page.home

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wordsfairy.filesync.MainActivity
import com.wordsfairy.filesync.ext.coreui.rememberFlowWithLifecycle
import com.wordsfairy.filesync.ext.flow.wordsFairyStartWith
import com.wordsfairy.filesync.ui.common.vibration
import com.wordsfairy.filesync.ui.theme.AppResId
import com.wordsfairy.filesync.ui.theme.WordsFairyTheme
import com.wordsfairy.filesync.ui.widgets.ImmerseOutlinedCard
import com.wordsfairy.filesync.ui.widgets.button.MyIconButton
import com.wordsfairy.filesync.ui.widgets.progress.ProgressCard
import com.wordsfairy.filesync.ui.widgets.text.Title
import com.wordsfairy.filesync.ui.widgets.toast.ToastModel
import com.wordsfairy.filesync.ui.widgets.toast.showToast

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/7/5 18:21
 */

@Composable
fun HomeUI() {

    val viewModel: HomeViewModel = hiltViewModel()

    val viewState by viewModel.viewStateFlow.collectAsState()
    val singleEvent = rememberFlowWithLifecycle(viewModel.singleEvent)
    val intentChannel = remember { Channel<ViewIntent>(Channel.UNLIMITED) }

    val feedback = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current


    LaunchedEffect(viewModel) {
        intentChannel
            .consumeAsFlow()
            .wordsFairyStartWith(ViewIntent.Initial)
            .onEach(viewModel::processIntent)
            .collect()
    }

    LaunchedEffect(singleEvent) {
        singleEvent.collectLatest { event ->
            when (event) {
                is SingleEvent.UI.Success -> {
                    focusManager.clearFocus()
                }

                is SingleEvent.UI.ShowToast -> {
                    event.toastModel.showToast()
                }

            }
        }
    }

    val selectFolderResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { data ->
            val uri = data.data?.data
            if (uri != null) {
                intentChannel.trySend(ViewIntent.SelectFolder(uri))
            } else {
                ToastModel("选择困难! ƪ(˘⌣˘)ʃ", ToastModel.Type.Info).showToast()
            }
        }

    Box(
        Modifier
            .fillMaxSize()
            .background(WordsFairyTheme.colors.whiteBackground)
            .systemBarsPadding()

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(16.dp))
                Title(title = "文件夹同步")
                //Box实现一个小圆点
                if (viewState.isConnected) {
                    Box(
                        Modifier
                            .padding(start = 6.dp, top = 6.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(WordsFairyTheme.colors.themeUi)
                    )
                }
                Spacer(Modifier.weight(1f))
                /** 扫码按钮 */
                MyIconButton(painter = AppResId.Drawable.Set) {
                    viewModel.heartbeat()
                }
                Spacer(Modifier.width(16.dp))
            }
            ImmerseOutlinedCard(
                Modifier
                    .fillMaxWidth()
                    .padding(26.dp), onClick = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                    selectFolderResult.launch(intent)
                }) {
                Title(title = viewState.title, fontSize = 15.sp)
            }

            ProgressCard(
                Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                "下载文件", viewState.downloadProgress, onClick = {
                    viewModel.filesyncs()
                })
            ProgressCard(
                Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                "文件夹同步", viewState.syncProgress, onClick = {
                    feedback.vibration()
                    viewModel.folderyncs()
                })
        }

    }
}