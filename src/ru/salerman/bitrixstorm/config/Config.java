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

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

import javax.swing.*;

public class Config implements Configurable {

    private static String curSiteTemplateFromSettings = ".default";
    private static String curSiteTemplateValue = ".default";
    private JComponent myComponent;
    private JComboBox siteTemplateName;
    private JPanel myPanel;
    private PropertiesComponent BitrixSettings;
    private String[] tpls;

    @NonNls
    private static final String BITRIX_SITE_TEMPLATE = "BitrixStorm.Site.Template";
    private Project project;

    @Nls
    @Override
    public String getDisplayName() {
        return "BitrixStorm";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        project = BitrixUtils.getProject();

        tpls = BitrixUtils.findComponentTemplatesList();

        BitrixSettings = PropertiesComponent.getInstance(project);

        curSiteTemplateFromSettings = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");

        for (int i=0; i<tpls.length; i++) {
            siteTemplateName.addItem(tpls[i]);
            if (tpls[i] == curSiteTemplateFromSettings) {
                siteTemplateName.setSelectedIndex(i);
            }
        }

        myComponent = (JComponent) myPanel;
        return myComponent;
    }

    @Override
    public boolean isModified() {
        curSiteTemplateFromSettings = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");
        curSiteTemplateValue = (String) siteTemplateName.getSelectedItem();
        if (curSiteTemplateValue.contentEquals(curSiteTemplateFromSettings)) {
            return false;
        }
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        curSiteTemplateFromSettings = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");
        curSiteTemplateValue = (String) siteTemplateName.getSelectedItem();
        if (curSiteTemplateValue != curSiteTemplateFromSettings) {
            BitrixSettings.setValue(BITRIX_SITE_TEMPLATE, curSiteTemplateValue);
        }
    }

    @Override
    public void reset() {
        curSiteTemplateFromSettings = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");
        curSiteTemplateValue = (String) siteTemplateName.getSelectedItem();

        for (int i=0; i<tpls.length; i++) {
            if (tpls[i].contentEquals(curSiteTemplateFromSettings)) {
                siteTemplateName.setSelectedIndex(i);
            }
        }
    }

    @Override
    public void disposeUIResources() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
