package Task1;


import org.apache.lucene.codecs.LiveDocsFormat;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.MultiFields;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {

//        makeIndex();
//        List<String> businessIdList = searchInBusinessIndex("businessIndex");
//        makeFiles("reviewIndex", businessIdList);


        makeCategoryVsBusinessIdMap();
    }

    private static List<String> searchInBusinessIndex(String businessIndex) {
        Search searchInReview = new Search(businessIndex);
        return searchInReview.makeUniqueIdList();
    }

    private static void makeIndex() {
        LuceneIndexWriter luceneIndexWriter;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please provide the path to file to index ");
            String pathToFile = bufferedReader.readLine();
//            String pathToFile = "yelp_academic_dataset_business.json";
            luceneIndexWriter = new LuceneIndexWriter(pathToFile, "businessIndexWithCategories");
            parseAndMakeIndex(pathToFile, luceneIndexWriter, "business_id");
            luceneIndexWriter.finish();

//            String pathToTestFile = "/media/jay/New Volume/Jay/IUB/Fall_2015/Search/Final Project/yelp_dataset_challenge_academic_dataset/yelp_dataset_challenge_academic_dataset/yelp_dataset_challenge_academic_dataset/test.json";
//            parseTestSet(pathToTestFile);


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void makeCategoryVsBusinessIdMap() {
        String pathToBusinessFile = "businessIndexWithCategories";

        Search search = new Search(pathToBusinessFile);
        search.readIndex();
        List<String> categoryList = search.makeUniqueCategoryList();
        search.makeCategoryBusinessIdMap(categoryList);


    }


    private static void parseTestSet(String pathToTestFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pathToTestFile)));
            JSONParser jsonParser = new JSONParser();
            String line = br.readLine();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(line);

            String text = (String) jsonObject.get("text");
            Search searchInReview = new Search("trainSet");
            searchInReview.findHits(text);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private static void makeFiles(String indexPath, List<String> businessIdList) {
        Search searchInReview = new Search(indexPath);
        searchInReview.makeFiles(businessIdList);

//        Search searchInTip = new Search("tipIndex");
//        searchInTip.readIndex();
    }

    private static void parseAndMakeIndex(String pathToJsonFile, LuceneIndexWriter luceneIndexWriter, String review) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(new File(pathToJsonFile)));
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        String line;

        int index = 1;
        while ((line = br.readLine()) != null) {
            if (jsonObjects.size() < 10000) {
                jsonObjects.add((JSONObject) jsonParser.parse(line));
            } else if (jsonObjects.size() == 10000) {
                makeIndex(index, jsonObjects, luceneIndexWriter, review);
                jsonObjects.clear();
                jsonObjects.add((JSONObject) jsonParser.parse(line));
                index++;
            }
        }
        if (jsonObjects.size() < 10000) {
            makeIndex(index, jsonObjects, luceneIndexWriter, review);
        }
    }

    private static void makeIndex(int index, ArrayList<JSONObject> jsonObjects, LuceneIndexWriter luceneIndexWriter, String fieldName) {
        System.out.println(index);
        System.out.println(jsonObjects.size());

        luceneIndexWriter.createIndex(jsonObjects, fieldName);
    }
}
