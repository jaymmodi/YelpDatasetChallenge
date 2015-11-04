package Task1;


import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainClass {

    public static void main(String[] args) {
        String pathToJsonFile = "/media/jay/New Volume/Jay/IUB/Fall_2015/Search/Final Project/yelp_dataset_challenge_academic_dataset/yelp_dataset_challenge_academic_dataset/yelp_dataset_challenge_academic_dataset/sample.json";

        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToJsonFile));
            String objPerLine = br.readLine();

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(objPerLine);
            System.out.println(obj);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
