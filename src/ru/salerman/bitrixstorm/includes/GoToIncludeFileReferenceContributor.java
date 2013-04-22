package ru.salerman.bitrixstorm.includes;

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
public class GoToIncludeFileReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {
        //$APPLICATION->GetTemplatePath("include_areas/company_name.php"),
        final String regexp = "(?:\\n*\\s*.*)\"([\\w/_]*.php)\"(?:\\n*\\s*.*)";  //GetTemplatePath(?:\n*\s*.*)\(     \)

        PsiElementPattern.Capture<StringLiteralExpression> psiElementCapture = PlatformPatterns.psiElement(
        StringLiteralExpression.class).withText(StandardPatterns.string().matches(regexp));

        psiReferenceRegistrar.registerReferenceProvider(
                psiElementCapture,
                new GoToIncludeFileReferenceProvider(),
                PsiReferenceRegistrar.DEFAULT_PRIORITY);
    }
}
