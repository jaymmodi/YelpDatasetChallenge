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
        System.out.println("Please provide the path to 'Yelp Reviews' file ");

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String pathToJsonFile = bufferedReader.readLine();
            LuceneIndexWriter luceneIndexWriter = null;

            if (pathToJsonFile.contains("review")) {
                luceneIndexWriter = new LuceneIndexWriter(pathToJsonFile, "reviewIndex");
            } else if (pathToJsonFile.contains("tip")) {
                luceneIndexWriter = new LuceneIndexWriter(pathToJsonFile, "tipIndex");
            }


            parseAndMakeIndex(pathToJsonFile, luceneIndexWriter);

            luceneIndexWriter.finish();
            readIndex();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseAndMakeIndex(String pathToJsonFile, LuceneIndexWriter luceneIndexWriter) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(new File(pathToJsonFile)));
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        JSONParser jsonParser = new JSONParser();
        String line;

        int index = 1;
        while ((line = br.readLine()) != null) {
            if (jsonObjects.size() < 10000) {
                jsonObjects.add((JSONObject) jsonParser.parse(line));
            } else {
                makeIndex(index, jsonObjects, luceneIndexWriter);
                jsonObjects.clear();
                index++;
            }
        }
    }

    private static void readIndex() {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("reviewIndex")));
            int totalNumberOfDocs = reader.maxDoc();
            System.out.println(totalNumberOfDocs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeIndex(int index, ArrayList<JSONObject> jsonObjects, LuceneIndexWriter luceneIndexWriter) {
        System.out.println(index);

        luceneIndexWriter.createIndex(jsonObjects);
    }
}
