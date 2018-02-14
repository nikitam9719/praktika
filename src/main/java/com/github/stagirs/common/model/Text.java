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

/**
 *
 * @author Dmitriy Malakhov
 */
public class Text {
    private StringBuilder text;
    private String className;

    public Text(String className, String text) {
        this.className = className;
        this.text = new StringBuilder(text);
    }

    public String getText() {
        return text.toString();
    }

    public void append(char c){
        text.append(c);
    }

    public void append(String c){
        text.append(c);
    }
    
    public String getClassName() {
        return className;
    }
    
    
}
