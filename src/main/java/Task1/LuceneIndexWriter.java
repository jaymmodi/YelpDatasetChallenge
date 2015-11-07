package Task1;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;

public class LuceneIndexWriter {

    Analyzer analyzer;
    String jsonFilePath;
    String indexPath;
    IndexWriter indexWriter;
    Directory dir;
    IndexWriterConfig iwc;

    public LuceneIndexWriter(String jsonFilePath, String indexPath) {
        try {
            this.indexPath = indexPath;
            this.jsonFilePath = jsonFilePath;
            this.dir = FSDirectory.open(Paths.get(indexPath));
            this.analyzer = new StandardAnalyzer();
            this.iwc = new IndexWriterConfig(analyzer);
            this.iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            this.indexWriter = new IndexWriter(dir, iwc);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createIndex(ArrayList<JSONObject> jsonObjects) {
        for (JSONObject jsonObject : jsonObjects) {
            addJSONObject(jsonObject);
        }
    }


    private void addJSONObject(JSONObject jsonObject) {
        Document document = new Document();
        Set set = jsonObject.keySet();
        try {

            for (Object key : set) {
                Class type = jsonObject.get(key).getClass();
                Object value = jsonObject.get(key);

                if (type.equals(String.class) && key.toString().equals("type") && value.toString().equals("review")) {
                    document.add(new TextField("REVIEW", jsonObject.get("text").toString(), Field.Store.NO));
                    document.add(new StringField("business_id", value.toString(), Field.Store.YES));
                }
            }

            indexWriter.addDocument(document);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        try {
            indexWriter.forceMerge(1);
            indexWriter.commit();
            indexWriter.close();
            System.out.println("Done");
        } catch (IOException e) {
            System.out.println("We had a problem closing the index: " + e.getMessage());
        }
    }
}
