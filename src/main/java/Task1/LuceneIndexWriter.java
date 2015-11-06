package Task1;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class LuceneIndexWriter {

    String jsonFilePath;
    String indexPath;
    IndexWriter indexWriter;

    public LuceneIndexWriter(String jsonFilePath, String indexPath) {
        this.jsonFilePath = jsonFilePath;
        this.indexPath = indexPath;
        openIndex();
    }


    public void createIndex(JSONObject jsonObject) {
        addJSONObject(jsonObject);
    }

    private void openIndex() {
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(dir, iwc);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addJSONObject(JSONObject jsonObject) {
        Document document = new Document();
        Set set = jsonObject.keySet();

        for (Object key : set) {
            Class type = jsonObject.get(key).getClass();
            String value = (String) jsonObject.get(key);

            if (type.equals(String.class) && key.toString().equals("review")) {
                document.add(new StringField(key.toString(), value, Field.Store.NO));
            }
        }
    }

    public void finish() {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            System.out.println("We had a problem closing the index: " + e.getMessage());
        }
    }
}
