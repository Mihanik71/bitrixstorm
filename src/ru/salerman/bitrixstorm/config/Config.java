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
import ru.salerman.bitrixstorm.bitrix.BitrixConfig;
import ru.salerman.bitrixstorm.bitrix.BitrixSiteTemplate;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class Config implements Configurable {

    private static String curSiteTemplateFromSettings = ".default";
    private static String curSiteTemplateValue = ".default";
    private static String curBitrixPathFromSettings = "/bitrix";
    private static String curBitrixPathValue = "/bitrix";
    private JComponent myComponent;
    private JComboBox siteTemplateName;
    private JPanel myPanel;
    //private JButton choisePathToBitrixBtn;
    //private JTextField pathToBitrixTextField;
    private PropertiesComponent BitrixSettings;
    private Set<String> tpls;

    @NonNls
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
        try {
            project = BitrixUtils.getProject();
            tpls = BitrixSiteTemplate.getTemplatesList(project).keySet();
            BitrixSettings = PropertiesComponent.getInstance(project);
            curSiteTemplateFromSettings = BitrixSettings.getValue(BitrixConfig.BITRIX_SITE_TEMPLATE, ".default");

            getBitrixStormSettings();

            if (tpls != null && !tpls.isEmpty()) {
                Iterator it = tpls.iterator();
                int i = 0;
                while (it.hasNext()) {
                    String tpl = it.next().toString();
                    siteTemplateName.addItem(tpl);
                    if (tpl == curSiteTemplateFromSettings) {
                        siteTemplateName.setSelectedIndex(i);
                    }
                    i++;
                }
            }
        } catch (NullPointerException npe) {

        }

        myComponent = (JComponent) myPanel;
        return myComponent;
    }

    @Override
    public boolean isModified() {
        try {
            getBitrixStormSettings();
            getCurrentValues();

            if (curSiteTemplateValue.contentEquals(curSiteTemplateFromSettings)){// && curBitrixPathFromSettings.contentEquals(curBitrixPathValue)) {
                return false;
            }
            return true;
        } catch (NullPointerException npe) {
            return false;
        }
    }

    @Override
    public void apply() throws ConfigurationException {
        try {
            getBitrixStormSettings();
            getCurrentValues();

            if (!curSiteTemplateValue.contentEquals(curSiteTemplateFromSettings)){// || !curBitrixPathFromSettings.contentEquals(curBitrixPathValue)) {
                BitrixSettings = PropertiesComponent.getInstance(project);
                BitrixSettings.setValue(BitrixConfig.BITRIX_SITE_TEMPLATE, curSiteTemplateValue);
                //BitrixConfig.getInstance(project).setValue(BitrixConfig.BITRIX_PATH, curBitrixPathValue);
            }
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public void reset() {
        getBitrixStormSettings();
        getCurrentValues();

        if (tpls != null && !tpls.isEmpty()) {
            int i = 0;
            Iterator it = tpls.iterator();
            while (it.hasNext()) {
                String tpl = it.next().toString();
                if (tpl.contentEquals(curSiteTemplateFromSettings)) {
                    siteTemplateName.setSelectedIndex(i);
                }
                i++;
            }
        }

        //pathToBitrixTextField.setText(curBitrixPathFromSettings);
    }

    private void getBitrixStormSettings() {
        BitrixSettings = PropertiesComponent.getInstance(project);
        curSiteTemplateFromSettings = BitrixSettings.getValue(BitrixConfig.BITRIX_SITE_TEMPLATE, ".default");
        //curBitrixPathFromSettings = BitrixConfig.getInstance(project).getValue(BitrixConfig.BITRIX_PATH, "/bitrix");
    }

    private void getCurrentValues() {
        if (tpls != null) {
            curSiteTemplateValue = (String) siteTemplateName.getSelectedItem();
        } else {
            curBitrixPathValue = ".default";
        }
        //curBitrixPathValue = pathToBitrixTextField.getText();
    }

    @Override
    public void disposeUIResources() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
