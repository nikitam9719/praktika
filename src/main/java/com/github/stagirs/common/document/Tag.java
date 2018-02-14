/*
 * Copyright 2017 Dmitriy Malakhov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stagirs.common.document;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Tag {
    private String[] words;
    private int semantic;

    public int getSemantic() {
        return semantic;
    }

    public void setSemantic(int semantic) {
        this.semantic = semantic;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public String[] getWords() {
        return words;
    }

    @Override
    public String toString() {
        return StringUtils.join(words, " ");
    }
    
    
}
