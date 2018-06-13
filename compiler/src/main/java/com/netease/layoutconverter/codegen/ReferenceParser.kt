package com.netease.layoutconverter.codegen

/**
 * 解析Android R类引用资源
 */
open class ReferenceParser(packageName: String, private val type: String, value: String) {
    fun getDrawable(id: String) = "ContextCompat.getDrawable(context, $id)"
    fun getColorStateList(id: String) = "ContextCompat.getColorStateList(context, $id)"
    fun getColor(id: String) = "ContextCompat.getColor(context, $id)"
    fun getDimension(id: String) = "(int) context.getResources().getDimension($id)"
    fun getString(id: String) = "context.getResources().getString($id)"
    fun getInteger(id: String) = "context.getResources().getInteger($id)"

    private val pkg = if (packageName.isEmpty()) "" else "$packageName."
    val ref = "${pkg}R.$type.${value.replace(".", "_")}"
    private val idRef = "${pkg}R.id.${value.replace(".", "_")}"

    override fun toString(): String {
        return when (type) {
            "null" -> "null"
            "drawable" -> getDrawable(ref)
            "mipmap" -> getDrawable(ref)
            "color" -> getColor(ref)
            "dimen" -> getDimension(ref)
            "string" -> getString(ref)
            "style" -> getString(ref)
            "+id" -> idRef
            "integer" -> getInteger(ref)
            else -> ref
        }
    }

    fun toReference() : String =
            when (type) {
                "+id" -> idRef
                "null" -> "0"
                else -> ref
            }
}

class DrawableReferenceParser(private val packageName: String, private val type: String, val value: String)
    : ReferenceParser(packageName, type, value) {
    override fun toString(): String {
        return when (type) {
            "null" -> "null"
            "drawable" -> getDrawable(ref)
            "mipmap" -> getDrawable(ref)
            "color" -> getDrawable(ref)
            else -> throw IllegalStateException("解析drawable异常: ${packageName}R.$type.$value")
        }
    }
}

class ColorStateListParser(packageName: String, private val type: String, val value: String)
    : ReferenceParser(packageName, type, value) {
    override fun toString(): String {
        return when (type) {
            "null" -> "null"
            "color", "drawable" -> getColorStateList(ref)
            else -> throw IllegalStateException("解析ColorStateList异常: $ref")
        }
    }
}

internal fun getColorDrawable(color: String): String {
    return "new ColorDrawable($color)"
}