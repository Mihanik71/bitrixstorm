package ru.salerman.bitrixstorm.config;
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

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import ru.salerman.bitrixstorm.bitrix.BitrixComponentManager;
import ru.salerman.bitrixstorm.bitrix.BitrixLangFilesManager;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

/**
 * Created by r3c130n on 21.12.13.
 */
public class extractToLangFile extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = (VirtualFile) e.getDataContext().getData(DataConstants.VIRTUAL_FILE);

        PsiFile path = BitrixUtils.getPsiFileByPath(file.getPath());

        final Editor editor = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor();

        String selectedText = editor.getSelectionModel().getSelectedText();

        BitrixLangFilesManager lang = new BitrixLangFilesManager(path);

        String lang_key = BitrixUtils.translite(selectedText).toUpperCase();

        lang.set(lang_key, selectedText);

        final String replacer = "<?=GetMessage(\"" + lang_key + "\");?>";

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                editor.getDocument().replaceString(editor.getSelectionModel().getSelectionStart(), editor.getSelectionModel().getSelectionEnd(), replacer);
            }
        });
    }
}
