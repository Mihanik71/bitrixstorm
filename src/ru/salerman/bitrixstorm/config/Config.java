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
import com.intellij.openapi.application.ImportOldConfigsPanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import ru.salerman.bitrixstorm.bitrix.BitrixConfig;
import ru.salerman.bitrixstorm.bitrix.BitrixSiteTemplate;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private JTextField pathToBitrixFolder;
    private JButton choiseBitrixFolderBtn;
    private PropertiesComponent BitrixSettings;
    private Set<String> tpls;

    @NonNls
    private Project project;

    public Config() {
        choiseBitrixFolderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fc = new JFileChooser(project.getBasePath());
               // if (myLastSelection != null){
               //     fc = new JFileChooser(myLastSelection);
               // }

                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //fc.setFileSelectionMode(SystemInfo.isMac ? JFileChooser.FILES_AND_DIRECTORIES : JFileChooser.DIRECTORIES_ONLY);
                //fc.setFileHidingEnabled(!SystemInfo.isLinux);

                int returnVal = fc.showOpenDialog(myComponent);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    if (file != null){
                        String bxSrcPath = file.getAbsolutePath();
                        setBitrixPath(bxSrcPath.replace(project.getBasePath(), ""));
                    }
                }
            }
        });
    }

    private void setBitrixPath (String path) {
        pathToBitrixFolder.setText(path);
        curBitrixPathValue = path;
    }

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
            BitrixSettings = PropertiesComponent.getInstance(project);

            getBitrixStormSettings();

            refreshTemplatesList();
        } catch (NullPointerException npe) {

        }

        myComponent = (JComponent) myPanel;
        return myComponent;
    }

    private void refreshTemplatesList() {
        try {
            tpls = BitrixSiteTemplate.getInstance(project).getTemplatesList().keySet();

            if (tpls != null && !tpls.isEmpty()) {
                siteTemplateName.setEnabled(true);
                Iterator it = tpls.iterator();
                int i = 0;
                siteTemplateName.removeAllItems();
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
            siteTemplateName.setEnabled(false);
        }
    }

    @Override
    public boolean isModified() {
        try {
            getBitrixStormSettings();
            getCurrentValues();

            if (curSiteTemplateValue.contentEquals(curSiteTemplateFromSettings) && curBitrixPathFromSettings.contentEquals(curBitrixPathValue)) {
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

            if (!curSiteTemplateValue.contentEquals(curSiteTemplateFromSettings) || !curBitrixPathFromSettings.contentEquals(curBitrixPathValue)) {
                BitrixSettings = PropertiesComponent.getInstance(project);
                BitrixSettings.setValue(BitrixConfig.BITRIX_SITE_TEMPLATE, curSiteTemplateValue);
                BitrixSettings.setValue(BitrixConfig.BITRIX_ROOT_PATH, curBitrixPathValue);
                BitrixSiteTemplate.getInstance(project).refreshRootPath();
            }
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public void reset() {
        getBitrixStormSettings();
        getCurrentValues();

        refreshTemplatesList();

        pathToBitrixFolder.setText(curBitrixPathFromSettings);
    }

    private void getBitrixStormSettings() {
        BitrixSettings = PropertiesComponent.getInstance(project);
        curSiteTemplateFromSettings = BitrixSettings.getValue(BitrixConfig.BITRIX_SITE_TEMPLATE, ".default");
        curBitrixPathFromSettings = BitrixSettings.getValue(BitrixConfig.BITRIX_ROOT_PATH, "/bitrix");
    }

    private void getCurrentValues() {
        if (tpls != null) {
            curSiteTemplateValue = (String) siteTemplateName.getSelectedItem();
        } else {
            curBitrixPathValue = ".default";
        }
        curBitrixPathValue = pathToBitrixFolder.getText();
    }

    @Override
    public void disposeUIResources() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
