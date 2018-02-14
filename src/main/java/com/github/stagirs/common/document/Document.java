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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Document {
    private String id;
    private String title;
    private String author;
    private String annotation;
    private String notes;
    private String output;
    private int importance;
    private List<Tag> tags = new ArrayList<>();
    private List<Point> points = new ArrayList<>();
    private Map<String, String> classifiers = new HashMap<>();
    
    /**
     * @return уникальный идентификатор документа
     */
    public String getId() {   
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return тэги, выделенные из метаописания документа, не включают теги выделенные из абзацев
     */
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    /**
     * @return заголовок документа
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return информация об авторе
     */
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * @return классификаторы документа: название классификатора -> id класса
     */
    public Map<String, String> getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(Map<String, String> classifiers) {
        this.classifiers = classifiers;
    }

    /**
     * @return выходные данные
     */
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
    /**
     * @return абзацы документа
     */
    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
    /**
     * @return примечание
     */
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    /**
     * @return аннотация документа
     */
    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    /**
     * @return важность документа
     */
    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }
    
}
