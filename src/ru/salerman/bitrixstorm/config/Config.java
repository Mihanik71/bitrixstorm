package ru.salerman.bitrixstorm.config;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 16.04.13
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public class Config implements Configurable {

    private JComponent myComponent;
    private JTextField siteTemplateName;
    private JPanel myPanel;
    private PropertiesComponent BitrixSettings;

    @NonNls
    private static final String BITRIX_SITE_TEMPLATE = "BitrixStorm.Site.Template";
    private Project project;

    @Nls
    @Override
    public String getDisplayName() {
        return "BitrixStorm Settings";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preferences.lookFeel";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        ProjectManager instance = ProjectManager.getInstance();
        Project[] openProjects = instance.getOpenProjects();

        project = openProjects[0];

        BitrixSettings = PropertiesComponent.getInstance(project);

        String curSiteTemplateFromSettings = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");

        siteTemplateName.setText(curSiteTemplateFromSettings);

        myComponent = (JComponent) myPanel;
        return myComponent;
    }

    @Override
    public boolean isModified() {
        String curSiteTemplateFromSettings = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");
        if (siteTemplateName.getText() == curSiteTemplateFromSettings) {
            return false;
        }
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

        String updatedSiteTemplate = siteTemplateName.getText();
        BitrixSettings.setValue(BITRIX_SITE_TEMPLATE, updatedSiteTemplate);

    }

    @Override
    public void reset() {
        BitrixSettings.setValue(BITRIX_SITE_TEMPLATE, ".default");
    }

    @Override
    public void disposeUIResources() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
