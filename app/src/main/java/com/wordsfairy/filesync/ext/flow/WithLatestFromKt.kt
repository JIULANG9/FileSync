package com.wordsfairy.filesync.ext.flow

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference


/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2022/10/27 12:19
 */
internal class AtomicRef<T>  constructor(initialValue: T) {
    private val atomic = AtomicReference(initialValue)

    var value: T
        get() = atomic.get()
        set(value) = atomic.set(value)

    fun compareAndSet(expect: T, update: T): Boolean = atomic.compareAndSet(expect, update)
}
fun <A, B, R> Flow<A>.withLatestFrom(
    other: Flow<B>,
    transform: suspend (A, B) -> R
): Flow<R> {
    return flow {
        val otherRef = AtomicRef<Any?>(null)

        try {
            coroutineScope {
                launch(start = CoroutineStart.UNDISPATCHED) {
                    other.collect { otherRef.value = it ?: NULL_VALUE }
                }

                collect { value ->
                    emit(
                        transform(
                            value,
                            NULL_VALUE.unbox(otherRef.value ?: return@collect)
                        )
                    )
                }
            }
        } finally {
            otherRef.value = null
        }
    }
}

@JvmField
val NULL_VALUE: Symbol = Symbol("NULL_VALUE")
class Symbol(@JvmField val symbol: String) {
    override fun toString(): String = "<$symbol>"

    @Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
    public inline fun <T> unbox(value: Any?): T = if (value === this) null as T else value as T
}

