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

import java.util.Hashtable;

import static java.io.File.separator;

/**
 * @author Mikhail Medvedev aka r3c130n <mm@salerman.ru>
 * @link http://www.salerman.ru/
 * @date: 20.05.13
 */
public class BitrixComponentTemplate {
    public String templateName;
	private Hashtable<String, BitrixEntity> templateEntities = new Hashtable<String, BitrixEntity>();

	protected PsiElement psiTemplateFile = null;
	protected PsiElement psiTemplateDirectory = null;
	private VirtualFile templateDirectory;

	public BitrixComponentTemplate (String templateName, VirtualFile templateDirectory) {
		String sep = BitrixSiteTemplate.sep;
		this.templateDirectory = templateDirectory;
		this.templateName = templateName;
		String templatePath = templateDirectory.getPath();
		this.psiTemplateFile = BitrixUtils.getPsiFileByPath (templatePath + sep + "template.php");
		this.psiTemplateDirectory = BitrixUtils.getPsiDirByPath(templatePath);

		this.templateEntities.put("template", new BitrixEntity(templatePath + sep + "template.php"));
		this.templateEntities.put("parameters", new BitrixEntity(templatePath + sep + ".parameters.php"));
		this.templateEntities.put("result_modifier", new BitrixEntity(templatePath + sep + "result_modifier.php"));
		this.templateEntities.put("component_epilog", new BitrixEntity(templatePath + sep + "component_epilog.php"));
		this.templateEntities.put("style", new BitrixEntity(templatePath + sep + "style.css"));
		this.templateEntities.put("script", new BitrixEntity(templatePath + sep + "script.js"));
	}


	public void setCodeTpl(String type, BitrixEntityCodeTemplate codeTpl) {
		if (this.templateEntities.containsKey(type)) {
			this.templateEntities.get(type).setTemplate(codeTpl);
		}
	}

	public void Create (String type) {
		if (this.templateEntities.containsKey(type)) {
			this.templateEntities.get(type).Create();
		}
	}


    public PsiElement toPsiFile () {
        return this.psiTemplateFile;
    }

	public PsiElement toPsiDirectory () {
		return this.psiTemplateDirectory;
	}

    public static PsiElement findComponentTemplate(BitrixComponent component) {
	    Project project = BitrixUtils.getProject();
        PsiElement tpl;
        String[] order = getComponentTemplatesPathOrder(component.getNamespace(), component.getName(), ".default");

        if (order != null) {
            for (String path : order) {
                tpl = BitrixUtils.getPsiFileByPath(path);
                if (tpl != null) {
                    return tpl;
                }
            }
        }

        return null;
    }

    public static String[] getComponentTemplatesPathOrder(String componentNameSpace, String componentName, String templateName) {
	    Project project = BitrixUtils.getProject();
        String sep = BitrixUtils.getEscapedSeparator();
        String[] order;
        if (templateName == "") {
            templateName = ".default";
        }
        int i = 0;
        VirtualFile context = BitrixUtils.getContext(project);
        if (context != null) {
            order = new String[7];

            String path = context.getPath().replace(context.getName(), "");
            order[i++]  = path
                        + componentNameSpace
                        + sep + componentName
                        + sep + templateName
                        + sep + "template.php";
        } else {
            order = new String[6];
        }

        order[i++]    = project.getBasePath()
                + BitrixSiteTemplate.getInstance(project).BITRIX_SITE_TEMPLATES_PATH
                + BitrixSiteTemplate.getInstance(project).getName()
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        order[i++]    = project.getBasePath()
                + sep + "local"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + "templates"
                + sep + templateName
                + sep + "template.php";

        order[i++]    = project.getBasePath()
                + BitrixSiteTemplate.getInstance(project).BITRIX_SITE_TEMPLATES_PATH
                + ".default"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        order[i++]    = BitrixSiteTemplate.getInstance(project).BITRIX_ROOT
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + "templates"
                + sep + templateName
                + sep + "template.php";

        order[i++]    = project.getBasePath()
                + sep + "local"
                + sep + "templates"
                + sep +  ".default"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        order[i++]    = project.getBasePath()
                + sep + "local"
                + sep + "templates"
                + sep + BitrixSiteTemplate.getInstance(project).getName()
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        return order;
    }
}
