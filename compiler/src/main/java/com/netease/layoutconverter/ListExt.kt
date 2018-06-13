package com.netease.layoutconverter

/**
 * List扩展
 * Created by hzwangpeng2015 on 2017/12/4.
 */
internal fun <T : Any, R : Any> List<T>.findFirst(transformer: (T) -> R?): R? {
    return this
            .map { transformer(it) }
            .firstOrNull { it != null }
}

internal fun <T : Any, R : Any> List<T>.findFirst2(transformer: (T) -> R?): R? {
    this.forEach {
        val newIt = transformer(it)
        if (newIt != null) return newIt
    }
    return null
}