package ru.salerman.bitrixstorm.bitrix;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NonNls;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 22.04.13
 * Time: 22:19
 * To change this template use File | Settings | File Templates.
 */
public class BitrixUtils {

    public static String componentName;
    public static String componentNameSpace = "bitrix";
    public static String currentTemplate = ".default";
    public static String siteTemplateName = ".default";
    public static PropertiesComponent BitrixSettings;
    @NonNls
    private static final String BITRIX_SITE_TEMPLATE = "BitrixStorm.Site.Template";

    public static String getSiteTemplateName() {

        ProjectManager instance = ProjectManager.getInstance();
        Project[] openProjects = instance.getOpenProjects();

        Project project = openProjects[0];

        BitrixSettings = PropertiesComponent.getInstance(project);
        siteTemplateName = BitrixSettings.getValue(BITRIX_SITE_TEMPLATE, ".default");
        return siteTemplateName;
    }

    public static String recognizeComponentTemplate() {
        return currentTemplate;
    }

    public static PsiFile getIncludeFile(String path, Project project) {
        if (path.substring(0, 1) != "/") {
            path = "/bitrix/templates/" + getSiteTemplateName() + "/" + path;
        }
        return getPsiFileByPath(project, project.getBasePath() + path);
    }

    public static PsiFile getSiteTemplateHeader(Project project) {
        String path = project.getBasePath() + "/bitrix/templates/" + getSiteTemplateName() + "/header.php";
        return getPsiFileByPath(project, path);
    }

    public static PsiFile getSiteTemplateFooter(Project project) {
        String path = project.getBasePath() + "/bitrix/templates/" + getSiteTemplateName() + "/footer.php";
        return getPsiFileByPath(project, path);
    }

    public static PsiFile findComponentTemplate(String componentNameSpace, String componentName, String templateName, Project project) {
        PsiFile tpl;
        String[] order = getComponentTemplatesPathOrder(componentNameSpace, componentName, templateName, project);

        for (String path : order) {
            tpl = getPsiFileByPath(project, path);
            if (tpl != null) {
                return tpl;
            }
        }

        return null;
    }

    private static String[] getComponentTemplatesPathOrder(String componentNameSpace, String componentName, String templateName, Project project) {
        String[] order = new String[3];

        if (templateName == "") {
            templateName = ".default";
        }

        order[0] = project.getBasePath() + "/bitrix/templates/" + getSiteTemplateName() + "/components/" + componentNameSpace + "/" + componentName + "/" + templateName + "/template.php";
        order[1] = project.getBasePath() + "/bitrix/templates/.default/components/" + componentNameSpace + "/" + componentName + "/" + templateName + "/template.php";
        order[2] = project.getBasePath() + "/bitrix/components/" + componentNameSpace + "/" + componentName + "/templates/" + templateName + "/template.php";

        return order;
    }

    private static PsiFile getPsiFileByPath(Project project, String defaultTemplatePath) {
        File myFile = new File(defaultTemplatePath);
        if(myFile.exists()) {
            VirtualFile vFile = LocalFileSystem.getInstance().findFileByIoFile(myFile);
            PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);

            return psiFile;
        }

        return null;
    }

    public static String[] findComponentTemplatesList() {
        String[] templates = new String[100];



        //FilenameIndex.getFilesByName("template.php")

        return templates;
    }
}
