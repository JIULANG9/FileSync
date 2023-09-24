package com.wordsfairy.filesync.ui.widgets.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.wordsfairy.filesync.ui.theme.WordsFairyTheme
import com.wordsfairy.filesync.ui.widgets.text.Title

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/9/23 14:46
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
    onClick: () -> Unit = {}
) {
    val progressColor = WordsFairyTheme.colors.themeAccent
    //通过判断progress的值来决定是否显示加载
    val load = progress > 0F

    val textColor = if (load) WordsFairyTheme.colors.themeUi else WordsFairyTheme.colors.textPrimary
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        colors =
        CardDefaults.cardColors(WordsFairyTheme.colors.itemBackground),
        border = BorderStroke(1.dp, textColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val fraction = progress * size.width
                    drawRoundRect(
                        color = progressColor,
                        size = Size(width = fraction, height = size.height),
                        cornerRadius = CornerRadius(12.dp.toPx()),
                        alpha = 0.9f,
                    )
                },
            content = {
                Row {
                    Title(
                        title = title, Modifier.padding(16.dp),
                        color = textColor
                    )
                    Spacer(Modifier.weight(1f))
                    if (load)
                        Title(
                            title = "${(progress * 100).toInt()}%", Modifier.padding(16.dp),
                            color = textColor
                        )
                }
            }
        )
    }
}