package com.netease.layoutconverter;

import com.netease.layoutconverter.codegen.Style
import kotlinx.dom.childElements
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test;

/**
 * Created by hzwangpeng2015 on 2017/12/13.
 */
class ParseStyleTest {
    @Test fun testStyle() {
        val path = "value/styles.xml"
        val doc = readResource(path).parseXml().documentElement

        val list = ArrayList<Style>()
        doc.childElements("style").forEach {
            val style = ResourceParser.getStyle(it)
            list.add(style)
        }

        assertTrue(list.size > 10)
        assertEquals("@color/color_default_background", list[0].items[0].value)
    }
}
