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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Point {
    private int level;
    private String title;
    private String text;
    private List<Tag> tags = new ArrayList<>();
    /**
     * У документа могут быть вложенные друг в друга разделы. Уровень вложенности определяет сколько родительских разделов имеет абзац.
     * @return уровень вложенности, на котором находится абзац
     */
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    /**
     * Если абзац идет первым в разделе с заголовком, то этот заголовок приписывается к этому абзацу
     * @return заголовок абзаца
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return текст абзаца
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    /**
     * @return тэги выделенные в абзаце
     */
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return text;
    }
    
    
}
