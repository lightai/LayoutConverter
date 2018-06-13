package com.netease.layoutconverter.codegen

import com.netease.layoutconverter.parseXml
import java.io.File

/**
 * Created by hzwangpeng2015 on 2017/12/5.
 */
fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Usage: <xml file> <java file>")
        return
    }

    val xmlFile = File(args[0])
    val javaFile = File(args[1])

    if (!xmlFile.exists()) {
        println("$xmlFile does not exist. Aborting")
        return
    }

    val kt = convert(xmlFile.readText())
    javaFile.writeText(kt)
    println(kt)
}

fun convert(xml: String): String {
    val layout = xml.parseXml()
    val root = layout.documentElement
    val view = LayoutConverter.parseView(root, null, mapOf())

    return view.toString()
}