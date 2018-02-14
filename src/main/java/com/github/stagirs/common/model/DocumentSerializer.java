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

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class DocumentSerializer {
    public static void serialize(File file, Document document) throws IOException{
        StringBuilder sb = new StringBuilder();
        serialize(sb, document);
        FileUtils.writeStringToFile(new File(file, document.getId()), sb.toString(), "utf-8");
    }
    
    public static void serialize(StringBuilder sb, Document document) throws IOException{
        if(document.getTitle() != null){
            sb.append("<div class='title' semantic='").append(document.getTitleSemantic()).append("'>").append(document.getTitle()).append("</div>");
        }
        if(document.getAuthor() != null){
            sb.append("<div class='author'>").append(document.getAuthor()).append("</div>");
        }
        if(document.getClassifier()!= null){
            sb.append("<div class='classifier'>").append(document.getClassifier()).append("</div>");
        }
        if(document.getThanks() != null){
            sb.append("<div class='thanks'>").append(document.getThanks()).append("</div>");
        }
        if(document.getOutput() != null){
            sb.append("<div class='output'>").append(document.getOutput()).append("</div>");
        }
        for (Block block : document.getBlocks()) {
            if(block instanceof Point){
                serialize(sb, (Point) block);
            }
            if(block instanceof Section){
                serialize(sb, (Section) block);
            }
        }
    }
    
    public static void serialize(StringBuilder sb, Section section){
        sb.append("<div class='section'>")
                    .append("<div class='section-title'semantic='").append(section.getTitleSemantic()).append("'>").append(section.getTitle()).append("</div>");
        for (Block block : section.getBlocks()) {
            if(block instanceof Point){
                serialize(sb, (Point) block);
            }
            if(block instanceof Section){
                serialize(sb, (Section) block);
            }
        }
        sb.append("</div>");
    }
    
    public static void serialize(StringBuilder sb, Point point){
        if(point.getClassName() != null){
            sb.append("<p pos='").append(point.getNumber()).append("' class='").append(point.getClassName()).append("'>");
        }else{
            sb.append("<p pos='").append(point.getNumber()).append("'>");
        }
        for (Sentence sentence : point.getSentences()) {
            serialize(sb, sentence);
        }
        sb.append("</p>");
    }
    
    public static void serialize(StringBuilder sb, Sentence sentence){
        sb.append("<span pos='").append(sentence.getNumber()).append("' semantic='").append(sentence.getSemantic()).append("' class='sentence'>");
        for (Text text : sentence.getParts()) {
            if(text.getClassName() != null){
                sb.append("<span class='").append(text.getClassName()).append("'>").append(text.getText().trim().replace("<", "&lt;").replace(">", "&rt;")).append("</span>");
            }else{
                sb.append(text.getText().trim().replace("<", "&lt;").replace(">", "&rt;"));
            }
        }
        sb.append("</span>");
    }
}
