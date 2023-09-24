package com.wordsfairy.filesync.ext.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2022/10/4 17:24
 */
public fun <T> flowFromSuspend(function: suspend () -> T): Flow<T> = flow { emit(function()) }


infix fun <T> Flow<T>.wordsFairyStartWith(item: T): Flow<T> = concat(flowOf(item), this)
fun <T> concat(flow1: Flow<T>, flow2: Flow<T>): Flow<T> = flow {
    emitAll(flow1)
    emitAll(flow2)
}


@OptIn(FlowPreview::class)
inline fun <T, R> Flow<T>.flatMapFirst(crossinline transform: suspend (value: T) -> Flow<R>): Flow<R> =
    flatMapConcat { value ->
        transform(value).take(1)
    }

