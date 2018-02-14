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
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Dmitriy Malakhov
 */
public class DocumentParser {
    
    private static List<String> split(String str){
        List<String> list = new ArrayList<>();
        while(str.contains("<")){
            if(str.indexOf("<") > 0){
                list.add(str.substring(0, str.indexOf("<")));
                str = str.substring(str.indexOf("<"));
            }
            list.add(str.substring(0, str.indexOf(">") + 1));
            str = str.substring(str.indexOf(">") + 1);
        }
        list.add(str);
        return list;
    }
    
    public static Document parse(File file) throws IOException{
        Document doc = new Document();
        doc.setId(file.getName());
        Iterator<String> list = split(FileUtils.readFileToString(file, "utf-8")).iterator();
        while(list.hasNext()){
            String item = list.next();
            if(item.equals("<div class='section'>")){
                doc.getBlocks().add(parseSection(doc.getId(), item, list));
                continue;
            }
            if(item.startsWith("<p")){
                doc.getBlocks().add(parsePoint(doc.getId(), item, list));
                continue;
            }
            if(item.startsWith("<div class='title' ")){
                item = item.split("semantic='")[1];
                doc.setTitleSemantic(Double.parseDouble(item.substring(0, item.indexOf("'"))));
                doc.setTitle(list.next());
                list.next();
                continue;
            }
            if(item.equals("<div class='author'>")){
                doc.setAuthor(list.next());
                list.next();
                continue;
            }
            if(item.equals("<div class='classifier'>")){
                doc.setClassifier(list.next());
                list.next();
                continue;
            }
            if(item.equals("<div class='thanks'>")){
                doc.setThanks(list.next());
                list.next();
                continue;
            }
            if(item.equals("<div class='output'>")){
                doc.setOutput(list.next());
                list.next();
                continue;
            }
        }
        return doc;
    }
    
    public static Section parseSection(String docId, String item, Iterator<String> list){
        Section section = new Section(docId, null);
        while(list.hasNext()){
            item = list.next();
            if(item.equals("</div>")){
                return section;
            }
            if(item.startsWith("<div class='section-title' ")){
                item = item.split("semantic='")[1];
                section.setTitleSemantic(Double.parseDouble(item.substring(0, item.indexOf("'"))));
                section.setTitle(list.next());
                list.next();
                continue;
            }
            if(item.equals("<div class='section'>")){
                section.getBlocks().add(parseSection(docId, item, list));
                continue;
            }
            if(item.startsWith("<p")){
                section.getBlocks().add(parsePoint(docId, item, list));
                continue;
            }
        }    
        throw new RuntimeException("can't find </div>");
    }
    
    public static Point parsePoint(String docId, String item, Iterator<String> list){
        String[] parts = item.split("'");
        Point point = new Point(docId, parseInt(parts[1]), parts.length > 3 ? parts[3] : null);
        while(list.hasNext()){
            item = list.next();
            if(item.equals("</p>")){
                return point;
            }
            point.getSentences().add(parseSentence(docId, point.getNumber(), item, list));
        }    
        throw new RuntimeException("can't find </p>");
    }
    
    public static Sentence parseSentence(String docId, int pointNumber, String item, Iterator<String> list){
        String[] parts = item.split("'");
        Sentence sentence = new Sentence(docId, pointNumber, parseInt(parts[1]), parseDouble(parts[3]));
        while(list.hasNext()){
            item = list.next();
            if(item.equals("</span>")){
                return sentence;
            }
            if(item.startsWith("<span")){
                sentence.getParts().add(new Text(item.split("'")[1], list.next()));
                list.next();
                continue;
            }
            sentence.getParts().add(new Text(null, item));
        }    
        throw new RuntimeException("can't find </span>");
    }
}
