package Task1;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.naming.directory.SearchControls;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {

//        makeIndex();
//        List<String> businessIdList = searchInBusinessIndex("businessIndex");
//        makeFiles("reviewIndex", businessIdList);


//        makeCategoryVsBusinessIdMap();

//        String pathToReviewDirectory = "/home/jay/trainCategory";

//        POSIndex malletIndex = new POSIndex(pathToReviewDirectory,"POSMalletIndex");

//        malletIndex.makeIndex();

//        System.out.println("Mallet Index ban gaya");

        searchInIndex();

    }

    private static void searchInIndex() {
        List<String> tags = new ArrayList<>(Arrays.asList("NN", "NNS", "NNPS", "NNP", "JJ", "POS", "FW"));

        String str = "Ray Wold was with the Ringling Brothers & Barnum & Bailey Circus for several years, meaning that he attended the world famous \"Clown College\" run by \"The Greatest Show on Earth\"®™ and learned his craft well.  He's a juggler, magician, stand-up comedian, singer, knife thrower, whip cracker, balancing artist, unicyclist, illusionist and all-around-terrific entertainer.";

        POSTagger posTagger = new POSTagger();
        String pos = null;
        try {
            pos = posTagger.tag(str, (ArrayList<String>) tags);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Search search = new Search("malletAndReviewIndex");

        String query = pos;
        System.out.println("---------------------");
        search.findHits("review", query);
        System.out.println("------------------------");
        search.findHits("pos", query);

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
            searchInReview.findHits("", text);

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
