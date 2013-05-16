/*
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

/**
 * @author Mikhail Medvedev aka r3c130n <mm@salerman.ru>
 * @link http://www.salerman.ru/
 * @date: 23.04.2013
 */

package ru.salerman.bitrixstorm.bitrix;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NonNls;

import java.io.File;

import static java.io.File.*;

public class BitrixUtils {

    public static String componentName;
    public static String componentNameSpace = "bitrix";
    public static String currentTemplate = ".default";
    public static String siteTemplateName = ".default";
    public static PropertiesComponent BitrixSettings;
    @NonNls
    private static final String BITRIX_SITE_TEMPLATE = "BitrixStorm.Site.Template";
    private static final String bitrixTemplatesPath = separator + "bitrix" + separator + "templates" + separator;
    private static String bitrixTemplatesPathEsc;

    private static final String getSeparator() {
        // need for compatibility with M$ Windows
        String sep = separator;

        if (!sep.contentEquals("/")) {
            sep = "\\\\";
        }

        bitrixTemplatesPathEsc = sep + "bitrix" + sep + "templates" + sep;
        return sep;
    }

    public static String getSiteTemplateName() {

        Project project = getProject();

        BitrixSettings = PropertiesComponent.getInstance(project);
        siteTemplateName = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");
        return siteTemplateName;
    }

    public static void setSiteTemplateName(String templateName) {

        // TODO: make this code right way
        Project project = getProject();

        BitrixSettings = PropertiesComponent.getInstance(project);

        BitrixSettings.setValue(BITRIX_SITE_TEMPLATE, templateName);

    }

    public static Project getProject() {
        ProjectManager instance = ProjectManager.getInstance();
        Project[] openProjects = instance.getOpenProjects();

        return openProjects[0];
    }

    public static String recognizeComponentTemplate() {
        return currentTemplate;
    }

    public static PsiFile getIncludeFile(String path, Project project) {
        String sep = getSeparator();

        if (path.endsWith("/")) {
            path += "index.php";
        }

        if (path.startsWith("#SITE_DIR#")) {
            path = path.replace("#SITE_DIR#", sep);
        }

        if (!path.startsWith(sep)) {
            path = bitrixTemplatesPath + getSiteTemplateName() + sep + path;
        }
        return getPsiFileByPath(project, project.getBasePath() + path);
    }

    public static PsiFile getSiteTemplateHeader(Project project) {
        String sep = getSeparator();
        String path = project.getBasePath() + bitrixTemplatesPath + getSiteTemplateName() + sep + "header.php";
        return getPsiFileByPath(project, path);
    }

    public static PsiFile getSiteTemplateFooter(Project project) {
        String sep = getSeparator();
        String path = project.getBasePath() + bitrixTemplatesPath + getSiteTemplateName() + sep + "footer.php";
        return getPsiFileByPath(project, path);
    }

    public static PsiFile findComponentTemplate(String componentNameSpace, String componentName, String templateName, Project project) {
        PsiFile tpl;
        String[] order = getComponentTemplatesPathOrder(componentNameSpace, componentName, templateName, project);

        for (String path : order) {
            tpl = getPsiFileByPath(project, path);
            if (tpl != null) {
                return tpl;
            }
        }

        return null;
    }

    public static Boolean isSiteTemplate (PsiElement path) {
        String sep = getSeparator();
        String pathToTpl = path.toString();
        if (pathToTpl.contains(bitrixTemplatesPath)) {
            String[] split = pathToTpl.split(bitrixTemplatesPathEsc);
            if (!split[1].contains(sep)) {
                return true;
            }
        }
        return false;
    }

    public static String getSiteTemplate (PsiElement path) {
        String sep = getSeparator();
        String pathToTpl = path.toString();
        if (pathToTpl.contains(bitrixTemplatesPath)) {
            String[] split = pathToTpl.split(bitrixTemplatesPathEsc);
            if (!split[1].contains(sep)) {
                return split[1];
            }
        }
        return null;
    }

    private static String[] getComponentTemplatesPathOrder(String componentNameSpace, String componentName, String templateName, Project project) {
        String sep = getSeparator();
        String[] order = new String[3];

        if (templateName == "") {
            templateName = ".default";
        }

        // TODO: add complex component support
        order[0]    = project.getBasePath()
                + bitrixTemplatesPath + getSiteTemplateName()
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        order[1]    = project.getBasePath()
                + bitrixTemplatesPath + ".default"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        order[2]    = project.getBasePath()
                + sep + "bitrix"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + "templates"
                + sep + templateName
                + sep + "template.php";

        return order;
    }

    private static PsiFile getPsiFileByPath(Project project, String defaultTemplatePath) {
        File myFile = new File(defaultTemplatePath);
        if(myFile.exists()) {
            VirtualFile vFile = LocalFileSystem.getInstance().findFileByIoFile(myFile);
            PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);

            return psiFile;
        }

        return null;
    }

    public static String getTplByPsiElement(PsiElement directory) {
        String sep = getSeparator();
        String raw = directory.toString();
        String[] fullPath = raw.split(sep);
        return fullPath[fullPath.length-1];
    }

    public static String[] findComponentTemplatesList() {
        Project project = getProject();

        try {
            VirtualFile baseDir = project.getBaseDir().findChild("bitrix").findChild("templates");
            PsiDirectory directory = PsiManager.getInstance(project).findDirectory(baseDir);
            PsiElement[] children = directory.getChildren();

            String[] templates = new String[children.length];

            for (int i = 0; i < children.length; i++) {
                templates[i] = getTplByPsiElement(children[i]);
            }

            return templates;
        } catch (NullPointerException ex) {

        }
        return null;
    }

    public static PsiElement findComponentSrc(String nameSpace, String component, Project project) {
        PsiFile cmp;
        String[] order = getComponentSrcPath(nameSpace, component, project);

        for (String path : order) {
            cmp = getPsiFileByPath(project, path);
            if (cmp != null) {
                return cmp;
            }
        }

        return null;
    }

    private static String[] getComponentSrcPath(String componentNameSpace, String componentName, Project project) {
        String[] order = new String[2];
        String sep = getSeparator();
        order[0]    = project.getBasePath()
                + sep + "bitrix"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + "component.php";
        order[1]    = project.getBasePath()
                + sep + "bitrix"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + "class.php";
        return order;
    }
}
