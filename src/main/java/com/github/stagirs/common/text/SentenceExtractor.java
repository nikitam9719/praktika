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
package com.github.stagirs.common.text;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class SentenceExtractor {
    
    public List<String> extract(String text){
        text = text.replaceAll("([А-Я])", ". $1");
        List<String> list = new ArrayList<>();
        int curIndex = text.indexOf(".");
        int lastSentenceIndex = 0;
        while(curIndex > -1){
            int lastWordSize = getLastWordSize(text, curIndex);
            int firstLetterPos = curIndex;
            while(firstLetterPos < text.length() && !Character.isLetter(text.charAt(firstLetterPos))){
                firstLetterPos++;
            }
            if(firstLetterPos >= text.length() || Character.isUpperCase(text.charAt(firstLetterPos)) && lastWordSize > 2){
                String sentence = text.substring(lastSentenceIndex, curIndex + 1);
                if(sentence.length() > 0 && getLastWordSize(sentence, sentence.length() - 1) > 0){
                    list.add(sentence);
                }
                lastSentenceIndex = curIndex + 1;
            }
            curIndex = text.indexOf(".", curIndex + 1);
        }
        String sent = text.substring(lastSentenceIndex).trim();
        if(sent.replaceAll("\\s+", " ").matches(".*?[а-я].*?")){
            list.add(sent);
        }
        return list;
    }
    
    private int getLastWordSize(String text, int curIndex){
        int lastWordSize = 0;
        while(curIndex >= 0 &&!Character.isAlphabetic(text.charAt(curIndex))){
            curIndex--;
        }
        while(curIndex >= 0 && Character.isAlphabetic(text.charAt(curIndex))){
            curIndex--;
            lastWordSize++;
        }
        return lastWordSize;
    }
    
    public static SentenceExtractor get(){
        return new SentenceExtractor();
    }
}
