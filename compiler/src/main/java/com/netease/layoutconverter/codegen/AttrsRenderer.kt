package com.netease.layoutconverter.codegen

internal val viewAttrRenderers = listOf(
        ::renderValueClass,
        ::render404,

        ::renderTextViewMaxLength,

        ::renderGravity,
        ::renderBackground,
        ::renderLinearLayoutOrientation,
        ::renderTag,
        ::renderVisibility,
        ::renderSingleLine,
        ::renderOrientation,
        ::renderColor,
        ::renderReference,
        ::renderDimension,
        ::renderString,
        ::renderBoolean,
        ::renderInteger,
        ::renderFloat,
        ::renderEnum,
        ::renderFlags,
        ::renderIncludeLayout,
        ::renderEllipsize,
        ::renderScaleType,
        ::render
)

internal fun renderValueClass(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr.valueClass != null && attr.valueClass.isNotEmpty()) {
    when {
        attr.valueClass.equals("ColorStateList", true) -> JavaAttr(styleable, attr, key, value.parseColorStateList())
        attr.valueClass.equals("drawable", true) -> JavaAttr(styleable, attr, key, value.parseDrawable())
        attr.valueClass.equals("int", true) -> if ("color" in attr.format) JavaAttr(styleable, attr, key, value.parseColor()) else null
        attr.valueClass.equals("reference", true) -> {
            val reference = try {
                value.parseReference().toReference()
            } catch (e: Exception) {
                if (value.startsWith("?")) "android.R.attr.${value.substring(value.lastIndexOf("/") + 1)}"
                else ignoreSetAttr
            }
            JavaAttr(styleable, attr, key, reference)
        }
        else -> null
    }
} else null

internal fun render404(styleable: Styleable, attr: Attr, key: String, value: String) = if (styleable == noStyleable || attr == noAttr) {
    JavaAttr(styleable, attr, key, value)
} else null


internal fun renderTextViewMaxLength(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr.name == "maxLength") {
    val integer = value.parseInteger()
    val v = "new InputFilter[] { new InputFilter.LengthFilter($integer) }"
    JavaAttr(styleable, attr, key, v)
} else null

internal fun renderIncludeLayout(styleable: Styleable, attr: Attr, key: String, value: String) = if (styleable.name == "Include" && attr.name == "layout") {
    JavaAttr(styleable, attr, key, value.replace("@layout/", ""))
} else null

@Suppress("UNUSED_PARAMETER")
internal fun renderGravity(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr.name == "gravity" || attr.name == "layout_gravity") {
    val values = value.split('|').map { "Gravity.${it.toUpperCase()}" }
    JavaAttr(styleable, attr, key, values.joinToString(" | "))
} else null

@Suppress("UNUSED_PARAMETER")
internal fun renderBackground(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr.name == "background") {
    when {
        value.isReference() -> {
            val reference = try {
                value.parseDrawable()
            } catch (e: Exception) {
                ignoreSetAttr
            }
            JavaAttr(styleable, attr, key, reference)
        }
        value.isColor() -> JavaAttr(styleable, attr, key, getColorDrawable(value.parseColor()))
        else -> null
    }
} else null

internal fun renderLinearLayoutOrientation(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr.name == "orientation" && styleable.name == "LinearLayout")
    JavaAttr(styleable, attr, key, "android.widget.LinearLayout.${value.toUpperCase()}")
else null

@Suppress("UNUSED_PARAMETER")
internal fun renderTag(styleable: Styleable, attr: Attr, key: String, value: String) = if (key == "tag") {
    renderString(styleable, attr, key, value)
} else null

@Suppress("UNUSED_PARAMETER")
internal fun renderVisibility(styleable: Styleable, attr: Attr, key: String, value: String) = if (key == "visibility") {
    JavaAttr(styleable, attr, key, "View.${value.toUpperCase()}")
} else null

@Suppress("UNUSED_PARAMETER")
internal fun renderSingleLine(styleable: Styleable, attr: Attr, key: String, value: String) = if (key == "singleLine") {
    JavaAttr(styleable, attr, key, value)
} else null

@Suppress("UNUSED_PARAMETER")
internal fun renderOrientation(styleable: Styleable, attr: Attr, key: String, value: String) = if (key == "orientation") {
    JavaAttr(styleable, attr, key, "LinearLayout.${value.toUpperCase()}")
} else null

internal fun renderBoolean(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "boolean" && (value == "true" || value == "false"))
    JavaAttr(styleable, attr, key, value)
else null

