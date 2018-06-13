package com.netease.layoutconverter.codegen

import com.netease.layoutconverter.KeyValuePair
import com.netease.layoutconverter.ResourceParser.attrs
import com.netease.layoutconverter.findFirst2
import com.netease.layoutconverter.getLayoutParamsHierarchy
import com.netease.layoutconverter.getViewHierarchy
import java.util.regex.Pattern

internal fun transformLayoutAttributes(attributes: List<KeyValuePair>, parentName: String)
        : LayoutParam {
    val map = attributes.map { it.key.replace("android:layout_", "") to it.value }.toMap()

    fun renderLayoutDimension(s: String) = when (s) {
        "wrap_content" -> "WRAP_CONTENT"
        "match_parent", "fill_parent" -> "MATCH_PARENT"
        else -> s.parseDimension()
    }

    val width = renderLayoutDimension(map["width"] ?: "wrap_content")
    val height = renderLayoutDimension(map["height"] ?: "wrap_content")

    val javaAttrList = ArrayList<JavaAttr>()
    attributes.forEach {
        if (!it.key.contains("android:layout_width") && !it.key.contains("android:layout_height")) {
            val javaAttr = transformLayoutAttribute(parentName, it.key, it.value)
            if (javaAttr != null && javaAttr.attr != noAttr) javaAttrList.add(javaAttr)
        }
    }

    return LayoutParam(getLayoutParamsHierarchy(parentName)[0], width, height, javaAttrList)
}

internal fun transformLayoutAttribute(lpName: String, attrName: String, attrValue: String): JavaAttr? {
    val layoutHierarchy = getLayoutParamsHierarchy(lpName)
    return transformCommon(attrName, attrValue, layoutHierarchy)
}

internal fun transformAttribute(viewName: String, attrName: String, attrValue: String): JavaAttr? {
    val viewHierarchy = getViewHierarchy(viewName)
    return transformCommon(attrName, attrValue, viewHierarchy)
}

internal fun transformCommon(attrName: String, attrValue: String, hierarchy: List<String>): JavaAttr? {
    return when {
        attrName.startsWith("xmlns:") -> null
        attrName.startsWith("tools:") -> null
        attrName == "style" -> null
        else ->  {
            val shortName = attrName.substring(attrName.lastIndexOf(":") + 1)

            var styleable: Styleable? = null
            var attr: Attr? = attrs.free.firstOrNull { it.name == shortName }
            if (attr == null) {
                attr = hierarchy.findFirst2 {
                    styleable = attrs.styleables[it]
                    styleable?.attrs?.firstOrNull { it.name == shortName && it.format.isNotEmpty() }
                }
                if (attr == null) {
                    attr = hierarchy.findFirst2 {
                        styleable = attrs.styleables[it]
                        styleable?.attrs?.firstOrNull { it.name == shortName }
                    }
                }
            } else {
                if (hierarchy.findFirst2 {
                    styleable = attrs.styleables[it]
                    styleable?.attrs?.firstOrNull { it.name == shortName }
                } == null) styleable = null
            }
            if (attr != null && attr.format.isEmpty()) {
                val withFormatAttr = hierarchy.findFirst2 {
                    val styleable2 = attrs.styleables[it]
                    styleable2?.attrs?.firstOrNull { it.name == shortName && it.format.isNotEmpty() }
                }
                if (withFormatAttr != null) attr = withFormatAttr
            }

            if (attr == null) {
                attr = noAttr
                styleable = noStyleable
            }
            if (styleable == null) {
                styleable = noStyleable
            }

            if (attrValue.startsWith("@{")) // Ignore data binding attr
                JavaAttr(styleable!!, attr, shortName, ignoreSetAttr)
            else
                renderAttribute(styleable!!, attr, shortName, attrValue)
        }
//        else -> JavaAttr(noStyleable, noAttr, attrName, attrValue)
    }
}

private fun renderAttribute(styleable: Styleable, attr: Attr, key: String, value: String): JavaAttr? {
    return viewAttrRenderers
            .map { it(styleable, attr, key, value) }
            .firstOrNull { it != null }
}

fun String.parseInteger(): String {
    return when {
        this.matches("-?[0-9]+".toRegex()) -> this
        this.isReference() -> this.parseReference().toString()
        else -> "0"
    }
}

