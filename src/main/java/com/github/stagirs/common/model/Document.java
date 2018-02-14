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
public class Document {
    private String id;
    private String title;
    private double titleSemantic;
    private String author;
    private String classifier;
    private String thanks;
    private String output;
    
    
    private List<Block> blocks = new ArrayList<>();

    public String getAuthor() {
        return author;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public String getClassifier() {
        return classifier;
    }

    public String getId() {
        return id;
    }

    public String getOutput() {
        return output;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOutput(String output) {
        this.output = output;
    }
    
    public void addOutput(String output) {
        if(this.output == null){
            this.output = "";
        }
        this.output += output;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThanks() {
        return thanks;
    }

    public void setThanks(String thanks) {
        this.thanks = thanks;
    }

    public double getTitleSemantic() {
        return titleSemantic;
    }

    public void setTitleSemantic(double semantic) {
        int s = (int) (semantic * 1000);
        semantic = s > 0  ? s : 1;
        this.titleSemantic = semantic / 1000;
    }
    
    
    
    
}
