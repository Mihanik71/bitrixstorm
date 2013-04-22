package ru.salerman.bitrixstorm.templates.footer;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 17.04.13
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class GoToSiteTemplateFooterReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
        StringLiteralExpression se = (StringLiteralExpression) psiElement;
        PsiElement parent = psiElement.getParent();

        GoToSiteTemplateFooterReference psiReference = new GoToSiteTemplateFooterReference(parent, psiElement.getProject());

        if (psiReference.resolve() != null) {
            return new PsiReference[]{psiReference};
        }
        return PsiReference.EMPTY_ARRAY;
    }
}
