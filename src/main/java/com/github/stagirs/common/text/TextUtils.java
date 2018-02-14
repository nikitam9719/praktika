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
public class TextUtils {
    public static List<String> splitWords(String text, boolean toLowerCase){
        List<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if(Character.isAlphabetic(text.charAt(i))){
                word.append(toLowerCase ? Character.toLowerCase(text.charAt(i)) : text.charAt(i));
                continue;
            }
            if(!word.toString().isEmpty()){
                words.add(word.toString());
                word = new StringBuilder();
            }
        }
        if(!word.toString().isEmpty()){
            words.add(word.toString());
            word = new StringBuilder();
        }
        return words;
    }
    
    public static String charactersClutch(String str){
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        for (int i = 1; i < str.length() - 1; i++) {
            if((i + 2 >= str.length() || !Character.isAlphabetic(str.charAt(i + 2))) &&
                Character.isAlphabetic(str.charAt(i + 1)) && 
               !Character.isAlphabetic(str.charAt(i)) && 
                Character.isAlphabetic(str.charAt(i - 1)) && 
               (i - 2 < 0 || !Character.isAlphabetic(str.charAt(i - 2)))){
                continue;
            }
            if((str.charAt(i) == '-' || str.charAt(i) == '-') && str.charAt(i + 1) == '\n'){
                i++;
                continue;
            }
            if(str.charAt(i) == '\n'){
                sb.append("\t");
                continue;
            }
            sb.append(str.charAt(i));
        }
        sb.append(str.charAt(str.length() - 1));
        return sb.toString();
    }
    
}
