/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netease.layoutconverter.annotationprocessor;


import com.google.auto.service.AutoService;
import com.google.googlejavaformat.java.Formatter;
import com.netease.layoutconverter.Layouts;
import com.netease.layoutconverter.ResourceParser;
import com.netease.layoutconverter.codegen.LayoutConverter;
import com.netease.layoutconverter.codegen.NotSupportException;
import com.netease.layoutconverter.tools.util.L;
import com.netease.layoutconverter.tools.util.StringUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * apt：把android layout xml布局文件转换为java代码
 * Created by hzwangpeng2015 on 2017/11/17.
 */
@SupportedAnnotationTypes({"com.netease.layoutconverter.Layouts"})
@AutoService(Processor.class)
public class ProcessExpressions extends AbstractProcessor {
    private static final String OPTION_ATTRS_FILE_PATHS = "attrsFilePaths";
    private static final String OPTION_VIEWS_FILE_PATHS = "viewsFilePaths";
    private static final String OPTION_STYLES_FILE_PATHS = "stylesFilePaths";
    private static final String FILE_PATH_SEPARATOR = ";";
    private static final String OPTION_EXCLUDE_REGEX = "excludeLayoutRegex";
    private static final String OPTION_LAYOUT_DIRS = "layoutDirs";

    private String excludeRegex;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        L.setClient(new L.Client() {
            @Override
            public void printMessage(Diagnostic.Kind kind, String message, Element element) {
                Messager messager = processingEnv.getMessager();
                messager.printMessage(kind, message, element);
            }
        });
        L.setDebugLog(true);

        final Layouts layouts = BuildInfoUtil.load(roundEnv);
        if (layouts == null) {
            L.i("[compiler] layouts == null");
            return false;
        }

        String attrsFilePaths = processingEnv.getOptions().get(OPTION_ATTRS_FILE_PATHS);
        if (attrsFilePaths != null) {
            String[] attrsPathsArray = attrsFilePaths.split(FILE_PATH_SEPARATOR);
            L.d("[compiler] attrsPathsArray:" + Arrays.toString(attrsPathsArray));
            for (String path : attrsPathsArray) {
                ResourceParser.INSTANCE.getAppAttrsPaths().add(path.trim());
            }
        }

        String viewsFilePaths = processingEnv.getOptions().get(OPTION_VIEWS_FILE_PATHS);
        if (viewsFilePaths != null) {
            String[] viewsPathsArray = viewsFilePaths.split(FILE_PATH_SEPARATOR);
            L.d("[compiler] viewsPathsArray:" + Arrays.toString(viewsPathsArray));
            for (String path : viewsPathsArray) {
                ResourceParser.INSTANCE.getAppViewsPaths().add(path.trim());
            }
        }

        String stylesFilePaths = processingEnv.getOptions().get(OPTION_STYLES_FILE_PATHS);
        if (stylesFilePaths != null) {
            String[] stylesPathsArray = stylesFilePaths.split(FILE_PATH_SEPARATOR);
            L.d("[compiler] stylesPathsArray:" + Arrays.toString(stylesPathsArray));
            for (String path : stylesPathsArray) {
                ResourceParser.INSTANCE.getAppStylesPaths().add(path.trim());
            }
        }

        ResourceParser.INSTANCE.parse();

        excludeRegex = processingEnv.getOptions().get(OPTION_EXCLUDE_REGEX);

        new Intermediate(layouts).genCode();
        return true;
    }

    class Intermediate {
        final Layouts layouts;

        // name to xml content map
        final Map<String, String> mLayoutInfoMap = new HashMap<String, String>();

        Intermediate(Layouts layouts) {
            this.layouts = layouts;

            final String layoutDIrs[] = processingEnv.getOptions().get(OPTION_LAYOUT_DIRS).split(FILE_PATH_SEPARATOR);
            for (String layoutInfoFolderPath : layoutDIrs) {

                L.d("[compiler] layoutInfoFolderPath: %s", layoutInfoFolderPath);

                final File layoutFolder = new File(layoutInfoFolderPath);
                if (layoutFolder.isDirectory()) {
                    final File[] layoutFiles = layoutFolder.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".xml");
                        }
                    });

                    if (layoutFiles != null) {
                        for (File layoutFile : layoutFiles) {
                            try {
                                final String fileName = layoutFile.getName();
                                final String content = FileUtils.readFileToString(layoutFile, "utf-8");
                                mLayoutInfoMap.put(fileName, content);
                            } catch (IOException e) {
                                L.e(e, "cannot load layout file information. Try a clean build");
                            }
                        }
                    }
                } else {
                    L.d("[compiler] layout info folder does not exist, skipping for %s", layoutInfoFolderPath);
                }
            }
        }

        void genCode() {
            final Set<String> layoutsSet = new HashSet<>(layouts.layouts().length);
            layoutsSet.addAll(Arrays.asList(layouts.layouts()));

            final Formatter formatter = new Formatter();
            for (Map.Entry<String, String> entry : mLayoutInfoMap.entrySet()) {
                final String layoutFileName = entry.getKey();

                if (excludeRegex != null && Pattern.matches(excludeRegex, layoutFileName)) {
                    L.d("[compiler] genCode Ignore layoutFileName:%s", layoutFileName);
                    continue;
                }

                if (!layouts.all() && !layoutsSet.contains(layoutFileName)) {
                    continue;
                }

                L.d("[compiler] genCode layoutFileName:%s", layoutFileName);

                final String clsName = clsName(layoutFileName) + "Layout";
                String javaCode = null;
                try {
                    javaCode = LayoutConverter.INSTANCE.genCode(
                            layouts.modulePackage(), clsName, entry.getValue(), mLayoutInfoMap);
                    javaCode = formatter.formatSource(javaCode);

                    try {
                        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                                layouts.modulePackage() + "." + clsName);
                        BufferedWriter writer = new BufferedWriter(sourceFile.openWriter());
                        IOUtils.write(javaCode, writer);
                        writer.close();
                    } catch (Throwable e) {
                        // todo
                        throw e;
                    }
                } catch (NotSupportException e) {
                    // todo
                } catch (Throwable e) {
                    L.e(e, "[compiler] genCode\n" + javaCode);
                }
            }
        }
    }

    private String clsName(String xmlFileName) {
        final String[] names = xmlFileName.substring(0, xmlFileName.indexOf(".")).split("_");
        final StringBuilder sb = new StringBuilder();
        for (String s : names) {
            sb.append(StringUtils.capitalize(s));
        }
        return sb.toString();
    }

}
