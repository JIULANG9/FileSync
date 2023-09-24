package com.wordsfairy.filesync.ui.widgets


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onSizeChanged

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.wordsfairy.filesync.ui.common.click
import com.wordsfairy.filesync.ui.widgets.button.MyIconButton
import com.wordsfairy.filesync.ui.widgets.text.Title
import com.wordsfairy.filesync.ui.theme.AppResId

import com.wordsfairy.filesync.ui.theme.WordsFairyTheme
import com.wordsfairy.filesync.ui.widgets.button.MyIconButton
import com.wordsfairy.filesync.ui.widgets.text.Title


@Composable
fun CommonsTopBar(title: String, onBack: (() -> Unit)) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MyIconButton(
            AppResId.Drawable.ArrowLeft,
            "onBack",
            size = 21.dp,
            tint = WordsFairyTheme.colors.textSecondary,
            onClick = onBack
        )
        Title(title = title, fontSize = 21.sp)
    }
}

@Composable
fun CommonButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = WordsFairyTheme.colors.primaryBtnBg,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = containerColor
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text,
            Modifier.padding(horizontal = 26.dp, vertical = 3.dp),
            color = WordsFairyTheme.colors.textWhite,
            fontSize = 19.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImmerseCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = WordsFairyTheme.colors.itemBackground,
    contentColor: Color = contentColorFor(backgroundColor),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = border,
        content = content
    )
}

@Composable
fun ImmerseCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = WordsFairyTheme.colors.itemBackground,
    contentColor: Color = contentColorFor(backgroundColor),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = border,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImmerseOutlinedCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}, content: @Composable () -> Unit,
) {
    OutlinedCard(
        modifier =modifier,
        onClick = onClick,
        colors =
        CardDefaults.cardColors(
            WordsFairyTheme.colors.itemBackground
        )
    ) {
        Column(modifier = Modifier
            .padding(16.dp)) {
            content()
        }
    }
}


@Composable
fun ImmerseAlphaCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = WordsFairyTheme.colors.whiteBackground,
    contentColor: Color = contentColorFor(backgroundColor),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,

    alpha: Float = 0.618f,

    content: @Composable ColumnScope.() -> Unit
) {

    var fullSize by remember { mutableStateOf(IntSize(0, 0)) }
    Card(

        modifier = modifier,

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = border,
        content = {
            Box(Modifier.onSizeChanged { fullSize = it }) {

                Box(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { fullSize.width.toDp() })
                        .height(with(LocalDensity.current) { fullSize.height.toDp() })
                        .alpha(alpha)
                        .background(backgroundColor)
                ) {

                }
                Column(content = content)
            }
        })

}


@Composable
fun ImmerseCardItem(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ImmerseCard(modifier) {
        Column(modifier = Modifier.padding(vertical = 6.dp), content = content)
    }
}


@Composable
fun EmptyDataView(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = AppResId.Mipmap.EmptyData, contentDescription = "EmptyData",
        modifier = modifier.padding(12.dp)
    )
}


/**
 * 增加未读小红点
 */
fun Modifier.unread(read: Boolean, badgeColor: Color) = this
    .drawWithContent {
        drawContent()
        if (!read) {
            drawCircle(
                color = badgeColor,
                radius = 5.dp.toPx(),
                center = Offset(size.width - 1.dp.toPx(), 1.dp.toPx()),
            )
        }
    }
