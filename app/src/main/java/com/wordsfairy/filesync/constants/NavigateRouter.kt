package com.wordsfairy.filesync.constants

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/4/24 11:52
 */
object NavigateRouter {

    private const val ROUTER_GROUP = "router/"
    object HomePage {
        /** 首页 */
        private const val ROUTER_GROUP_HOME = "${ROUTER_GROUP}home"
        const val AppNav = "$ROUTER_GROUP_HOME/app_nav"

        const val Page = "$ROUTER_GROUP_HOME/homepage"
        const val FolderManage = "$ROUTER_GROUP_HOME/folder_manage"
    }

    object Wonderland{
        /** 仙境 */
        private const val ROUTER_GROUP_WONDERLAND = "${ROUTER_GROUP}wonderland"
        const val Page = "$ROUTER_GROUP_WONDERLAND/wonderland"

        const val Welcome = "$ROUTER_GROUP_WONDERLAND/welcome"
        /** 设置密码 */
        const val SetPassword = "$ROUTER_GROUP_WONDERLAND/setPassword"

        const val RestorePassword = "$ROUTER_GROUP_WONDERLAND/restorePassword"
    }

    object Set {
        /** 全局设置 */
        private const val ROUTER_GROUP_SET = "set_page"

        const val Page = "$ROUTER_GROUP_SET/home"
        const val NoteData = "$ROUTER_GROUP_SET/notedata"
        const val BackupsProgressBar = "$ROUTER_GROUP_SET/notedata/backupsprogressbar"
        const val BackupsQRCode = "$ROUTER_GROUP_SET/notedata/backupsqrcode"

  
    }
}