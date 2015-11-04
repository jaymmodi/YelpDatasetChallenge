package Task1;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;

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

    public void finish() {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            System.out.println("We had a problem closing the index: " + e.getMessage());
        }
    }

    private void addJSONObject(JSONObject jsonObject) {

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
}
