package ru.salerman.bitrixstorm.bitrix;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Hashtable;

/**
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
public class BitrixComponentTemplatesManager {
	private String[] pathToTemplates;
	private Hashtable<String, BitrixComponentTemplate> templatesList;

	BitrixComponentTemplatesManager (String namespace, String componentName) {
		Project project = BitrixUtils.getProject();
		String sep = BitrixUtils.getEscapedSeparator();
		String rootPath = BitrixSiteTemplate.getInstance(project).BITRIX_ROOT;

		this.pathToTemplates = new String[3];

		this.pathToTemplates[0] = rootPath + sep + "components" + sep + namespace + sep + componentName + sep +"templates";
		this.pathToTemplates[1] = rootPath + sep + "templates" + sep + ".default" + sep + "components" + sep + namespace + sep + componentName;
		this.pathToTemplates[2] = rootPath + sep + "templates" + sep + BitrixSiteTemplate.getInstance(project).getName() + sep + "components" + sep + namespace + sep + componentName;
	}

	public Hashtable<String, BitrixComponentTemplate> getTemplates () {
		this.templatesList = new Hashtable<String, BitrixComponentTemplate>();
		for (String path : this.pathToTemplates) {
			try {
				VirtualFile directory = LocalFileSystem.getInstance().findFileByPath(path);
				if (directory != null && directory.isDirectory()) {
					VirtualFile[] templates = directory.getChildren();
					for (VirtualFile template : templates) {
						if (template.isDirectory()) {
							this.templatesList.put(template.getName(), new BitrixComponentTemplate(template.getName(), template));
						}
					}
				}
			} catch (NullPointerException npe) {
				//return null;
			}
		}
		return this.templatesList;
	}
}
