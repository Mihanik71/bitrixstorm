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

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.List;

/**
 * @author Mikhail Medvedev aka r3c130n <mm@salerman.ru>
 * @link http://www.salerman.ru/
 * @date: 20.05.13
 */
public class BitrixComponent {
    private Project project;
    private String namespace;
    private String component;
    private String templateName;
    private BitrixComponentTemplate template;
    private List<BitrixComponentTemplate> arTemplates;
    private List<BitrixComponentParameter> arParams;

    public BitrixComponent(Project project, String namespace, String component, String templateName) {
        this.project = project;
        this.component = component;
        this.namespace = namespace;
        this.templateName = templateName;

        template = new BitrixComponentTemplate(this);
    }

    public String getNamespace () {
        return this.namespace;
    }

    public String getName () {
        return this.component;
    }

    public String getTemplateName () {
        return this.templateName;
    }

    public BitrixComponentTemplate getTemplate () {
        return this.template;
    }

    public Project getProject() {
        return project;
    }

    public static BitrixComponent initComponentFromString (String includeComponentString) {
        Project prj = BitrixUtils.getProject();
        String namespace = getComponentNamespaceFromString(includeComponentString);
        String component = getComponentNameFromString(includeComponentString);
        String template = getComponentTemplateFromString(includeComponentString);
        return new BitrixComponent(prj, namespace, component, template);
    }

    /**
     * Find "component.php" or "class.php"
     *
     * @return
     */
    public PsiElement findComponentSrc() {
        PsiFile cmp;
        String[] order = getComponentSrcPath();

        for (String path : order) {
            cmp = BitrixUtils.getPsiFileByPath(project, path);
            if (cmp != null) {
                return cmp;
            }
        }

        return null;
    }

    public static String getComponentNamespaceFromString (String string) {
        String cleanString = clearIncludeComponentString(string);
        if (cleanString == null) return null;

        String[] pathElements = cleanString.split(":");
        if (pathElements == null) return null;

        return pathElements[0];
    }

    public static String getComponentNameFromString (String string) {
        String cleanString = clearIncludeComponentString(string);
        if (cleanString == null) return null;

        String[] pathElements = cleanString.split(":");
        if (pathElements == null) return null;

        String[] cmptpl = pathElements[1].split(",");
        if (cmptpl == null) return null;
        return cmptpl[0];
    }

    public static String getComponentTemplateFromString (String string) {
        String cleanString = clearIncludeComponentString(string);
        if (cleanString == null) return null;
        if (cleanString.endsWith(",")) return ""; // .default

        String[] pathElements = cleanString.split(":");
        if (pathElements == null) return null;

        String[] cmptpl = pathElements[1].split(",");
        if (cmptpl == null) return null;

        if (cmptpl.length == 2) {
            return cmptpl[1];
        } else {
            return "";
        }
    }

    private static String clearIncludeComponentString (String string) {
        String[] allStrings = string.toLowerCase().split("array");
        String cleanString = allStrings[0].replace("\"", "").replace("'", "").replace("\n","").replace(" ","").replace("\t","");
        return cleanString.substring(0, cleanString.length() - 1);
    }

    private String[] getComponentSrcPath() {
        String[] order = new String[2];
        String sep = BitrixUtils.getEscapedSeparator();
        order[0]    = project.getBasePath()
                + sep + "bitrix"
                + sep + "components"
                + sep + namespace
                + sep + component
                + sep + "component.php";
        order[1]    = project.getBasePath()
                + sep + "bitrix"
                + sep + "components"
                + sep + namespace
                + sep + component
                + sep + "class.php";
        return order;
    }
}
