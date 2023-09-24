package com.wordsfairy.filesync.utils.log.print

import com.wordsfairy.filesync.utils.log.LogLevel


interface IPrinter {
    open fun print(logLevel: LogLevel, tag:String?, msg:String?, thr: Throwable?, param:Any?)
}