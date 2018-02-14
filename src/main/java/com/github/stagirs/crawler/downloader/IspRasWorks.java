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
package com.github.stagirs.crawler.downloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stagirs.crawler.Downloader;
import com.github.stagirs.crawler.model.business.Author;
import com.github.stagirs.crawler.model.business.Record;
import com.github.stagirs.crawler.model.business.Record.Type;
import com.github.stagirs.crawler.model.service.Session;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dmitriy Malakhov
 */
@Component
public class IspRasWorks extends Downloader{

    @Override
    public String getName() {
        return "Труды Института системного программирования РАН";
    }

    @Override
    public String getUrl() {
        return "http://www.ispras.ru";
    }
    
    
    
    @Override
    public void process(Session session) throws IOException, InterruptedException {
        for (String year : getAvailableYears()) {
            for (String release : getAvailableReleases(year)) {
                if(!isNewRelease(release)){
                    continue;
                }
                for(Record record : releaseDownload(release)){
                    save(session, record);
                }
                save(release);
            }
        }
    }
    
    private List<Record> releaseDownload(String release) throws IOException, InterruptedException{
        List<Record> releases = new ArrayList<>();
        Document doc = Jsoup.connect(getUrl() + "/proceedings/" + release + "/").get();
        for(Element el : doc.select(".main-inform a")){
            String href = el.attr("href");
            if(href.endsWith("recent-issues.php") || href.endsWith(".pdf")){
                continue;
            }
            releases.add(getRecord(Jsoup.connect(getUrl() + href).get()));
            Thread.sleep(1000);
        }
        return releases;
    }
    
    protected Record getRecord(Document doc) throws IOException{
        Record record = new Record();
        record.setType(Type.PAPER);
        
        record.setUrl(doc.select("a.link-labour").attr("href"));
        record.setTitle(doc.select("h2.publ").text());
        record.setAnnotation(doc.select("p.prev_text").text());
        String tags = doc.select("p.tags").text();
        if(!tags.isEmpty()){
            record.setKeyWords(Arrays.asList(tags.split(";")));
        }
        String[] parts = doc.select("p.izdanie").text().split(",");
        record.setSource(parts[0]);
        record.setLocation("Москва");
        record.setYear(parts[3]);

        if(parts.length == 5){
            record.setPages((parts[1] + "," + parts[2] + "," + parts[4]).trim());
            record.setYear(parts[3].trim());
        }else{
            record.setPages((parts[1] + "," + parts[3]).trim());
            record.setYear(parts[2].trim());
        }
        record.setDoi(doc.select("p.doi").text());
        record.setAuthors(getAuthors(doc.select("p.authors").html()));
        return record;
    }
    

    private List<String> getAvailableReleases(String year) throws IOException{
        List<String> releases = new ArrayList<>();
        Document doc = Jsoup.connect("http://www.ispras.ru/proceedings/" + year + "/").get();
        for(Element el : doc.select(".main-inform a")){
            releases.add(el.attr("href").split("/")[2]);
        }
        Collections.sort(releases);
        return releases;
    }
    
    private List<String> getAvailableYears() throws IOException{
        List<String> years = new ArrayList<>();
        Document doc = Jsoup.connect(getUrl() + "/proceedings/all-archives.php").get();
        for(Element el : doc.select("a.link_publication")){
            years.add(el.attr("href").split("/")[2]);
        }
        return years;
    }
    
    protected List<Author> getAuthors(String str){
        if(str.isEmpty()){
            return new ArrayList<>();
        }
        String[] lems = str.replace(".", " ").replace(",", " , ").replace("(", " ( ").replace(")", " ) ").replace("<", " <").replace(">", "> ").replaceAll("\\s+", " ").trim().split(" ");
        Author author = new Author();
        List<Author> authors = new ArrayList<>();
        int level = 0;
        for (int i = 0; i < lems.length; i++) {
            if(lems[i].equals("(")){
                level++;
                continue;
            }
            if(lems[i].equals(")")){
                level--;
                continue;
            }
            if(level != 0){
                if(lems[i].equals(",") && author.getLocation() == null){
                    author.setLocation("");
                }else if(author.getLocation() != null){
                    if(author.getLocation().isEmpty()){
                        author.setLocation(lems[i]);
                    }else{
                        author.setLocation(author.getLocation() + (Character.isLetter(lems[i].charAt(0)) ? " " : "") + lems[i]);
                    }
                }else{
                    if(author.getOrganization() == null){
                        author.setOrganization(lems[i]);
                    }else{
                        author.setOrganization(author.getOrganization() + (Character.isLetter(lems[i].charAt(0)) ? " " : "") + lems[i]);
                    }
                }
            }else{
                if(lems[i].equals(",") || lems[i].equals("<br>")){
                    if(author.getSurname() != null){
                        authors.add(author);
                        author = new Author();
                    }
                    continue;
                }
                if(author.getName() == null){
                    if(!Character.isLetter(lems[i].charAt(0))){
                        continue;
                    }
                    if(lems[i].length() == 1 && Character.isLetter(lems[i].charAt(0))){
                        author.setName(lems[i] + ".");
                        while(lems[i + 1].length() == 1 && !Character.isLetter(lems[i + 1].charAt(0))){
                            i++;
                        }
                        if(lems[i + 1].length() == 1 && Character.isLetter(lems[i + 1].charAt(0))){
                            author.setName(author.getName()  + lems[i + 1] + ".");
                            i++;
                        }
                        continue;
                    }
                    author.setName("");
                    author.setSurname(lems[i]);
                    continue;
                }
                if(author.getSurname() == null){
                    author.setSurname(lems[i]);
                }else{
                    author.setSurname(author.getSurname() + " " + lems[i]);
                }
            }
        }
        authors.add(author);
        return authors;
    }
    
    public static void main(String[] args) throws IOException {
        
        FileUtils.write(new File("src/test/resources/IspRasWorks/isp_29_2017_1_39"), Jsoup.connect("http://www.ispras.ru/proceedings/isp_29_2017_1/isp_29_2017_1_39/").get().html(), "utf-8");
        IspRasWorks ispRasWorks = new IspRasWorks();
        List<Author> authors = ispRasWorks.getAuthors("И.Б. Бурдонов, А.С. Косачев.");
        System.out.println(new ObjectMapper().writeValueAsString(authors));
    }
}
