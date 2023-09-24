package com.wordsfairy.filesync.data


import com.wordsfairy.filesync.constants.AppConsppts
import com.wordsfairy.filesync.store.DataStoreUtils


/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2022/12/28 18:10
 */
object AppSystemSetManage {

    private const val Dark_Mode = "dark_mode"
    private const val Dark_Mode_FOLLOW_SYSTEM = "dark_mode_follow_system"


    private const val CloseAnimation = "close_animation"

    /**提醒设置密码*/
    private const val ReminderSetPassword = "reminder_set_password"

    /**文件保存路径*/
    private const val FileSavePath = "file_save_path"
    /**
     * 深色模式
     */
    var darkUI: Boolean
        get() = DataStoreUtils.readBooleanData(Dark_Mode, false)
        set(value) = DataStoreUtils.saveSyncBooleanData(Dark_Mode, value = value)

    fun setDarkMode(follow: Boolean) {
        darkUI = follow
    }

    /**
     * 关闭动画
     */
    var closeAnimation: Boolean
        get() = DataStoreUtils.readBooleanData(CloseAnimation, false)
        set(value) = DataStoreUtils.saveSyncBooleanData(CloseAnimation, value = value)

    /**
     * 深色模式跟随系统
     */
    var darkModeFollowSystem: Boolean
        get() = DataStoreUtils.readBooleanData(Dark_Mode_FOLLOW_SYSTEM, false)
        set(value) = DataStoreUtils.saveSyncBooleanData(Dark_Mode_FOLLOW_SYSTEM, value = value)

    fun followSystem(follow: Boolean) {
        darkModeFollowSystem = follow
    }
    /**
     * 文件保存路径
     */
    var fileSavePath: String
        get() = DataStoreUtils.readStringData(FileSavePath, AppConsppts.File.DefaultPath)
        set(value) = DataStoreUtils.saveSyncStringData(FileSavePath, value = value)

}

