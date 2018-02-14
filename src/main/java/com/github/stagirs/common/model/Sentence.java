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
public class Sentence {
    private int number;
    private double semantic;
    private int pointNumber;
    private String docId;
    private List<Text> parts = new ArrayList<>();
    
    public Sentence(String docId, int pointNumber, int number, double semantic) {
        this.number = number;
        this.pointNumber = pointNumber;
        this.docId = docId;
        this.semantic = semantic;
    }

    public List<Text> getParts() {
        return parts;
    }

    public void setParts(List<Text> parts) {
        this.parts = parts;
    }

    public int getNumber() {
        return number;
    }

    public double getSemantic() {
        return semantic;
    }

    public void setSemantic(double semantic) {
        int s = (int) (semantic * 1000);
        semantic = s > 0  ? s : 1;
        this.semantic = semantic / 1000;
    }

    public String getDocId() {
        return docId;
    }

    public int getPointNumber() {
        return pointNumber;
    }
    
    
    
}
