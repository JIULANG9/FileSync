package com.wordsfairy.filesync.utils.log

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/8/17 16:12
 */
object LogX {
    //设置 Logger
    public fun setLogger(logger: Logger): Unit {
        Logger.logger = logger
    }

    public fun getLogger(): Logger {
        return Logger.logger
    }

    public fun v(tag: String, msg: String) {
        Logger.logger.println(LogLevel.V, tag, msg, null, null)
    }

    public fun v(tag: String, msg: String, thr: Throwable) {
        Logger.logger.println(LogLevel.V, tag, msg, thr, null)
    }

    public fun v(tag: String, msg: String, thr: Throwable, param: Any?) {
        Logger.logger.println(LogLevel.V, tag, msg, thr, param)
    }

    public fun d(tag: String, msg: String) {
        Logger.logger.println(LogLevel.D, tag, msg, null, null)
    }

    public fun d(tag: String, msg: String, thr: Throwable) {
        Logger.logger.println(LogLevel.D, tag, msg, thr, null)
    }

    public fun d(tag: String, msg: String, thr: Throwable, param: Any?) {
        Logger.logger.println(LogLevel.D, tag, msg, thr, param)
    }

    public fun i(tag: String, msg: String) {
        Logger.logger.println(LogLevel.I, tag, msg, null, null)
    }

    public fun i(tag: String, msg: String, thr: Throwable) {
        Logger.logger.println(LogLevel.I, tag, msg, thr, null)
    }

    public fun i(tag: String, msg: String, thr: Throwable, param: Any?) {
        Logger.logger.println(LogLevel.I, tag, msg, thr, param)
    }

    public fun w(tag: String, msg: String) {
        Logger.logger.println(LogLevel.W, tag, msg, null, null)
    }

    public fun w(tag: String, msg: String, thr: Throwable) {
        Logger.logger.println(LogLevel.W, tag, msg, thr, null)
    }

    public fun w(tag: String, msg: String, thr: Throwable, param: Any?) {
        Logger.logger.println(LogLevel.W, tag, msg, thr, param)
    }

    public fun e(tag: String, msg: String) {
        Logger.logger.println(LogLevel.E, tag, msg, null, null)
    }

    public fun e(tag: String, thr: Throwable) {
        Logger.logger.println(LogLevel.E, tag, null, thr, null)
    }

    public fun e(tag: String, msg: String, thr: Throwable) {
        Logger.logger.println(LogLevel.E, tag, msg, thr, null)
    }

    public fun e(tag: String, msg: String, thr: Throwable, param: Any?) {
        Logger.logger.println(LogLevel.E, tag, msg, thr, param)
    }

    public fun wtf(tag: String, msg: String, thr: Throwable, param: Any?) {
        Logger.logger.println(LogLevel.WTF, tag, msg, thr, param)
    }


}

