package com.netease.layoutconverter.codegen

import com.netease.layoutconverter.*
import com.netease.layoutconverter.ResourceParser.styles
import kotlinx.dom.childElements
import org.w3c.dom.Element

object LayoutConverter {
    private val TAG_MERGE = "merge"
    private val TAG_INCLUDE = "include"
    val TAG_1995 = "blink"
    val TAG_REQUEST_FOCUS = "requestFocus"
    val TAG_TAG = "tag"

    /**
     * Android databinding tag
     */
    val TAG_LAYOUT = "layout"

    private fun convertToView(xml: String, xmls: Map<String, String>): View {
        val layout = xml.parseXml()
        val root = layout.documentElement
        return parseView(root, null, xmls)
    }

    /**
     * @param pkg 程序包名
     * @param layout 布局文件名字
     * @param xml 布局文件内容
     */
    @Throws(NotSupportException::class)
    fun genCode(pkg: String, layout: String, xml: String, xmls: Map<String, String>): String {
        nameMap.clear()

        val imports = listOf(
                "import android.content.Context;",
                "import android.view.*;",
                "import android.widget.*;",
                "import android.webkit.*;",
                "import android.text.*;",
                "import android.graphics.drawable.*;",
                "import android.content.res.*;",
                "import android.animation.*;",
                "import $pkg.R;",
                "import android.support.v4.content.ContextCompat;",
                "import android.util.*;",

                "import com.netease.layoutconverter.BaseLayout;",
                "import com.netease.layoutconverter.compat.*;",

                // todo: remove
                "import android.support.design.widget.*;"
        )

        val view = convertToView(xml, xmls)
        return "package $pkg;\n\n" +

                imports.joinToString("\n", postfix = "\n") +

                "\npublic class $layout extends BaseLayout {\n" +

                "    public static $layout from(Context context) {\n" +
                "        return new $layout(context);\n" +
                "    }\n\n" +

                "    private $layout(Context context) {\n" +
                "        super(context);\n" +
                "    }\n\n" +

                "    @Override public View inflate(ViewGroup root, boolean attachToRoot) {" +
                "$view\n\n".indent(2) +

                "        onFinishInflate();\n\n" +

                "        if (attachToRoot) {\n" +
                "            root.addView(${view.viewObj});\n" +
                "        }\n\n" +

                "        return ${view.viewObj};\n" +
                "    }\n" +
                "}"
    }

    internal fun parseView(view: Element, parentName: String?, xmls: Map<String, String>): View {
        val validView: Element = view.firstValid(xmls) ?: return emptyView
        validView.expandStyle()

        val name = validView.tagName

        val (attributes, layoutParams) = validView.getViewAttrs(name!!, parentName)
        val children = validView.childElements().map { parseView(it, name, xmls) }

        return View(validView.tagName, attributes, layoutParams, children)
    }

//    private fun Element.firstValid(xmls: Map<String, String>): Element? {
//        var element: Element = this
//
//        while (true) {
//            while (element.tagName == TAG_MERGE || element.tagName == TAG_LAYOUT) {
//                val first = element.firstChildElement()
//                element = first ?: return null
//            }
//
//            val name = element.tagName
//            if (name == TAG_INCLUDE) {
//                element = element.includeLayout(xmls) ?: return null
//            } else {
//                return element
//            }
//        }
//    }

    private fun Element.firstValid(xmls: Map<String, String>): Element? {
        var element: Element = this

        while (true) {
            element = when {
                element.tagName == TAG_LAYOUT -> element.childElements().firstOrNull { it.tagName != "data" } ?: return null
                element.tagName == TAG_INCLUDE -> element.includeLayout(xmls) ?: return null
                else -> return element
            }
        }
    }

    /**
     * 展开style=@style/...属性
     */
    private fun Element.expandStyle() {
        val styleRef = this.getAttribute("style")
        if (styleRef.startsWith("@style/")) {
            val value = styleRef.substring("@style/".length)
            styles[value]?.items?.forEach {
                this.setAttribute(it.name, it.value)
            }
        }
    }

    private fun Element.includeLayout(xmls: Map<String, String>): Element? {
        val (attrs, layoutParams) = getViewAttrs("Include", null)
        attrs.forEach {
            if (it.name == "layout") {
                val element = xmls["${it.value.substring(it.value.lastIndexOf("/") + 1)}.xml"]?.parseXml()?.documentElement

                // 设置<include/>的属性
                if (element != null) {
                    attrs.forEach {
                        when (it.name) {
                            "id" -> element.setAttribute("android:id", this.getAttribute("android:id"))
                            "visibility" -> element.setAttribute("android:visibility", this.getAttribute("android:visibility"))
                        }
                    }
                    if (this.getAttribute("layout_width").isNotEmpty() && this.getAttribute("layout_height").isNotEmpty()) {
                        layoutParams.attrs?.forEach { element.setAttribute(it.name, it.value) }
                    }

                    return element
                }
            }
        }
        return null
    }

    private fun Element.getViewAttrs(name: String, parentName: String?): Pair<List<JavaAttr>, LayoutParam> {
        val simpleName = name.subStringFromLastDot()
        val simpleParentName = parentName?.subStringFromLastDot()

        val original = arrayListOf<KeyValuePair>()
        (0..(attributes.length - 1))
                .map { attributes.item(it) }
                .mapTo(original) { it.nodeName * it.nodeValue }

        val (layoutAttributes, ordinaryAttributes) = original.partition { it.key.startsWith("android:layout_") }

        val layoutParams =
                if (simpleParentName != null)
                    transformLayoutAttributes(layoutAttributes, simpleParentName)
                else
                    transformLayoutAttributes(layoutAttributes, defaultRootLayoutParams)

        val list =  ordinaryAttributes.mapNotNull { transformAttribute(simpleName, it.key, it.value) }

        // 处理重复定义的属性
        var index = 0
        list.forEach {
            try {
                var i = index
                while (i < list.size) {
                    if (list[i].name == it.name) {
                        list[i].repeat = true
                        break
                    }
                    i++
                }
            } finally {
                index++
            }
        }

        return list to layoutParams
    }
}