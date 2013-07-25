package ru.salerman.bitrixstorm.bitrix;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;

import static java.io.File.separator;

public class BitrixSiteTemplate {
    public static String BITRIX_SITE_TEMPLATES_PATH,
                         BITRIX_SITE_TEMPLATES_PATH_ESCAPED,
                         BITRIX_ROOT_PATH,
                         BITRIX_ROOT_PATH_ESCAPED;

    private String templateName = null;
    public static String sep = BitrixUtils.getEscapedSeparator();
	private static Hashtable<String, BitrixSiteTemplate> instancesList;

    private Project project;
    private PropertiesComponent BitrixSettings;

    private BitrixSiteTemplate(@NotNull Project prj) {
        this.project = prj;
        this.templateName = getName();
        refreshRootPath();
    }

    public void refreshRootPath() {
        BITRIX_ROOT_PATH = BitrixSettings.getValue(BitrixConfig.BITRIX_ROOT_PATH, "/bitrix");
        BITRIX_ROOT_PATH_ESCAPED = BITRIX_ROOT_PATH.replace("/", sep);
        BITRIX_SITE_TEMPLATES_PATH = BITRIX_ROOT_PATH + separator + "templates" + separator;
        BITRIX_SITE_TEMPLATES_PATH_ESCAPED = BITRIX_ROOT_PATH_ESCAPED + sep + "templates" + sep;
    }

    public static BitrixSiteTemplate getInstance (@NotNull Project project) {
	    String hash = project.getLocationHash();

	    if (instancesList == null) {
		    instancesList = new Hashtable<String, BitrixSiteTemplate>();
	    }

	    if (!instancesList.containsKey(hash)) {
		    instancesList.put(hash, new BitrixSiteTemplate(project));
	    }
	    return instancesList.get(hash);
    }

    public String getName() {
        if (this.templateName == null) {
            this.BitrixSettings = PropertiesComponent.getInstance(this.project);
            this.templateName = this.BitrixSettings.getValue(BitrixConfig.BITRIX_SITE_TEMPLATE, ".default");
        }
        return this.templateName;
    }

    public void setName(String templateName) {
        this.BitrixSettings = PropertiesComponent.getInstance(this.project);
        this.BitrixSettings.setValue(BitrixConfig.BITRIX_SITE_TEMPLATE, templateName);
        this.templateName = templateName;
    }

    public String getPathToHeader() {
        return this.project.getBasePath() + BITRIX_SITE_TEMPLATES_PATH + this.templateName + sep + "header.php";
    }

    public String getPathToFooter() {
        return this.project.getBasePath() + BITRIX_SITE_TEMPLATES_PATH + this.templateName + sep + "footer.php";
    }

    public String getSiteTemplate (@NotNull PsiElement path) {
        String pathToTpl = path.toString();
        if (pathToTpl.contains(BITRIX_SITE_TEMPLATES_PATH)) {
            String[] split = pathToTpl.split(BITRIX_SITE_TEMPLATES_PATH_ESCAPED);
            if (!split[1].contains(sep)) {
                return split[1];
            }
        }
        return null;
    }

    public boolean isSiteTemplate (@NotNull PsiElement path) {
        String pathToTpl = path.toString();
        if (pathToTpl != null && pathToTpl.contains(BITRIX_SITE_TEMPLATES_PATH)) {
            String[] split = pathToTpl.split(BITRIX_SITE_TEMPLATES_PATH_ESCAPED);
            if (split != null && !split[1].contains(sep)) {
                return true;
            }
        }
        return false;
    }

    public Hashtable<String, String> getTemplatesList () {
        Hashtable<String, String> templates = new Hashtable<String, String>();

        try {
            VirtualFile baseDir = this.project.getBaseDir();
            if (BITRIX_ROOT_PATH == sep + "bitrix") {
                baseDir = baseDir.findChild("bitrix").findChild("templates");
            } else {
                String[] dirs = BITRIX_ROOT_PATH_ESCAPED.split(sep);
                if (dirs != null) {
                    for (int i = 0; i < dirs.length; i++) {
                        if (dirs[i] != null && !dirs[i].contentEquals("")) {
                            String dir = dirs[i];
                            baseDir = baseDir.findChild(dir);
                        }
                    }
                    baseDir = baseDir.findChild("templates");
                }
            }

            if (baseDir == null) return null;

            PsiDirectory directory = PsiManager.getInstance(this.project).findDirectory(baseDir);
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