package com.netease.layoutconverter

import com.netease.layoutconverter.ResourceParser.layoutParamsHierarchy
import com.netease.layoutconverter.ResourceParser.viewHierarchy
import com.netease.layoutconverter.codegen.*
import com.netease.layoutconverter.tools.util.L
import kotlinx.dom.childElements
import org.w3c.dom.Element
import java.io.File

object ResourceParser {
    private val attrsPaths = arrayOf(
            "frameworks/base/core/attrs.xml",
            "frameworks/support/design/attrs.xml"
    )
    val appAttrsPaths = ArrayList<String>()

    private val viewsPaths = arrayOf(
            "frameworks/base/core/views.xml",
            "frameworks/support/views.xml"
    )
    val appViewsPaths = ArrayList<String>()

    private val layoutParams = arrayOf(
            "frameworks/base/core/layout_params.xml",
            "frameworks/support/design/layout_params.xml"
    )
    private val appLayoutParamsPaths = ArrayList<String>()

    val appStylesPaths = ArrayList<String>()

    internal lateinit var attrs: Attrs
    internal lateinit var viewHierarchy: Map<String, String>
    internal lateinit var layoutParamsHierarchy: Map<String, String>
    internal lateinit var styles : Map<String, Style>

    internal fun getStyle(element: Element): Style {
        val name = element.getAttribute("name")
        val parent = element.getAttribute("parent")

        // flag
        val itemList = ArrayList<NameValue>()
        element.childElements("item").forEach {
            val v = NameValue(it.getAttribute("name"), it.textContent?:"")
            itemList.add(v)
        }

        return Style(name, parent, itemList)
    }

    private fun parseStyles(): Map<String, Style> {
        val map = HashMap<String, Style>()

        appStylesPaths.forEach {
            val doc = readFile(it).parseXml().documentElement

            doc.childElements("style").forEach {
                val style = getStyle(it)
                map.put(style.name, style)
            }
        }

        return map
    }

    private fun getAttr(it: Element): Attr {
        val name = it.getAttribute("name")
        val format = if (it.getAttribute("format").isEmpty()) emptyList() else it.getAttribute("format").split("|")
        val valueClass = it.getAttribute("valueClass")
        val code = it.getAttribute("code")

        // flag
        val flagList = ArrayList<NameValue>()
        it.childElements("flag").forEach {
            flagList.add(NameValue(it.getAttribute("name"), it.getAttribute("value")))
        }

        // enum
        val enumList = ArrayList<NameValue>()
        it.childElements("enum").forEach {
            enumList.add(NameValue(it.getAttribute("name"), it.getAttribute("value")))
        }

        return Attr(name, format, flagList, enumList, valueClass, code)
    }

    private fun parseAttrs(): Attrs {
        val styleables = HashMap<String, Styleable>()
        val free = ArrayList<Attr>()

        L.d("[compiler] attrs paths: ${appAttrsPaths + attrsPaths}")

        appAttrsPaths.forEach {
            val doc = readFile(it).parseXml().documentElement

            doc.childElements("declare-styleable").forEach {
                val attrList = ArrayList<Attr>()
                it.childElements("attr").forEach {
                    attrList.add(getAttr(it))
                }

                val styleableName = it.getAttribute("name")
                val styleable = Styleable(styleableName, attrList)
                styleables.put(styleableName, styleable)
            }

            doc.childElements("attr").map {
                free.add(getAttr(it))
            }
        }

        attrsPaths.forEach {
            val doc = readResource(it).parseXml().documentElement

            doc.childElements("declare-styleable").forEach {
                val attrList = ArrayList<Attr>()
                it.childElements("attr").forEach {
                    attrList.add(getAttr(it))
                }

                val styleableName = it.getAttribute("name")
                styleables.put(styleableName, Styleable(styleableName, attrList))
            }

            doc.childElements("attr").map {
                free.add(getAttr(it))
            }
        }

        return Attrs(free, styleables = styleables)
    }

    private fun parseViewHierarchy(): Map<String, String> {
        val views = HashMap<String, String>()

        appViewsPaths.forEach {
            readFile(it).parseXml().documentElement.childElements().map {
                views.put(it.tagName, it.getAttribute("parent"))
            }
        }

        viewsPaths.forEach {
            readResource(it).parseXml().documentElement.childElements().map {
                views.put(it.tagName, it.getAttribute("parent"))
            }
        }

        return views
    }

    private fun parseLayoutParamsHierarchy(): Map<String, String> {
        val map = HashMap<String, String>()

        appLayoutParamsPaths + layoutParams.forEach {
            readResource(it).parseXml().documentElement.childElements().map {
                map.put(it.tagName, it.getAttribute("parent"))
            }
        }
        return map
    }

    private fun readFile(filename: String): String {
        L.d("readFile $filename")
        return File(filename).readText()
    }

    fun parse() {
        attrs = parseAttrs()
        viewHierarchy = parseViewHierarchy()
        layoutParamsHierarchy = parseLayoutParamsHierarchy()
        styles = parseStyles()
    }
}

fun readResource(filename: String): String {
    L.d("readResource url:%s", Attrs::class.java.classLoader.getResource(filename).toString())
    ReferenceParser::class.java.classLoader.getResourceAsStream(filename).use {
        return it.reader().readText()
    }
}

/**
 * 获取LayoutParams的继承层次
 * @param layoutParamsCls 布局的类名字
 */
internal fun getLayoutParamsHierarchy(layoutParamsCls: String): List<String> {
    val default = "ViewGroup_Layout"
    val array = ArrayList<String>()

    var key: String? = layoutParamsCls.layoutParamsTag()

    // 找到第一个有效的父布局
    if (!layoutParamsHierarchy.containsKey(key)) {
        val validClass = getViewHierarchy(layoutParamsCls).firstOrNull {
            layoutParamsHierarchy.containsKey(it.layoutParamsTag())
        }
        if (validClass != null) {
            key = validClass.layoutParamsTag();
        }
    }

    if (!layoutParamsHierarchy.containsKey(key)) {
        array.add(default)
    } else {
        while (key != null && key.isNotEmpty()) {
            array.add(key)
            key = layoutParamsHierarchy[key]
        }
    }

    return array
}

/**
 * 获取@param view对应的父类
 */
fun getViewHierarchy(viewName: String): List<String> {
    val default = "View"
    val array = ArrayList<String>()

    if (!viewHierarchy.containsKey(viewName)) {
        array.add(viewName)
        array.add(default)
    } else {
        var key: String? = viewName
        while (key != null && key.isNotEmpty()) {
            array.add(key)
            key = viewHierarchy[key]
        }
    }

    return array
}