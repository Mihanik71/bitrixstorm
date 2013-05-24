package ru.salerman.bitrixstorm.bitrix;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NonNls;

import java.util.Hashtable;

import static java.io.File.separator;

public class BitrixSiteTemplate {
    public static final String BITRIX_SITE_TEMPLATE = "BitrixStorm.Site.Template";
    public static final String BITRIX_SITE_TEMPLATES_PATH = separator + "bitrix" + separator + "templates" + separator;
    public static String BITRIX_SITE_TEMPLATES_PATH_ESCAPED;

    private String templateName = null;
    private static String sep = BitrixUtils.getEscapedSeparator();
    private static BitrixSiteTemplate instance = null;

    @NonNls
    private static Project project;
    private PropertiesComponent BitrixSettings;

    private BitrixSiteTemplate(Project prj) {
        this.templateName = getName();
        project = prj;
    }
    
    public static BitrixSiteTemplate getInstance (Project prj) {
        if (instance == null) {
            project = prj;
            instance = new BitrixSiteTemplate(project);
            BITRIX_SITE_TEMPLATES_PATH_ESCAPED = sep + "bitrix" + sep + "templates" + sep;
        }
        return instance;
    }

    public String getName() {
        if (this.templateName == null) {
            this.BitrixSettings = PropertiesComponent.getInstance(project);
            this.templateName = this.BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");
        }
        return templateName;
    }

    public void setName(String templateName) {
        this.BitrixSettings = PropertiesComponent.getInstance(project);
        this.BitrixSettings.setValue(BITRIX_SITE_TEMPLATE, templateName);
        this.templateName = templateName;
    }

    public String getPathToHeader() {
        if (project == null) return null;
        return project.getBasePath() + BITRIX_SITE_TEMPLATES_PATH + templateName + sep + "header.php";
    }

    public String getPathToFooter() {
        if (project == null) return null;
        return project.getBasePath() + BITRIX_SITE_TEMPLATES_PATH + templateName + sep + "footer.php";
    }

    public String getSiteTemplate (PsiElement path) {
        String pathToTpl = path.toString();
        if (pathToTpl.contains(BITRIX_SITE_TEMPLATES_PATH)) {
            String[] split = pathToTpl.split(BITRIX_SITE_TEMPLATES_PATH_ESCAPED);
            if (!split[1].contains(sep)) {
                return split[1];
            }
        }
        return null;
    }

    public static boolean isSiteTemplate (PsiElement path) {
        String pathToTpl = path.toString();
        if (pathToTpl != null && pathToTpl.contains(BITRIX_SITE_TEMPLATES_PATH)) {
            String[] split = pathToTpl.split(BITRIX_SITE_TEMPLATES_PATH_ESCAPED);
            if (split != null && !split[1].contains(sep)) {
                return true;
            }
        }
        return false;
    }

    public static Hashtable<String, String> getTemplatesList (Project project) {
        Hashtable<String, String> templates = new Hashtable<String, String>();

        try {
            VirtualFile baseDir = project.getBaseDir().findChild("bitrix").findChild("templates");
            PsiDirectory directory = PsiManager.getInstance(project).findDirectory(baseDir);
            PsiElement[] children = directory.getChildren();

            for (int i = 0; i < children.length; i++) {
                templates.put(
                        BitrixUtils.getFileNameByPsiElement(children[i]),
                        BitrixUtils.getPathByPsiElement(children[i])
                );
            }


        } catch (NullPointerException ex) {
            return null;
        }

        return templates;
    }
}