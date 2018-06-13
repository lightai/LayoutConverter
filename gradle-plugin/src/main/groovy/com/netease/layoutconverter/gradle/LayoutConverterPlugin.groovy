package com.netease.layoutconverter.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by hzwangpeng2015 on 2018/2/26.
 */
public class LayoutConverterPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.tasks.create('genViewMap', ViewMapTask)
    }
}
