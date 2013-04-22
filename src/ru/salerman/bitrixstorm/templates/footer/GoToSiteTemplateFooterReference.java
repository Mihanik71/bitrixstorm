package ru.salerman.bitrixstorm.templates.footer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 17.04.13
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class GoToSiteTemplateFooterReference implements PsiReference {

    private String templateString;
    private Project project;
    private PsiElement psiElement;
    private TextRange textRange;

    public GoToSiteTemplateFooterReference(final PsiElement psiElement, Project project) {
        this.psiElement = psiElement;
        this.templateString = psiElement.getText();
        this.project = project;

        textRange = new TextRange(7, this.templateString.length() - 1);
    }

    @Override
    public PsiElement getElement() {
        return psiElement;
    }

    @Override
    public TextRange getRangeInElement() {
        return textRange;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return BitrixUtils.getSiteTemplateFooter(project);
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return this.templateString;
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        return resolve();
    }

    @Override
    public boolean isReferenceTo(PsiElement psiElement) {
        return false;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }

    @Override
    public boolean isSoft() {
        return true;
    }
}
