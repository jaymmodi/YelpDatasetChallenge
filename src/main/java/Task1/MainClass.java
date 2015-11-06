package Task1;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainClass {

    public static void main(String[] args) {
        String pathToJsonFile = "/media/jay/New Volume/Jay/IUB/Fall_2015/Search/Final Project/yelp_dataset_challenge_academic_dataset/yelp_dataset_challenge_academic_dataset/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_review.json";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pathToJsonFile)));
            ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
            JSONParser jsonParser = new JSONParser();
            String line;

            while ((line = br.readLine()) != null) {
                if (jsonObjects.size() < 10000) {
                    jsonObjects.add((JSONObject) jsonParser.parse(line));
                } else {
                    makeIndex(jsonObjects, pathToJsonFile);
                    jsonObjects.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void makeIndex(ArrayList<JSONObject> jsonObjects, String pathToJsonFile) {
        System.out.println(jsonObjects.size());

        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(pathToJsonFile, "reviewIndex");
        luceneIndexWriter.createIndex(jsonObjects);
    }
}
