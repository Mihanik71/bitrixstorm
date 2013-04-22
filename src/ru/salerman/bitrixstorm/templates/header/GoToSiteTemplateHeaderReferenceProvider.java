package ru.salerman.bitrixstorm.templates.header;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 22.04.13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public class GoToSiteTemplateHeaderReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
        StringLiteralExpression se = (StringLiteralExpression) psiElement;
        PsiElement parent = psiElement.getParent();

        GoToSiteTemplateHeaderReference psiReference = new GoToSiteTemplateHeaderReference(parent, psiElement.getProject());

        if (psiReference.resolve() != null) {
            return new PsiReference[]{psiReference};
        }
        return PsiReference.EMPTY_ARRAY;
    }
}