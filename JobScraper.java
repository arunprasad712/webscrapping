
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JobScraper {

    public static void main(String[] args) {
        System.out.println("Put some skills that you are not familiar with:");
        try (Scanner scr = new Scanner(System.in)) {
            String unfamiliarSkill = scr.nextLine();

            while (true) {
                findJobs(unfamiliarSkill);
                int timeWait = 10;
                System.out.println("Waiting " + timeWait + " minutes...");
                try {
                    TimeUnit.MINUTES.sleep(timeWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void findJobs(String unfamiliarSkill) {
        try {
            String url = "https://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=Web+Application+Developer&txtLocation=";
            Document document = Jsoup.connect(url).get();
            Elements jobs = document.select("li.clearfix.job-bx.wht-shd-bx");

            for (int index = 0; index < jobs.size(); index++) {
                Element job = jobs.get(index);
                String publishedDate = job.select("span.sim-posted span").text();

                if (publishedDate.contains("few")) {
                    String companyName = job.select("h3.joblist-comp-name").text().replaceAll("\\s+", "");
                    String skills = job.select("span.srp-skills").text().replaceAll("\\s+", "");
                    String moreInfo = job.select("header h2 a").attr("href");

                    if (!skills.contains(unfamiliarSkill)) {
                        String fileName = "posts/" + index + ".txt";
                        try (FileWriter fileWriter = new FileWriter(fileName)) {
                            fileWriter.write("Company Name: " + companyName.trim() + "\n");
                            fileWriter.write("Required Skills: " + skills.trim() + "\n");
                            fileWriter.write("More Info: " + moreInfo + "\n");
                            fileWriter.write("Published Date: " + publishedDate + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("File saved: " + index);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
