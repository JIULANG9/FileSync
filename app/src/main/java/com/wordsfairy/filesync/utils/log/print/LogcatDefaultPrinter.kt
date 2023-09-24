package com.wordsfairy.filesync.utils.log.print


import com.wordsfairy.filesync.utils.log.LogLevel
import com.wordsfairy.filesync.utils.log.format.BaseFormatStrategy
import com.wordsfairy.filesync.utils.log.format.LogcatDefaultFormatStrategy
import com.wordsfairy.filesync.utils.log.format.LogcatPrettyFormatStrategy

/**
 * 默认Loacat 打印机
 * @property printable  是否打印日志到 logcat
 * @property minLevel   最小日志输出级别
 * @property formatStrategy 日志格式策略
 */
open class LogcatDefaultPrinter(
    val printable: Boolean = true,
    val minLevel: LogLevel = LogLevel.V,
    val formatStrategy: LogcatDefaultFormatStrategy = LogcatPrettyFormatStrategy()
) : BaseLogcatPrinter() {

    override fun isPrint(): Boolean {
        return printable
    }

    override fun getPrintMinLevel(): LogLevel {
        return minLevel
    }

    override fun getLogcatFormatStrategy(): BaseFormatStrategy {
        return formatStrategy
    }

}