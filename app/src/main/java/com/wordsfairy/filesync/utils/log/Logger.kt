package com.wordsfairy.filesync.utils.log


import com.wordsfairy.filesync.utils.log.format.LogcatPrettyFormatStrategy
import com.wordsfairy.filesync.utils.log.print.BaseLogTxtPrinter
import com.wordsfairy.filesync.utils.log.print.BaseLogcatPrinter
import com.wordsfairy.filesync.utils.log.print.LogcatDefaultPrinter


/**
 * Logger 实体
 */
open class Logger {

    internal companion object{
         var logger =  Builder()
             .setLogcatPrinter(LogcatDefaultPrinter())

             .setIsDebug(true)
             .build()
    }

    private lateinit var builder: Builder
    private constructor(build: Builder){
        builder = build

    }

    internal fun isDebug():Boolean{
       return builder.isDebug
    }

    fun getLogcatPrinter(): BaseLogcatPrinter?{
        return builder.logcatPrinter
    }

    fun getLogTxtPrinter(): BaseLogTxtPrinter?{
        return builder.logTxtPrinter
    }

    /**
     * 打印日志
     * @param level 日志等级
     * @param tag
     * @param msg 日志消息
     * @param thr 异常消息
     * @param param 其他参数
     */
    open fun println(level: LogLevel, tag:String?, msg:String?, thr:Throwable?,param:Any?){
        if(this::builder.isInitialized){
            builder.logcatPrinter?.print(level, tag, msg, thr,param)
            builder.logTxtPrinter?.print(level,tag,  msg, thr,param)
        }
    }

    class Builder{
        //控制台打印器
        internal var logcatPrinter: BaseLogcatPrinter? = LogcatDefaultPrinter()
        //日志文件打印机
        internal var logTxtPrinter: BaseLogTxtPrinter? = null


        internal var isDebug = false

        fun setLogcatPrinter(logcatPrinter: BaseLogcatPrinter?): Builder {
            this.logcatPrinter = logcatPrinter
            return this
        }

        fun setLogTxtPrinter(logTxtPrinter: BaseLogTxtPrinter?): Builder {
            this.logTxtPrinter = logTxtPrinter
            return this
        }

        fun setIsDebug(isDebug:Boolean):Builder{
            this.isDebug = isDebug
            return this
        }

        fun build(): Logger {
            return Logger(this)
        }

    }

}