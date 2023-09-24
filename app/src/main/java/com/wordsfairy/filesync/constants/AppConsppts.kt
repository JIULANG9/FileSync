package com.wordsfairy.filesync.constants

import android.os.Environment
import android.provider.Settings
import com.wordsfairy.filesync.base.BaseApplication

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/5/10 22:27
 */
object AppConsppts {

    object SearchEngines {
        const val Baidu = "https://www.baidu.com/s?wd="
        const val Bing = "https://cn.bing.com/search?q="
        const val Google = "https://www.google.com/search?q="
    }

    /** 服务协议网址 */
    const val URL_OfficialWebsite = "https://www.wordsfairy.cloud"
    const val URL_SERVICE_AGREEMENT = "https://www.wordsfairy.cloud/blackboard/service-agreement/index.html"
    /** 词仙隐私保护指引 */
    const val URL_PRIVACY_PROTECTION = "https://www.wordsfairy.cloud/blackboard/privacy/index.html"

    const val URL_JUEJIN= "https://juejin.cn/user/1943592291019373/posts"
    /** github */
    const val URL_GITHUB= "https://github.com/JIULANG9/WordsFairyNote"
    const val URL_GITEE= "https://gitee.com/JIULANG9/WordsFairyNote"

    object File {
         val DefaultPath = Environment.getExternalStorageDirectory().path+"/WordsFairy/FileSync"

    }
    object AnimationDuration {

        val duration = Settings.Global.getInt(BaseApplication.CONTEXT.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 0);

        const val NavHostDuration = 666
        const val ContentListDelay = 600L
        const val MEDIUM = 2000L
        const val LONG = 3000L

        val AppNavDelayDuration = NavHostDuration*duration
    }

    enum class BottomSheetUIType{
        Login ,SetPassword,ScanCode
    }
}