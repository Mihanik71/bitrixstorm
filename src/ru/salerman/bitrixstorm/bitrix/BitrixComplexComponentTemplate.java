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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Medvedev aka r3c130n <mm@salerman.ru>
 * @link http://www.salerman.ru/
 * @date: 20.05.13
 */
public class BitrixComplexComponentTemplate {   // extends BitrixComponentTemplate
	/*
    public BitrixComplexComponentTemplate(BitrixComponent component) {
        this.psiTemplate = findComponentTemplate(component);
        if (this.psiTemplate == null) {
            return;
        }
        this.path = BitrixUtils.getPathByPsiElement(psiTemplate);
    }

    public static PsiElement findComponentTemplate(BitrixComponent component) {
        PsiElement tpl;
        String[] order = getComponentTemplatesPathOrder(component.getNamespace(), component.getName(), component.getTemplateName(), component.getProject());

        if (order != null) {
            for (String path : order) {
                tpl = BitrixUtils.getPsiDirByPath(component.getProject(), path);
                if (tpl != null) {
                    return tpl;
                }
            }
        }

        return null;
    }
         */
    public static String[] getComponentTemplatesPathOrder(String componentNameSpace, String componentName, String templateName, Project project) {
        String sep = BitrixUtils.getEscapedSeparator();
        String[] order;
        if (templateName == "") {
            templateName = ".default";
        }
        int i = 0;
        VirtualFile context = BitrixUtils.getContext(project);
        if (context != null) {
            order = new String[4];

            String path = context.getPath().replace(context.getName(), "");
            order[i++]  = path
                    + componentNameSpace
                    + sep + componentName
                    + sep + templateName;
        } else {
            order = new String[3];
        }

        order[i++]    = project.getBasePath()
                + BitrixSiteTemplate.BITRIX_SITE_TEMPLATES_PATH + BitrixSiteTemplate.getInstance(project).getName()
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName;

        order[i++]    = project.getBasePath()
                + BitrixSiteTemplate.BITRIX_SITE_TEMPLATES_PATH + ".default"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName;

        order[i++]    = project.getBasePath()
                + sep + "bitrix"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + "templates"
                + sep + templateName;

        return order;
    }

    /*
    public ResolveResult[] getResolveList() {
        List<ResolveResult> results = new ArrayList<ResolveResult>();

        for (PsiElement file : getTemplateFiles()) {
            results.add(new PsiElementResolveResult(file));
        }

        return results.toArray(new ResolveResult[results.size()]);
    }

    public PsiElement[] getTemplateFiles() {
        PsiElement[] children = this.psiTemplate.getChildren();

        return children;
    }
    */
}
