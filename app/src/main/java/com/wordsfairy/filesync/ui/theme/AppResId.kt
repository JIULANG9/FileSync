package com.wordsfairy.filesync.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wordsfairy.filesync.R
import com.wordsfairy.filesync.base.BaseApplication

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/4/29 14:10
 */

object AppResId {


    object Drawable {
        val ArrowLeft : Painter
            @Composable get() = painterResource(id = R.drawable.app_ic_arrow_left)

        val Set: Painter
            @Composable get() = painterResource(id = R.drawable.app_ic_set)
    }
    object Mipmap {

        val EmptyData : Painter
            @Composable get() = painterResource(id = R.mipmap.app_ic_empty_data)
    }
    object StringText {

        /** 创建 */

        val Confirm : String // 确定
            @Composable get() = stringResource(R.string.app_confirm)
        val Cancel : String // 取消
            @Composable get() = stringResource(R.string.app_cancel)

        val Disagree : String // 不同意
            @Composable get() = stringResource(R.string.app_disagree)
        val Agree : String // 同意
            @Composable get() = stringResource(R.string.app_agree)

    }

}