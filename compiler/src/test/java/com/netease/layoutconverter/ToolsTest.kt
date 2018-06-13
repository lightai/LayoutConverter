package com.netease.layoutconverter

import org.junit.Test

/**
 * Created by hzwangpeng2015 on 2017/12/19.
 */

class ToolsTest {
    @Test fun genViewXml() {
        val inPath = "D:\\android\\aosp\\frameworks\\support"
        val outPath = "D:\\android\\github\\android-layout-converter\\compiler\\src\\test\\resources\\out\\support_views.xml"
        val list = ArrayList<String>()
        list.add(inPath)
        list.add(outPath)
        scanAndGenViewHierarchy(list)
    }
}
