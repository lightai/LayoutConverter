package com.netease.layoutconverter.codegen

import com.netease.layoutconverter.firstCapital
import com.netease.layoutconverter.firstLowercase

fun List<JavaAttr>.getTheme(): JavaAttr? {
    this.forEach { if (it.name == "theme") return it }
    return null
}

class View(private val viewCls: String,
           private val attrs: List<JavaAttr>,
           private val layoutParams: LayoutParam,
           private val children: List<View>) {
    val viewObj: String

    init {
        val viewCls = if (viewCls.contains("."))
            viewCls.substring(viewCls.lastIndexOf(".") + 1, viewCls.length)
        else
            viewCls

        val number: Int = nameMap.getOrDefault(viewCls, 0)
        nameMap.put(viewCls, number + 1)

        viewObj = "${viewCls.firstLowercase()}$number"
    }

    @Throws(NotSupportException::class)
    override fun toString(): String {
        fun JavaAttr.render(viewObj: String) = if (value.isNotEmpty()) {
            if (value == ignoreSetAttr) {
                "// Ignore $name"
            } else if (styleable == noStyleable) {
                "// Can't find attr: $name:$value"
            } else if (name == "theme") {
                "// No need set theme: $value"
            } else {
                if (attr.code != null && attr.code.isNotEmpty()) {
                    when {
                        attr.code == "set" -> "$viewObj.set${name.capitalize()}($value);"
                        attr.code == "ignore" -> "// Ignore $name"
                        else -> attr.code.replace("\$view", viewObj).replace("\$value", value)
                    }
                } else {
                    "$viewObj.set${name.firstCapital()}($value);"
                }
            }
        } else ""

        if (viewCls == "fragment" || viewCls == "merge") {
            throw NotSupportException("$viewCls not yet support")
        }

        val theme = attrs.getTheme()
        val newView = if (theme == null)
            "final $viewCls $viewObj = obtainView($viewCls.class);\n"
        else
            "final $viewCls $viewObj = obtainView($viewCls.class, ${theme.value});\n"

        val setViewAttrs = if (attrs.isEmpty()) "" else attrs.joinToString("\n", postfix = "\n") { it.render(viewObj) }
        val view = "\n$newView$setViewAttrs"

        val lpObj = "lp${viewObj.capitalize()}"
        val lpCls = layoutParams.name.replace("_", ".") + "Params"
        var lpNew = "$lpCls $lpObj = new $lpCls(${layoutParams.width}, ${layoutParams.height});\n"
        // todo
        if (lpCls == "GridLayout.LayoutParams") lpNew = "$lpCls $lpObj = new $lpCls();\n"
        val lpSetAttrs =
                if (layoutParams.attrs == null || layoutParams.attrs.isEmpty()) ""
                else layoutParams.attrs.joinToString("\n", postfix = "\n") { it.render(lpObj) }
        val lpViewSet = "$viewObj.setLayoutParams($lpObj);\n"
        val lp = "$lpNew$lpSetAttrs$lpViewSet"

        val childrenRendered = children.joinToString("\n") {
            "$it$viewObj.addView(${it.viewObj});"
        }

        val onFinishInflate = "viewOnFinishInflate($viewObj);\n"

        return "$view$lp$childrenRendered"
    }
}

val nameMap: MutableMap<String, Int> = HashMap()
val emptyView = View("", emptyList(), noLayoutParams, emptyList())