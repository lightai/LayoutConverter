package com.netease.layoutconverter

import kotlinx.dom.writeXmlString
import org.w3c.dom.Document
import java.io.File
import java.nio.charset.Charset
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by hzwangpeng2015 on 2017/12/19.
 */
val pattern = Regex("class\\s+([A-Za-z0-9_]+)\\s+extends\\s+([A-Za-z0-9_.]+)")

/**
 * 扫描源码，创建view之间的继承关系
 */
fun scanAndGenViewHierarchy(args: List<String>) {
    if (args.size != 2) {
        System.out.print("usage: inPath outPath")
        return
    }

    ResourceParser.parse()

    val rootDir = File(args[0])

    val classMap = TreeMap<String, String>()
    expandDir(rootDir, classMap)
    val viewMap = classMap.filter {
        val parent = parent(it.key, classMap)
        parent == "View" || ResourceParser.viewHierarchy.containsKey(parent)
    }

    createXml(viewMap)?.writeXmlString(File(args[1]).writer(Charset.forName("utf-8")), true)
}

fun parent(key: String, map: Map<String, String>): String {
    var parent = map[key]
    while (true) {
        val k = parent
        parent = map[k]
        if (parent == null) return k!!
    }
}

fun createXml(map: Map<String, String>): Document? {
    val doc = DocumentBuilderFactory
            .newInstance().newDocumentBuilder().newDocument()

    val root = doc.createElement("class")
    doc.appendChild(root)

    map.forEach {
        val e = doc.createElement(it.key)
        e.setAttribute("parent", it.value)
        root.appendChild(e)
    }

    return doc
}

fun expandDir(dir: File, map: TreeMap<String, String>) {
    if (dir.isDirectory) {
//        System.out.print("\n\n${dir.name}\n\n")
        dir.listFiles().forEach { expandDir(it, map) }
    } else {
        val java = dir.readLines().joinToString("\n") {
            val v = it.trim()
            if (v.startsWith("*") || v.startsWith("/") || v.endsWith("*/") || v.startsWith("import ")) "" else it
        }
        val matcher = pattern.find(java)
        if (matcher != null) {
            val thisClass = matcher.groups[1]?.value
            val superClass = matcher.groups[2]?.value
            if (!thisClass.isNullOrEmpty() && !superClass.isNullOrEmpty()) {
                map.put(thisClass!!, superClass!!)
//                System.out.print("${dir.path}:$thisClass=$superClass\n:")
            }
        }
    }
}