package ru.salerman.bitrixstorm.templates.header;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

/**
 * Created with IntelliJ IDEA.
 * User: r3c130n
 * Date: 22.04.13
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public class GoToSiteTemplateHeaderReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {
        //require($_SERVER["DOCUMENT_ROOT"]."/bitrix/header.php");
        final String regexp = "[\"]\\/bitrix\\/header\\.php[\"]";

        PsiElementPattern.Capture<StringLiteralExpression> psiElementCapture = PlatformPatterns.psiElement(
                StringLiteralExpression.class).withText(StandardPatterns.string().matches(regexp));

        psiReferenceRegistrar.registerReferenceProvider(
                psiElementCapture,
                new GoToSiteTemplateHeaderReferenceProvider(),
                PsiReferenceRegistrar.DEFAULT_PRIORITY);
    }
}
