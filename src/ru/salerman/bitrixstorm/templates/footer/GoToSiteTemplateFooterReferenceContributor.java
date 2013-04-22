package ru.salerman.bitrixstorm.templates.footer;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 17.04.13
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
public class GoToSiteTemplateFooterReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {
        //require($_SERVER["DOCUMENT_ROOT"]."/bitrix/footer.php");
        final String regexp = "[\"]\\/bitrix\\/footer\\.php[\"]";

        PsiElementPattern.Capture<StringLiteralExpression> psiElementCapture = PlatformPatterns.psiElement(
        StringLiteralExpression.class).withText(StandardPatterns.string().matches(regexp));

        psiReferenceRegistrar.registerReferenceProvider(
                psiElementCapture,
                new GoToSiteTemplateFooterReferenceProvider(),
                PsiReferenceRegistrar.DEFAULT_PRIORITY);
    }
}
