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

    /**
     * Get right-way separator
     */
    public static final String getEscapedSeparator() {
        // need for compatibility with M$ Windows
        String sep = separator;

        if (!sep.contentEquals("/")) {
            sep = "\\\\";
        }
        return sep;
    }

    /**
     * Get current project from anywhere
     * @return Project project
     */
    public static Project getProject() {
        // TODO: make this code right way
        ProjectManager instance = ProjectManager.getInstance();
        Project[] openProjects = instance.getOpenProjects();
        return openProjects[0];
    }

    public static PsiFile getIncludeFile(String path, Project project) {
        String sep = getEscapedSeparator();

        if (path.endsWith("/")) {
            path += "index.php";
        }

        if (path.startsWith("#SITE_DIR#")) {
            path = path.replace("#SITE_DIR#", sep);
        }

        if (!path.startsWith("/")) {
            path = BitrixSiteTemplate.BITRIX_SITE_TEMPLATES_PATH + BitrixSiteTemplate.getInstance(project).getName() + sep + path;
        }
        return getPsiFileByPath(project, project.getBasePath() + path);
    }

    /**
     * Get PSI file by path-string
     *
     * @param project
     * @param defaultTemplatePath
     * @return
     */
    public static PsiFile getPsiFileByPath(Project project, String defaultTemplatePath) {
        try {
            File myFile = new File(defaultTemplatePath);
            if(myFile.exists()) {
                VirtualFile vFile = LocalFileSystem.getInstance().findFileByIoFile(myFile);
                PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);

                return psiFile;
            }
        } catch (NullPointerException npe) {
            return null;
        }

        return null;
    }

    /**
     * Get filename by PSI
     *
     * @param directory
     * @return
     */
    public static String getFileNameByPsiElement(PsiElement directory) {
        String sep = getEscapedSeparator();
        String raw = directory.toString();
        String[] fullPath = raw.split(sep);
        return fullPath[fullPath.length-1];
    }

    /**
     * Get path by PSI
     *
     * @param directory
     * @return
     */
    public static String getPathByPsiElement(PsiElement directory) {
        return directory.toString().replace("PsiElement:", "").replace("PsiDirectory:", "").replace("PsiFile:", "");
    }

    public static boolean isDirectoryExists (String path) {
        try {
            File dir = new File(path);
            return dir.isDirectory();
        } catch (NullPointerException e) {
            return false;
        }
    }
}
