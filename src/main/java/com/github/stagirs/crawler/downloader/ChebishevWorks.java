package com.github.stagirs.crawler.downloader;
import com.github.stagirs.crawler.Downloader;
import com.github.stagirs.crawler.model.business.Author;
import com.github.stagirs.crawler.model.business.Record;
import com.github.stagirs.crawler.model.service.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by nikit on 13.02.2018.
 */
@Component
public class ChebishevWorks extends Downloader {
    @Override
    public String getName() {
        return "Чебышевский сборник";
    }

    @Override
    public String getUrl() {
        return "http://www.chebsbornik.ru/jour/issue/archive";
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
        Document doc = Jsoup.connect(getUrl()).get();
        for (Element el : doc.select("a[href*=/issue/view]")) {
            if (!(el.childNode(0).toString().contains("№") && (el.childNode(0).toString().contains("20"))))
                continue;
            releases.add(el.childNode(0).toString());
        }
        return releases;
    }

    private List<Record> releaseDownload(String releaseName) throws IOException, InterruptedException{
        List<Record> releases = new ArrayList<>();
        Document doc = Jsoup.connect(getUrl()).get();
        for(Element el : doc.select("a[href*=/issue/view]")){
            if(!(el.childNode(0).toString().contains(releaseName)))
                continue;
            Document doc1 =Jsoup.connect(el.attr("href")).get();
            for(Element el1: doc1.select("a[href*=/article/view]")) {
                if(el1.childNode(0).toString().contains("PDF"))
                    continue;
                String recordurl=el1.attr("href");
                releases.add(getRecord(recordurl));
            }
        }

        return releases;
    }

    protected Record getRecord(String url) throws IOException {

        Record record = new Record();
        record.setType(Record.Type.PAPER);
        Document doc=Jsoup.connect(url).get();
        if(doc.select("#articleFullText").size()!=0)
            record.setUrl(doc.select("#articleFullText").first().childNode(3).attr("href"));
        record.setTitle(doc.select("h1").text());
        System.out.println(record.getTitle());
        if(doc.select("#articleAbstract").first().childNode(5).childNode(0).toString().contains("<p>"))
            record.setAnnotation(doc.select("#articleAbstract").first().childNode(5).childNode(0).childNode(0).toString());
        else
            record.setAnnotation(doc.select("#articleAbstract").first().childNode(5).childNode(0).toString());
        record.setSource("Чебышевский сборник");
        record.setLocation("Москва");
        List<Author>authorlist = new ArrayList<>();
        Elements auth=doc.select("#authorString");
        if(!(auth.get(0).childNode(0).childNodes().isEmpty())){
            ArrayList<String> authors= new ArrayList<String>();
            for(int l=0;l<auth.first().childNode(0).childNodes().size();l++)
            {
                if(l%2==0)
                    continue;
                authors.add(auth.first().childNode(0).childNode(l).childNode(0).toString());
            }
            for(int j=0;j<authors.size();j++)
            {
                if(authors.get(j).length()<2)
                    continue;
                String[] temp=authors.get(j).substring(1).split(" ");
                if(temp.length<2) {
                    Author a = new Author(temp[0], " ");
                    authorlist.add(a);
                }
                else{
                    Author a;
                    if(temp.length==2)
                        a= new Author(temp[1],temp[0]);
                    else
                        a= new Author(temp[2],temp[0]+" "+temp[1]);
                    authorlist.add(a);}

            }
            record.setAuthors(authorlist);
        }
        String year=doc.select("#breadcrumb").first().childNode(3).childNode(0).toString();
        record.setYear(year.substring(year.length()-5,year.length()-1));
        Elements doi=doc.select("a[href*=dx.doi.org]");
        if (doi.size()!=0)
            record.setDoi(doc.select("a[href*=dx.doi.org]").get(0).childNode(0).toString());
        //record.setAuthors(getAuthors(doc.select("p.authors").html()));
        return record;
    }
}
