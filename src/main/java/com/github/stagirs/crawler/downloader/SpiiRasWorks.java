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
 * Created by nikit on 27.12.2017.
 */
@Component
public class SpiiRasWorks extends Downloader {
    @Override
    public String getName() {
        return "Труды СПИИРАН";
    }

    @Override
    public String getUrl() {
        return "http://www.proceedings.spiiras.nw.ru/ojs/index.php/sp/issue/archive";
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
        for (int i = 1; i <= 3; i++) {
            Document doc = Jsoup.connect("http://www.proceedings.spiiras.nw.ru/ojs/index.php/sp/issue/archive" + "?issuesPage=" + i + "#issues").get();
            for (Element el : doc.select("a[href*=/issue/view]")) {
                if (!(el.childNode(0).toString().contains("Труды СПИИРАН")))
                    continue;
                releases.add(el.childNode(0).toString());
            }
        }
        return releases;
    }

    private List<Record> releaseDownload(String releaseName) throws IOException, InterruptedException{
        List<Record> releases = new ArrayList<>();
        for(int i=1;i<=3;i++) {
            Document doc = Jsoup.connect("http://www.proceedings.spiiras.nw.ru/ojs/index.php/sp/issue/archive" + "?issuesPage=" + i + "#issues").get();
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
        }

        return releases;
    }

    protected Record getRecord(String url) throws IOException {

        Record record = new Record();
        record.setType(Type.PAPER);
        Document doc=Jsoup.connect(url).get();
        record.setUrl(url);
        record.setTitle(doc.select("h3").text());
        record.setAnnotation(doc.select("#articleAbstract").first().childNode(5).childNode(0).toString());
        String[] parts = doc.select("#biblographyLink").get(0).childNode(3).toString().split("//")[1].replace(".","").replace("- ","-").split(" ");
        record.setSource("Труды СПИИРАН");
        record.setLocation("Санкт-Петербург");
        List<Author>authorlist = new ArrayList<>();
        Elements auth=doc.select("#authorString");
        if(!(auth.get(0).childNode(0).childNodes().isEmpty())){
        String[] authors=auth.first().childNode(0).childNode(0).toString().split(",");
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
                    Author a= new Author(temp[1],temp[0]);
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
                Author a= new Author(temp[1],temp[0]);
                authorlist.add(a);}
            }

        }
        record.setAuthors(authorlist);
        }
        String[] pages=parts[parts.length-1].split("-");
        record.setPages((parts[5] + "," + pages[0] + "," + pages[1]).trim());
        record.setYear(parts[3].trim());

        record.setDoi(doc.select("a[href*=dx.doi.org]").get(0).childNode(0).toString());
        //record.setAuthors(getAuthors(doc.select("p.authors").html()));
        return record;
    }

}


