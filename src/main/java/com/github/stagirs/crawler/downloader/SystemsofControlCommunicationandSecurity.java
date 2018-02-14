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
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
/**
 * Created by nikit on 12.02.2018.
 */
@Component
public class SystemsofControlCommunicationandSecurity extends Downloader   {
    @Override
    public String getName() {
        return "Системы управления, связи и безопасности";
    }

    @Override
    public String getUrl() {
        return "http://sccs.intelgr.com";
    }
    @Override
    public void process(Session session) throws IOException, InterruptedException {
        for (String release : getAvailableReleases()) {
            if(!isNewRelease(release)){
                continue;
            }
            for(Record record : releaseDownload(release)){
                save(session, record);
            }
            save(release);
        }
    }


    private List<String> getAvailableReleases() throws IOException {
        List<String> releases = new ArrayList<>();
            Document doc = Jsoup.connect(getUrl()+"/arch.html").get();
            for (Element el : doc.select("a[href*=201]")) {
                if (!(el.childNode(0).toString().contains("№") && (el.childNode(0).toString().contains("20"))))
                    continue;
                releases.add(el.childNode(0).toString());
            }
        return releases;
    }

    private List<Record> releaseDownload(String releaseName) throws IOException, InterruptedException{
        List<Record> releases = new ArrayList<>();
            Document doc = Jsoup.connect(getUrl()+"/arch.html").get();
            for(Element el : doc.select("a[href*=201]")){
                if(!(el.childNode(0).toString().contains(releaseName)))
                    continue;
                Document doc1 =Jsoup.connect(getUrl()+"/"+el.attr("href")).get();
                for(Element el1: doc1.select("li:has(div.artname)")) {
                    if(el1.childNode(0).toString().contains("PDF"))
                        continue;
                    releases.add(getRecord(el1,releaseName.substring(3)));
                }
            }

        return releases;
    }

    protected Record getRecord(Element el1,String year) throws IOException {

        Record record = new Record();
        record.setType(Type.PAPER);
        int firstrun=0;
        for(int i=0;i<11;i++)
        {
            if(el1.childNode(i).toString().contains("artname"))
            {
                firstrun=i;
                break;
            }

        }
        record.setUrl(el1.childNode(0+firstrun).childNodes().get(1).attr("href"));
        record.setTitle(el1.childNode(2+firstrun).childNodes().get(0).toString());
        record.setSource("Системы управления, связи и безопасности");
        record.setLocation("Санкт-Петербург");
        List<Author>authorlist = new ArrayList<>();
            String[] authors=el1.childNode(0+firstrun).childNodes().get(0).toString().split(",");
            for(int j=0;j<authors.length;j++)
            {
                if(j==0)
                {
                    String[] temp=authors[j].split(" ");
                    if(temp.length<2) {
                        Author a = new Author(temp[0], " ");
                        authorlist.add(a);
                    }
                    else{
                        Author a;
                        if(temp.length==2)
                            a= new Author(temp[0],temp[1]);
                        else
                            a= new Author(temp[0],temp[1]+" "+temp[2]);
                        authorlist.add(a);}
                }
                else {
                    if(authors[j].length()<2)
                        continue;
                    String[] temp=authors[j].substring(1).split(" ");
                    if(temp.length<2) {
                        Author a = new Author(temp[0], " ");
                        authorlist.add(a);
                    }
                    else{
                        Author a;
                        if(temp.length==2)
                            a= new Author(temp[0],temp[1]);
                        else
                            a= new Author(temp[0],temp[1]+" "+temp[2]);
                        authorlist.add(a);}
                }

            }
            record.setAuthors(authorlist);

        record.setYear(year);
        //record.setAuthors(getAuthors(doc.select("p.authors").html()));
        return record;
    }
//    public static void main(String[] args) throws IOException, InterruptedException {
//
//        SystemsofControlCommunicationandSecurity ispRasWorks = new SystemsofControlCommunicationandSecurity();
//        System.out.println(SystemsofControlCommunicationandSecurity.getAvailableReleases());
//        System.out.println(releaseDownload("№1 2017"));
//    }
}
