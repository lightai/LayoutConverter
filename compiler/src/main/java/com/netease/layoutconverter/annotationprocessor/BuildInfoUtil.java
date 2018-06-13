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

import com.netease.layoutconverter.Layouts;

import java.lang.annotation.Annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

/**
 * Created by hzwangpeng2015 on 2017/11/17.
 */
class BuildInfoUtil {
    static Layouts load(RoundEnvironment roundEnvironment) {
        return extractNotNull(roundEnvironment, Layouts.class);
    }

    private static <T extends Annotation> T extractNotNull(RoundEnvironment roundEnv,
            Class<T> annotationClass) {
        T result = null;
        for (Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            final T info = element.getAnnotation(annotationClass);
            if (info == null) {
                continue;
            }
            result = info;
        }
        return result;
    }
}
