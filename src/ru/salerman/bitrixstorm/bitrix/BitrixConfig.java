package ru.salerman.bitrixstorm.bitrix;/*
 * Copyright 2011-2013 Salerman <www.salerman.ru>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;

import java.util.Hashtable;

/**
 * @author Mikhail Medvedev aka r3c130n <mm@salerman.ru>
 * @link http://www.salerman.ru/
 * @date: 21.05.13
 */
public class BitrixConfig {
    @NonNls
    //private static Project project;

    public static final String BITRIX_SITE_TEMPLATE = "BitrixStorm.Site.Template";
    public static final String BITRIX_PATH = "BitrixStorm.Bitrix.Path";

    //public static PropertiesComponent getInstance (Project project) {
    //    return PropertiesComponent.getInstance(project);
    //}
}
