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
public class Section extends Block{

    private List<Block> blocks = new ArrayList<>();
    private String title;
    private String docId;
    private double titleSemantic;
    
    public Section(String docId, String className) {
        super(className);
        this.docId = docId;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTitleSemantic() {
        return titleSemantic;
    }

    public void setTitleSemantic(double semantic) {
        int s = (int) (semantic * 1000);
        semantic = s > 0  ? s : 1;
        this.titleSemantic = semantic / 1000;
    }

    public String getDocId() {
        return docId;
    }
    
    @Override
    public boolean isPoint() {
        return false;
    }

    @Override
    public boolean isSection() {
        return true;
    }
    
    
    
}
