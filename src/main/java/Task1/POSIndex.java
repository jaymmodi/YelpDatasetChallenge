package Task1;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jay on 11/28/15.
 */
public class POSIndex {


    public StandardAnalyzer analyzer;
    public String pathToDirectory;
    public String indexPath;
    public IndexWriter indexWriter;
    public Directory dir;
    public IndexWriterConfig iwc;
    public String pathToReviewDirectory;

    public POSIndex(String pathToReviewDirectory, String indexPath) {
        try {
            this.pathToReviewDirectory = pathToReviewDirectory;
            this.indexPath = indexPath;
            this.dir = FSDirectory.open(Paths.get(indexPath));
            this.analyzer = new StandardAnalyzer();
            this.iwc = new IndexWriterConfig(analyzer);
            this.iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            this.indexWriter = new IndexWriter(dir, iwc);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void makeIndex() {
        File file = new File(this.pathToDirectory);
        List<String> tags = new ArrayList<>(Arrays.asList("NN", "NNS", "NNPS", "NNP", "JJ", "POS", "FW"));

        if (file.isDirectory()) {
            for (File categoryFile : file.listFiles()) {

                if (categoryFile.getName().endsWith("_keys.txt")) {
                    String categoryName = categoryFile.getName().replace("_keys.txt", "");
                    System.out.println(categoryFile.getName());

                    String reviewString = getReviewString(categoryName, pathToReviewDirectory);

                    String posString = getNounsAdjectives(reviewString, tags);

                    addToLucene(categoryName, posString);
                }
            }
            finish();


        }

    }

    private String getNounsAdjectives(String reviewString, List<String> tags) {
        POSTagger posTagger = new POSTagger();
        String posString = "";

        try {
            posString = posTagger.tag(reviewString, (ArrayList<String>) tags);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return posString;
    }


    private String getReviewString(String categoryFile, String pathToReviewDirectory) {
        File file = new File(pathToReviewDirectory + File.separator + categoryFile + ".txt");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void addToLucene(String categoryName, String indexString) {

        try {
            Document document = new Document();
            document.add(new StringField("category", categoryName, Field.Store.YES));
            document.add(new TextField("posMallet", indexString, Field.Store.YES));

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
