package ru.salerman.bitrixstorm.bitrix;

import com.intellij.lang.FileASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.codeInsight.PhpScope;
import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlow;
import com.jetbrains.php.lang.psi.PhpExpressionCodeFragment;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Statement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by r3c130n on 21.12.13.
 */
public class BitrixLangFilesManager {

    String path;
    PsiElement psiElement;
    String defaultLang = "ru";
    PsiDirectory langDirectory;
    String fileName;
    PsiFile psiFile;

    public BitrixLangFilesManager (PsiElement psiElement) {
        this.psiElement = psiElement;
        this.getLangFile();
    }

    private void getLangFile () {
        this.fileName = this.psiElement.getContainingFile().getName();
        if (this.psiElement.getContainingFile().getContainingDirectory().findSubdirectory("lang") == null) {
            this.psiElement.getContainingFile().getContainingDirectory().createSubdirectory("lang");
        }
        this.langDirectory = this.psiElement.getContainingFile().getContainingDirectory().findSubdirectory("lang");
    }

    private boolean langFileExists(String lang) {
        try {
            return (this.langDirectory.isDirectory() && this.langDirectory.findSubdirectory(lang).isDirectory() && this.langDirectory.findSubdirectory(lang).findFile(this.fileName).isWritable());
        } catch (NullPointerException npe) {
            return false;
        }
    }

    private boolean prepareLangFile (String lang) {
        if (!this.langFileExists(lang)) {
            if (!this.langDirectory.isDirectory()) {
                return false;
            }
            if (!this.langDirectory.isWritable()) {
                return false;
            }
            if (this.langDirectory.findSubdirectory(lang) == null) {
                this.langDirectory.createSubdirectory(lang);
            }

            if (!this.langDirectory.findSubdirectory(lang).isDirectory()) {
                this.langDirectory.createSubdirectory(lang);
            }
            if (!this.langDirectory.findSubdirectory(lang).isWritable()) {
                return false;
            }
            if (this.langDirectory.findSubdirectory(lang).findFile(this.fileName) == null) {
                this.psiFile = this.langDirectory.findSubdirectory(lang).createFile(this.fileName);
            } else {
                this.psiFile = this.langDirectory.findSubdirectory(lang).findFile(this.fileName);
                return true;
            }
            if (!this.langDirectory.findSubdirectory(lang).findFile(this.fileName).isWritable()) {
                return false;
            }
            return true;
        }
        this.psiFile = this.langDirectory.findSubdirectory(lang).createFile(this.fileName);
        return true;
    }

    public void set (String key, String text, String lang) {
        if (this.prepareLangFile(lang)) {
            String mess = "$MESS[\"" + key + "\"] = \"" + text + "\";";

            PhpPsiElement el = PhpPsiElementFactory.createPhpPsiFromText(this.psiElement.getProject(), TokenSet.EMPTY, mess);
            
            this.psiFile.add(el);
        }
    }

    public void set (String key, String text) {
        this.set(key, text, this.defaultLang);
    }

    public String get (String key, String lang) {
        return "";
    }

    public String get (String key) {
        return this.get(key, this.defaultLang);
    }


}
