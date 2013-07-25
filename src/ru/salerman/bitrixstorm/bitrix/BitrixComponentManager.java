package ru.salerman.bitrixstorm.bitrix;
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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;

public class BitrixComponentManager {
	private Hashtable<String, BitrixComponent> componentsList;
	private static Hashtable<String, BitrixComponentManager> instancesList;
	private Project project;

	BitrixComponentManager (@NotNull Project project) {
		this.project = project;
		this.componentsList = new Hashtable<String, BitrixComponent>();
		this.fillComponents();
	}

	public static BitrixComponentManager getInstance (@NotNull Project project) {
		String hash = project.getLocationHash();

		if (instancesList == null) {
			instancesList = new Hashtable<String, BitrixComponentManager>();
		}

		if (!instancesList.containsKey(hash)) {
			instancesList.put(hash, new BitrixComponentManager(project));
		}
		return instancesList.get(hash);
	}

	public Hashtable<String, BitrixComponent> getComponents() {
		return this.componentsList;
	}

	public BitrixComponent getComponent(String componentString) {
		if (this.componentsList.containsKey(componentString)) {
			return this.componentsList.get(componentString);
		}
		return null;
	}

	private void fillComponents() {
		String componentsPath = this.project.getBasePath() + "/bitrix/components";
		try {
			VirtualFile directory = LocalFileSystem.getInstance().findFileByPath(componentsPath);
			if (directory != null && directory.isDirectory()) {
				VirtualFile[] nameSpaces = directory.getChildren();
				for (VirtualFile namespace : nameSpaces) {
					if (namespace.isDirectory()) {
						VirtualFile[] components = namespace.getChildren();
						for (VirtualFile component : components) {
							if (component.isDirectory()) {
								this.componentsList.put(namespace.getName() + ":" + component.getName(), new BitrixComponent(namespace.getName(), component.getName()));
							}
						}
					}
				}
			}

		} catch (NullPointerException npe) {
			return;
		}
	}
}
