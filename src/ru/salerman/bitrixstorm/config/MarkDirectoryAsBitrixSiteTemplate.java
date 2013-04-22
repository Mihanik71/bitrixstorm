package ru.salerman.bitrixstorm.config;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 22.04.13
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
public class MarkDirectoryAsBitrixSiteTemplate extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        PsiElement path = LangDataKeys.PSI_ELEMENT.getData(dataContext);

        String siteTemplate = BitrixUtils.getSiteTemplate(path);

        BitrixUtils.setSiteTemplateName(siteTemplate);

        Messages.showMessageDialog("\"" + siteTemplate + "\" was marked as Bitrix Site Template ", "Information", Messages.getInformationIcon());
    }

    @Override
    public void update(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        PsiElement path = LangDataKeys.PSI_ELEMENT.getData(dataContext);

        Boolean isSiteTemplate = BitrixUtils.isSiteTemplate(path);

        if (!isSiteTemplate) {
            e.getPresentation().setEnabled(false);
        } else {
            e.getPresentation().setEnabled(true);
        }
    }
}
