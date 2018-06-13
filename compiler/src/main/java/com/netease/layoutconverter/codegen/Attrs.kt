package com.netease.layoutconverter.codegen

data class NameValue(
        val name: String = "",
        val value: String = "")

data class Attr(
        val name: String = "",
        val format: List<String> = emptyList(),
        val flags: List<NameValue>? = null,
        val enum: List<NameValue>? = null,
        // below extend attr
        val valueClass: String? = null,
        val code: String? = null)

data class Styleable(
        val name: String = "",
        val attrs: List<Attr> = emptyList())

data class Attrs(
        val free: List<Attr> = emptyList(),
        val styleables: Map<String, Styleable> = emptyMap())

data class JavaAttr(val styleable: Styleable, val attr: Attr, val name: String, val value: String, /* 表示重复定义的属性 */var repeat: Boolean = false)

data class LayoutParam(
        val name: String,
        val width: String = "MATCH_PARENT",
        val height: String = "MATCH_PARENT",
        val attrs: List<JavaAttr>? = null)

data class Style(val name: String, val parent: String = "", val items: List<NameValue>)

data class Package(val name: String, val styles: List<Style>)

val noAttr: Attr = Attr()
val noStyleable = Styleable()
val noLayoutParams = LayoutParam("")

val defaultRootLayoutParams = "ViewGroup_MarginLayout"

internal fun String.layoutParamsTag(): String {
    if (isEmpty() || this == "ViewGroup_MarginLayout") return this
    return this.substring(this.lastIndexOf(".") + 1, this.length) + "_Layout"
}

internal val ignoreSetAttr = "~//ignore---]]]333@@!!!2017121300~----%&*()^~"