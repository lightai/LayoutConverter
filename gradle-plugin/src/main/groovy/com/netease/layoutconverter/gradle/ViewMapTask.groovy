package com.netease.layoutconverter.gradle

import com.netease.layoutconverter.ToolsKt
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 生成Android View的继承层级xml文档
 *
 * Created by hzwangpeng2015 on 2018/2/26.
 */
public class ViewMapTask extends DefaultTask {
    String inPath, outPath

    @TaskAction
    def genXML() {
        ToolsKt.scanAndGenViewHierarchy([inPath, outPath])
    }

    def setInPath(String path) {
        inPath = path
    }

    def setOutPath(String path) {
        outPath = path
    }
}
