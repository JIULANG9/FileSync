package com.wordsfairy.filesync.ui.widgets.text


import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.material.placeholder
import com.wordsfairy.filesync.ui.theme.WordsFairyTheme


/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2022/11/12 0:29
 */


@Composable
fun CommonsText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = WordsFairyTheme.colors.textSecondary,
    maxLines: Int = 99,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {

    Text(
        text = text,
        modifier = modifier
            .placeholder(
                visible = isLoading,
                color = WordsFairyTheme.colors.placeholder
            ),
        fontWeight = FontWeight.Normal,
        fontSize = H5,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
fun MiniText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = WordsFairyTheme.colors.textSecondary,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Title(
        title = text,
        modifier = modifier,
        fontSize = H7,
        color = color,
        maxLine = maxLines,
        textAlign = textAlign,
        isLoading = isLoading,
    )
}

@Composable
fun BigTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = H3,
        color = WordsFairyTheme.colors.textPrimary
    )
}

@Composable
fun WebTitle(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier.placeholder(
            visible = isLoading,
        ),
        fontSize = 15.sp,
        color = WordsFairyTheme.colors.textPrimary,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
fun AnnotatedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = H6,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal,
    canCopy: Boolean = false,
    isLoading: Boolean = false
) {
    if (canCopy) {
        SelectionContainer {
            Text(
                text = text,
                modifier = modifier
                    .placeholder(
                        visible = isLoading,
                        color = WordsFairyTheme.colors.whiteBackground
                    ),
                fontWeight = fontWeight,
                fontSize = fontSize,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlign
            )
        }
    } else {
        Text(
            text = text,
            modifier = modifier
                .placeholder(
                    visible = isLoading,
                    color = WordsFairyTheme.colors.whiteBackground
                ),
            fontWeight = fontWeight,
            fontSize = fontSize,
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlign
        )
    }
}


@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    color: Color = WordsFairyTheme.colors.textPrimary,
    fontWeight: FontWeight = FontWeight.Bold,
    maxLine: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Text(
        text = title,
        modifier = modifier
            .placeholder(
                visible = isLoading,
                color = WordsFairyTheme.colors.placeholder
            ),
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = color,
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
fun SubTitle(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    color: Color = WordsFairyTheme.colors.textSecondary,
    textAlign: TextAlign = TextAlign.Start
) {
    Title(
        title = title,
        modifier = modifier,
        fontSize = fontSize,
        color = color,
        maxLine = 2,
        textAlign = textAlign
    )
}

@Composable
fun TextSecondary(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = WordsFairyTheme.colors.textSecondary,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier.placeholder(
            visible = isLoading,
        ),
        fontSize = 15.sp,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
fun TextContent(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = WordsFairyTheme.colors.textSecondary,
    maxLines: Int = 99,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Title(
        title = text,
        modifier = modifier,
        fontSize = 16.sp,
        color = color,
        maxLine = maxLines,
        textAlign = textAlign,
        isLoading = isLoading
    )
}
@Composable
fun TextNoteContent(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = WordsFairyTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start
) {

    Text(
        text = text,
        modifier = modifier,
        fontSize = 17.sp,
        color = color,
        textAlign = textAlign
    )
}
/** 关键词高亮显示 */
@Composable
fun HighlightedText(
    content: String,
    highlighted: String,
    highlightColor: Color = WordsFairyTheme.colors.themeUi
) {
    val splitContent = content.split(highlighted, ignoreCase = true)
    val annotatedText = buildAnnotatedString {
        for (i in splitContent.indices) {
            withStyle(style = SpanStyle(color = WordsFairyTheme.colors.textSecondary)) {
                append(splitContent[i])
            }
            if (i != splitContent.size - 1) {
                withStyle(style = SpanStyle(color = highlightColor)) {
                    append(highlighted)
                }
            }
        }
    }

    Text(
        annotatedText, maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}


private val H1 = 46.sp  //超大号标题
private val H2 = 36.sp  //大号标题
private val H3 = 24.sp  //主标题
private val H4 = 20.sp  //普通标题
private val H5 = 16.sp  //内容文本
private val H6 = 13.sp  //普通文字尺寸
private val H7 = 12.sp  //提示语尺寸
