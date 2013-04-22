/*
 * Copyright 2011-2013 Salerman <www.salerman.ru>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Mikhail Medvedev aka r3c130n <mm@salerman.ru>
 * @link http://www.salerman.ru/
 * @date: 23.04.2013
 */

package ru.salerman.bitrixstorm.components;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

public class GoToTemplateOfComponentReference implements PsiReference {

    private String templateString;
    private Project project;
    private String cleanString;
    private PsiElement psiElement;
    private TextRange textRange;
    private String component;
    private String nameSpace;
    private String templateName;


    public GoToTemplateOfComponentReference(final PsiElement psiElement, Project project) {
        this.psiElement = psiElement;
        this.templateString = psiElement.getText();
        this.project = project;
        String[] allStrings = this.templateString.toLowerCase().split("array");
        cleanString = allStrings[0].replace("\"", "").replace("'", "").replace("\n","").replace(" ","").replace("\t","");
        cleanString = cleanString.substring(0, cleanString.length() - 1);


        final String[] pathElements = cleanString.split(":");

        this.nameSpace = pathElements[0];
        String[] cmptpl = pathElements[1].split(",");
        this.component = cmptpl[0];
        if (cmptpl.length == 2) {
            this.templateName = cmptpl[1];
        } else {
            this.templateName = "";
        }

        int start = this.templateString.indexOf(this.component);
        int stop = start + component.length();

        textRange = new TextRange(1, stop);
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
        return BitrixUtils.findComponentTemplate(nameSpace, component, templateName, project);
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