fun String.parseReference(): ReferenceParser {
    return if (this == "@null") {
        ReferenceParser("", "null", "")
    } else {
        val matcher = Pattern.compile("@((([A-Za-z0-9._]+):)?)([+A-Za-z0-9_]+)/?([A-Za-z0-9_.]+)").matcher(this)
        if (!matcher.matches()) {
            throw RuntimeException("Invalid reference: $this")
        }
        ReferenceParser(matcher.group(3) ?: "", matcher.group(4) ?: "", matcher.group(5))
    }
}

fun String.parseDrawable(): String {
    return when {
        this == "@null" -> DrawableReferenceParser("", "null", "").toString()
        this.startsWith("?") -> "getThemeDrawable(android.R.attr.${this.substring(this.lastIndexOf("/") + 1)})"
        this.startsWith("#") -> "new ColorDrawable(${parseDirectColor()})"
        else -> {
            val matcher = Pattern.compile("@((([A-Za-z0-9._]+):)?)([+A-Za-z0-9_]+)/?([A-Za-z0-9_.]+)").matcher(this)
            if (!matcher.matches()) {
                throw RuntimeException("Invalid reference: $this")
            }
            DrawableReferenceParser(matcher.group(3) ?: "", matcher.group(4) ?: "", matcher.group(5)).toString()
        }
    }
}

fun String.parseColorStateList(): String {
    return when {
        // #fffffffff的形式
        this.startsWith("#") -> {
            return "ColorStateList.valueOf(${parseDirectColor()})"
        }

        // null处理
        this == "@null" -> ColorStateListParser("", "null", "").toString()

        // 引用
        else -> {
            val matcher = Pattern.compile("@((([A-Za-z0-9._]+):)?)([+A-Za-z0-9_]+)/?([A-Za-z0-9_.]+)").matcher(this)
            if (!matcher.matches()) {
                throw RuntimeException("Invalid reference: $this")
            }
            ColorStateListParser(matcher.group(3) ?: "", matcher.group(4) ?: "", matcher.group(5)).toString()
        }
    }
}

/**
 * 解析"#ffddeecc"格式颜色的值
 */
fun String.parseDirectColor(): String {
    val color = replace("#", "").toLowerCase()
    return when (color.length) {
        3 -> "0xff${color[0]}${color[0]}${color[1]}${color[1]}${color[2]}${color[2]}"
        6 -> "0xff$color"
        else -> "0x$color"
    }
}

fun String.parseFlagValue(): Int =
        if (startsWith("0x")) Integer.parseInt(this.substring(2), 16) else this.toInt()

fun String.isReference(): Boolean = startsWith("@")

fun String.isSpecialReferenceAttribute() = when (this) {
    "text" -> true
    "background" -> true
    else -> false
}

fun String.isDimension(): Boolean =
        endsWith("sp") || endsWith("dp") || endsWith("px") || endsWith("dip")

fun String.isColor(): Boolean = toLowerCase().matches("#[0-9a-f]+".toRegex())

fun String.parseColor(): String {
    if (isReference()) {
        return try {
            this.parseReference().toString()
        } catch (e: Exception) {
            ignoreSetAttr
        }
    }

    val color = replace("#", "").toLowerCase()
    return when (color.length) {
        3 -> "0xff$color[0]$color[0]$color[1]$color[1]$color[2]$color[2]"
        6 -> "0xff$color"
        else -> "0x$color"
    }
}

fun String.parseDimension(): String {
    return when {
        isReference() -> this.parseReference().toString()
        startsWith("?") -> "getThemeDimension(android.R.attr.${this.substring(this.lastIndexOf("/") + 1)})"
        this == "match_parent" || this == "fill_parent" -> "MATCH_PARENT"
        this == "wrap_content" -> "WRAP_CONTENT"
        else -> {
            val matcher = Pattern.compile("(-?[0-9.]+)(dip|dp|px|sp|in|mm|pt)").matcher(this)
            if (!matcher.matches()) {
                throw RuntimeException("Invalid dimension: $this")
            }
            var numericValue = matcher.group(1)
            numericValue = if ("." in numericValue) "${numericValue}f" else numericValue
            val unit = matcher.group(2)
            when (unit) {
                "dip", "dp" -> "dp($numericValue, context)"
                "sp" -> "sp($numericValue, context)"
                "mm" -> "mm($numericValue, context)"
                "in" -> "in($numericValue, context)"
                "pt" -> "pt($numericValue, context)"
                else -> numericValue
            }
        }
    }
}