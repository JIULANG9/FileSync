package com.wordsfairy.filesync.ui.widgets.toast


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordsfairy.filesync.constants.EventBus
import com.wordsfairy.filesync.ext.flowbus.postEventValue
import com.wordsfairy.filesync.ui.theme.WordsFairyTheme

import kotlinx.coroutines.delay

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/8/3 11:00
 */

private const val AnimationDurationMillis = 300


fun PillButtonModel.showToast() {
    postEventValue(
        EventBus.ShowPillButton,
        this
    )
}


@Stable
class PillButtonState {

    public var currentData: PillButtonData? by mutableStateOf(null)
        private set

    suspend fun show(
        toastModel: PillButtonModel
    ) {
        currentData = DataImpl(
            toastModel.message,
            toastModel.type,
        )

    }

    suspend fun hide() {
        currentData = DataImpl(
            isShow = false
        )
    }

    @Stable
    private class DataImpl(
        override val message: String = "",
        override val type: PillButtonModel.Type? = PillButtonModel.Type.Text,
        override val isShow: Boolean = true,
    ) : PillButtonData

}

interface PillButtonData {
    val message: String
    val type: PillButtonModel.Type?
    val isShow: Boolean
}

open class PillButtonModel(
    open val message: String = "",
    open val type: Type = Type.Text,
    open val show: Boolean = true,
) {
    enum class Type {
        Text, Loading
    }
}
fun hidePillButtonModel(){
    PillButtonModel(show = false).showToast()
}
fun showPillButtonModel(string: String){
    PillButtonModel(string).showToast()
}

fun showPillButtonModelLoad(string: String){
    PillButtonModelLoad(string).showToast()
}
data class PillButtonModelLoad(
    override val message: String,
) : PillButtonModel(message, Type.Loading)


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PillButton(
    hostState: PillButtonState,
) {
    val currentData = hostState.currentData ?: return

    key(currentData) {

        var state by remember { mutableStateOf(false) }


        val transition = updateTransition(targetState = state, label = "pillButton")

        val isLoading = currentData.type == PillButtonModel.Type.Loading
        LaunchedEffect(Unit) {
            state = currentData.isShow
            if (!isLoading) {
                delay(2000)
                state = false
            }
        }
        transition.AnimatedVisibility(
            visible = { it },
            enter = fadeIn(
                tween(
                    durationMillis = AnimationDurationMillis,
                    easing = EaseOutBack,
                ),
            ) + slideInVertically(
                animationSpec = tween(
                    durationMillis = AnimationDurationMillis,
                    easing = EaseOutBack,
                ),
                initialOffsetY = { fullHeight -> -fullHeight },
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = AnimationDurationMillis,
                    easing = EaseInBack,
                ),
            ) + slideOutVertically(
                animationSpec = tween(
                    durationMillis = AnimationDurationMillis,
                    easing = EaseInBack,
                ),
                targetOffsetY = { fullHeight -> -fullHeight },
            ),
        ) {
            Box(Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(top = 6.dp)
                        .align(Alignment.Center),
                    colors = CardDefaults.cardColors(
                        containerColor = WordsFairyTheme.colors.themeUi
                    ),
                    onClick = {
                        state = false
                    },
                    shape = RoundedCornerShape(26.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(
                        Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = WordsFairyTheme.colors.textWhite,
                                strokeCap = StrokeCap.Round,
                            )
                            Spacer(Modifier.width(6.dp))
                        }
                        Text(
                            text = currentData.message,
                            fontSize = 16.sp,
                            color = WordsFairyTheme.colors.textWhite
                        )
                    }
                }
            }

        }
    }
}

