/*
 * Copyright 2016 Dmitriy Malakhov.
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
package com.github.stagirs.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Point extends Block{
    private int number;
    private String docId;
    
    private List<Sentence> sentences = new ArrayList<>();

    public Point(String docId, int number, String className) {
        super(className);
        this.number = number;
        this.docId = docId;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public int getNumber() {
        return number;
    }

    public String getDocId() {
        return docId;
    }

    @Override
    public boolean isPoint() {
        return true;
    }

    @Override
    public boolean isSection() {
        return false;
    }
    
    
}
