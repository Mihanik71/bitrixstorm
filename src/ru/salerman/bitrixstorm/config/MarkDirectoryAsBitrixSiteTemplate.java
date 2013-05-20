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

package ru.salerman.bitrixstorm.config;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import ru.salerman.bitrixstorm.bitrix.BitrixSiteTemplate;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

public class MarkDirectoryAsBitrixSiteTemplate extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        PsiElement path = LangDataKeys.PSI_ELEMENT.getData(dataContext);

        String siteTemplate = BitrixSiteTemplate.getInstance(e.getProject()).getSiteTemplate(path);

        BitrixSiteTemplate.getInstance(e.getProject()).setName(siteTemplate);

        Messages.showMessageDialog("\"" + siteTemplate + "\" was marked as Bitrix Site Template ", "Information", Messages.getInformationIcon());
    }

    @Override
    public void update(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        PsiElement path = LangDataKeys.PSI_ELEMENT.getData(dataContext);

        Boolean isSiteTemplate = BitrixSiteTemplate.isSiteTemplate(path);

        if (!isSiteTemplate) {
            e.getPresentation().setVisible(false);
            e.getPresentation().setEnabled(false);
        } else {
            e.getPresentation().setVisible(true);
            e.getPresentation().setEnabled(true);
        }
    }
}
