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

import com.intellij.ide.DataManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryImpl;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

import static java.io.File.*;

public class BitrixUtils {
	private static Project project = null;

    public static VirtualFile getContext(Project project) {
        VirtualFile context = null;
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getSelectedFiles();
        if (files != null) {
            if (files[0].toString().contains("bitrix")) {
                context = files[0];
            }
        }
        return context;
    }

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
	    /*DataContext dataContext = DataManager.getInstance().getDataContext();
	    return PlatformDataKeys.PROJECT.getData(dataContext);
	    */
	    if (project == null) {
		    ProjectManager instance = ProjectManager.getInstance();
		    Project[] openProjects = instance.getOpenProjects();
		    project = openProjects[0];
	    }
	    return project;
    }

	public static void setProject(Project prj) {
		project = prj;
	}

    public static PsiFile getIncludeFile(String path) {
	    Project project = getProject();
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
        return getPsiFileByPath(project.getBasePath() + path);
    }

    /**
     * Get PSI file by path-string
     *
     * @param defaultTemplatePath
     * @return
     */
    public static PsiFile getPsiFileByPath(String defaultTemplatePath) {
	    Project project = getProject();
	    try {
            VirtualFile vFile = LocalFileSystem.getInstance().findFileByPath(defaultTemplatePath);
            if (vFile != null) {
                PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);

                return psiFile;
            }
        } catch (NullPointerException npe) {
            return null;
        }

        return null;
    }

    /**
     * Get PSI dir by path-string
     *
     * @param defaultTemplatePath
     * @return
     */
    public static PsiDirectory getPsiDirByPath(String defaultTemplatePath) {
	    Project project = getProject();
        try {
            VirtualFile vFile = LocalFileSystem.getInstance().findFileByPath(defaultTemplatePath);
            if (vFile != null) {
                PsiDirectory psiFile = PsiManager.getInstance(project).findDirectory(vFile);
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
    public static String getFileNameByPsiElement(@NotNull PsiElement directory) {
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
    public static String getPathByPsiElement(@NotNull PsiElement directory) {
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
