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
 * Created by nikit on 13.02.2018.
 */
@Component
public class InternationalJournalofOpenInformationTechnologies extends Downloader{
    @Override
    public String getName() {
        return "International Journal of Open Information Technologies";
    }

    @Override
    public String getUrl() {
        return "http://injoit.org/index.php/j1/issue/archive";
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
            Document doc = Jsoup.connect(getUrl() + "?issuesPage=" + i + "#issues").get();
            for (Element el : doc.select("a[href*=/issue/view]")) {
                releases.add(el.childNode(0).toString());
            }
        }
        return releases;
    }

    private List<Record> releaseDownload(String releaseName) throws IOException, InterruptedException{
        List<Record> releases = new ArrayList<>();
        for(int i=1;i<=3;i++) {
            Document doc = Jsoup.connect(getUrl() + "?issuesPage=" + i + "#issues").get();
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
        record.setTitle(doc.select("h3").text().replace("Refbacks",""));
        System.out.println(record.getTitle());
        if(doc.select("#articleFullText").size()!=0)
            record.setUrl(doc.select("#articleFullText").first().childNode(3).attr("href"));
        if(doc.select("#articleAbstract").first().childNode(5).childNode(0).toString().contains("<p>"))
            record.setAnnotation(doc.select("#articleAbstract").first().childNode(5).childNode(0).childNode(0).toString());
        else
            record.setAnnotation(doc.select("#articleAbstract").first().childNode(5).childNode(0).toString());
        record.setSource("International Journal of Open Information Technologies");
        record.setLocation("Москва");
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
                        Author a;
                        if(temp.length==2)
                            a= new Author(temp[1],temp[0]);
                        else
                            a= new Author(temp[2],temp[0]+" "+temp[1]);
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
                            a= new Author(temp[1],temp[0]);
                        else
                            a= new Author(temp[2],temp[0]+" "+temp[1]);
                        authorlist.add(a);}
                }

            }
            record.setAuthors(authorlist);
        }
        String year=doc.select("#breadcrumb").first().childNode(3).childNode(0).toString();
        record.setYear(year.substring(year.length()-5,year.length()-1));
        Elements doi=doc.select("a[href*=dx.doi.org]");
        if (doi.size()!=0)
            record.setDoi(doc.select("a[href*=dx.doi.org]").get(0).childNode(0).toString());        //record.setAuthors(getAuthors(doc.select("p.authors").html()));
        return record;
    }
//            public static void main(String[] args) throws IOException, InterruptedException {
//
//      InternationalJournalofOpenInformationTechnologies ispRasWorks = new InternationalJournalofOpenInformationTechnologies();
//       //System.out.println(InternationalJournalofOpenInformationTechnologies.getAvailableReleases());
//       System.out.println(releaseDownload("Vol 6, No 2 (2018)"));
//   }
}
