package com.wordsfairy.filesync.utils.log.print

import android.util.Log

import com.wordsfairy.filesync.utils.log.LogLevel
import com.wordsfairy.filesync.utils.log.format.BaseFormatStrategy
import com.wordsfairy.filesync.utils.log.print.IPrinter

abstract class BaseLogcatPrinter: IPrinter {

    override fun print(logLevel: LogLevel, tag:String?, msg:String?, thr: Throwable?, param:Any?){

        val msg = getLogcatFormatStrategy().format(logLevel,tag, msg, thr)
        Log.println(logLevel.logLevel,tag, msg)
    }

    //是否打印日志
    abstract fun isPrint():Boolean
    //最小打印级别，比他低级的日志不会打印
    abstract fun getPrintMinLevel(): LogLevel
    //日志输出格式
    abstract fun getLogcatFormatStrategy(): BaseFormatStrategy




}