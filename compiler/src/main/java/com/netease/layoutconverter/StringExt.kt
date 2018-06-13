package com.netease.layoutconverter

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

/**
 * String扩展
 * Created by hzwangpeng2015 on 2017/12/4.
 */
private const val INTENT = "    "

internal fun String.indent(width: Int): String {
    if (isEmpty()) return this
    val intent = INTENT.repeat(width)
    return split('\n').joinToString("\n") { if (it.trim().isEmpty()) it else intent + it }
}

internal fun String.swapCamelCase(): String {
    val ch = withIndex().firstOrNull { Character.isUpperCase(it.value) }
    return if (ch == null) this else substring(ch.index).toLowerCase() + substring(0, ch.index).firstCapital()
}

/**
 * 首字母大写
 */
internal fun String.firstCapital(): String = if (isEmpty()) this else Character.toUpperCase(this[0]) + substring(1)

/**
 * 首字母小写
 */
internal fun String.firstLowercase(): String = if (isEmpty()) this else Character.toLowerCase(this[0]) + substring(1)

internal fun String.parseXml(): Document {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val inputSource = InputSource(StringReader(this))
    return builder.parse(inputSource)
}

data class KeyValuePair(val key: String, val value: String) {
    override fun toString() = if (value.isNotEmpty()) "$key = $value" else key
}

internal operator fun String.times(value: String) = KeyValuePair(this, value)

internal fun String.subStringFromLastDot(): String = this.substring(this.lastIndexOf(".") + 1)

/*-- android --*/
internal fun String.dp(): String {
    val ch = withIndex().firstOrNull { Character.isUpperCase(it.value) }
    return if (ch == null) this else substring(ch.index).toLowerCase() + substring(0, ch.index).firstCapital()
}