internal fun renderInteger(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "integer")
    if (value.matches("-?[0-9]+".toRegex()))
        JavaAttr(styleable, attr, key, value)
    else {
        val enumEntry = attr.enum?.firstOrNull { it.name == value }
        if (enumEntry != null) JavaAttr(styleable, attr, key, enumEntry.value)
        else null
    }
else null

internal fun renderFloat(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "float") {
    val enumEntry = attr.enum?.firstOrNull { it.name == value }
    if (enumEntry != null) JavaAttr(styleable, attr, key, enumEntry.value)
    else {
        val v: String = if (value.endsWith("f")) value else {
            "${value}f"
        }
        JavaAttr(styleable, attr, key, v)
    }
} else null

internal fun renderString(styleable: Styleable, attr: Attr, key: String, value: String) =
        if (attr accepts "string") {
            if (value.isReference())
                renderReference(styleable, attr, key, value.parseReference().toString())
            else
                JavaAttr(styleable, attr, key, ("\"" + value.replace("\"", "\\\"") + "\""))
        } else null

internal fun renderColor(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "color" && value.isColor()) {
    JavaAttr(styleable, attr, key, value.parseColor())
} else null

internal fun renderReference(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "reference" && value.isReference()) {
    val reference = try {
        value.parseReference().toString()
    } catch (e: Exception) {
        ignoreSetAttr
    }
    JavaAttr(styleable, attr, key, reference)
} else null

internal fun renderEnum(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "enum" && attr.enum != null) {
    val enumEntry = attr.enum.firstOrNull { it.name == value }
    if (enumEntry != null) JavaAttr(styleable, attr, key, enumEntry.value) else JavaAttr(styleable, attr, key, value)
} else null

internal fun renderFlags(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "flags" && attr.flags != null) {
    val sum = value.split('|').fold(0) { sum, flag ->
        val entry = attr.flags.firstOrNull { it.name == flag }
        sum + (entry?.value?.parseFlagValue() ?: 0)
    }
    JavaAttr(styleable, attr, key, sum.toString())
} else null

internal fun renderDimension(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr accepts "dimension") {
    val dimension = value.parseDimension()
    JavaAttr(styleable, attr, key, dimension)
} else null

internal fun render(styleable: Styleable, attr: Attr, key: String, value: String): JavaAttr {
    val newValue: String = when {
        value.isReference() -> {
            try {
                value.parseReference().toString()
            } catch (e: Exception) {
                ignoreSetAttr
            }
        }
        value.isColor() -> value.parseColor()
        else -> {
            val flagSum = value.split('|').fold(0) { flagSum, flag ->
                val entry = attr.flags?.firstOrNull { it.name == flag }
                flagSum + (entry?.value?.parseFlagValue() ?: 0)
            }
            val enumSum = value.split('|').fold(0) { enumSum, enum ->
                val entry = attr.enum?.firstOrNull { it.name == enum }
                enumSum + (entry?.value?.parseFlagValue() ?: 0)
            }
            when {
                flagSum > 0 -> "$flagSum"
                enumSum > 0 -> "$enumSum"
                else -> "\"$value\""
            }
        }
    }
    return JavaAttr(styleable, attr, key, newValue)
}

internal fun renderEllipsize(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr.name == "ellipsize") {
    val ellipsize = when (value) {
        "none" -> ignoreSetAttr
        "start" -> "TextUtils.TruncateAt.START"
        "middle" -> "TextUtils.TruncateAt.MIDDLE"
        "end" -> "TextUtils.TruncateAt.END"
        "marquee" -> "TextUtils.TruncateAt.MARQUEE"
        else -> ignoreSetAttr
    }
    JavaAttr(styleable, attr, key, ellipsize)
} else null

internal fun renderScaleType(styleable: Styleable, attr: Attr, key: String, value: String) = if (attr.name == "scaleType") {
    val scaleType = when (value) {
        "matrix" -> "ImageView.ScaleType.MATRIX,"
        "fitXY" -> "ImageView.ScaleType.FIT_XY"
        "fitStart" -> "ImageView.ScaleType.FIT_START"
        "fitCenter" -> "ImageView.ScaleType.FIT_CENTER"
        "fitEnd" -> "ImageView.ScaleType.FIT_END"
        "center" -> "ImageView.ScaleType.CENTER"
        "centerCrop" -> "ImageView.ScaleType.CENTER_CROP"
        "centerInside" -> "ImageView.ScaleType.CENTER_INSIDE"
        else -> ignoreSetAttr
    }
    JavaAttr(styleable, attr, key, scaleType)
} else null

private infix fun Attr.accepts(f: String) = f in this.format

private fun Attr.isReference() = this.format.size == 1 && this.format[0] == "reference"