package Task1;


import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainClass {

    public static void main(String[] args) {
        LuceneIndexWriter luceneIndexWriter;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please provide the path to 'Yelp Reviews' file ");
            String pathToReviewFile = bufferedReader.readLine();

            System.out.println("Please provide the path to 'Yelp Tip' file ");
            String pathToTipFile = bufferedReader.readLine();

            luceneIndexWriter = new LuceneIndexWriter(pathToReviewFile, "reviewIndex");
            parseAndMakeIndex(pathToReviewFile, luceneIndexWriter, "REVIEW");
            luceneIndexWriter.finish();

            luceneIndexWriter = new LuceneIndexWriter(pathToTipFile, "tipIndex");
            parseAndMakeIndex(pathToTipFile, luceneIndexWriter, "TIP");
            luceneIndexWriter.finish();

            readIndex("reviewIndex");
            readIndex("tipIndex");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseAndMakeIndex(String pathToJsonFile, LuceneIndexWriter luceneIndexWriter, String review) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(new File(pathToJsonFile)));
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        JSONParser jsonParser = new JSONParser();
        String line;

        int index = 1;
        while ((line = br.readLine()) != null) {
            if (jsonObjects.size() < 10000) {
                jsonObjects.add((JSONObject) jsonParser.parse(line));
            } else if (jsonObjects.size() == 10000) {
                makeIndex(index, jsonObjects, luceneIndexWriter, review);
                jsonObjects.clear();
                index++;
            }
        }
        if (jsonObjects.size() < 10000) {
            System.out.println(jsonObjects.get(jsonObjects.size() - 1));
            makeIndex(index, jsonObjects, luceneIndexWriter, review);
        }
    }

    private static void readIndex(String indexPath) {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            int totalNumberOfDocs = reader.maxDoc();
            System.out.println(totalNumberOfDocs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeIndex(int index, ArrayList<JSONObject> jsonObjects, LuceneIndexWriter luceneIndexWriter, String fieldName) {
        System.out.println(index);
        System.out.println(jsonObjects.size());

        luceneIndexWriter.createIndex(jsonObjects, fieldName);
    }
}
