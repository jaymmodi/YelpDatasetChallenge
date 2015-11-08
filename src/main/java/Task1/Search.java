package Task1;


import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class Search {


    public void readIndex(String indexPath) {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            int totalNumberOfDocs = reader.maxDoc();
            System.out.println(totalNumberOfDocs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
