package ru.salerman.bitrixstorm.bitrix;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

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
public class BitrixEntity {
	private String filePath;
	private boolean bExists = false;
	private VirtualFile file;
	private List<BitrixEntityCodeTemplate> codeTemplatesList;

	BitrixEntity (String filePath) {
		this.filePath = filePath;

		this.file = LocalFileSystem.getInstance().findFileByPath(filePath);

		if (file != null) {
			this.bExists = true;
		}
	}

	public void setTemplate (BitrixEntityCodeTemplate obCodeTemplate) {
		this.codeTemplatesList.add(obCodeTemplate);
	}

	private boolean Exists() {
		return this.bExists;
	}

	public boolean Create () {
		/*
		$handle = fopen($this->filePath, "a");
if (!is_writable($this->filePath)) return false;
$out = '';

if (!empty($this->arCodeTemplates)) {
		foreach ($this->arCodeTemplates as $obTpl) {
		$out .= $obTpl->getCode();
}
		}

		if (fwrite($handle, $out) !== FALSE) {
		$this->bExists = true;
return true;
}
		return false;
		 */
		return true;
	}
}