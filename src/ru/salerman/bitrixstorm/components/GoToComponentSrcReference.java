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
import ru.salerman.bitrixstorm.bitrix.BitrixComponent;
import ru.salerman.bitrixstorm.bitrix.BitrixComponentTemplate;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

public class GoToComponentSrcReference implements PsiReference {

    private String componentString;
    private PsiElement psiElement;
    private TextRange textRange;
    private BitrixComponent component;

    public GoToComponentSrcReference(final PsiElement psiElement, Project project) {
        this.psiElement = psiElement;
        this.componentString = psiElement.getText();

        this.component = BitrixComponent.initComponentFromString(componentString);

        int start = this.componentString.indexOf(this.component.getName());
        int stop = start + this.component.getName().length();

        textRange = new TextRange(start, stop);
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
        return component.findComponentSrc();
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return this.componentString;
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
