package Task1;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Search {
    public IndexReader reader;
    public String indexPath;
    public IndexSearcher searcher;
    public Set<String> businessIdSet;
    public Analyzer analyzer;

    public Search(String indexPath) {
        try {
            this.indexPath = indexPath;
            this.reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            this.searcher = new IndexSearcher(reader);
            this.businessIdSet = new HashSet<>();
            this.analyzer = new StandardAnalyzer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readIndex() {
        int totalNumberOfDocs = reader.maxDoc();
        System.out.println(totalNumberOfDocs);
    }

    public void makeFiles(List<String> businessIdList) {

        for (String id : businessIdList) {

            StringBuilder fullText = new StringBuilder();
            try {
                Query query = getQuery(id);

                int count = getCount(query);
                if (count > 0) {
                    TopDocs docs = searcher.search(query, count);

                    ScoreDoc[] hits = docs.scoreDocs;

                    makeText(fullText, hits);

                    writeToFile(fullText, id);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeText(StringBuilder fullText, ScoreDoc[] hits) throws IOException {
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            fullText.append(doc.get("REVIEW"));
        }
    }

    private Query getQuery(String id) {
        Term term = new Term("business_id", id);
        return new TermQuery(term);
    }

    private int getCount(Query query) throws IOException {
        TotalHitCountCollector totalHitCountCollector = new TotalHitCountCollector();
        searcher.search(query, totalHitCountCollector);

        return totalHitCountCollector.getTotalHits();
    }


    private void writeToFile(StringBuilder fullText, String id) {
        String workingDirectory = System.getProperty("user.dir");
        File file = new File(workingDirectory + "/reviewFiles/" + id + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);

            fileWriter.write(fullText.toString());
            System.out.println("Written to file");
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeSetBusinessIds(String business_id) {
        businessIdSet.add(business_id);
    }


    public void findHits(String text) {

        System.out.println(text);
        QueryParser parser = new QueryParser("REVIEW", new StandardAnalyzer());
        try {
            Query query = parser.parse(QueryParser.escape(text));
            this.searcher.setSimilarity(new BM25Similarity());

            TopDocs results = searcher.search(query, 10);

            ScoreDoc[] hits = results.scoreDocs;

            for (int i = 0; i < hits.length; i++) {
                Document doc = searcher.doc(hits[i].doc);
                System.out.println(doc.get("business_id"));
            }
//            printToFile(hits, count);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> makeUniqueIdList() {
        List<LeafReaderContext> leafReaderContexts = reader.getContext().leaves();
        System.out.println(leafReaderContexts.size());

        List<String> businessIdList = new ArrayList<>();
        for (LeafReaderContext leafReaderContext : leafReaderContexts) {

            int startDoc = leafReaderContext.docBase;
            int numberDocs = leafReaderContext.reader().maxDoc();
            for (int i = startDoc; i < numberDocs; i++) {
                try {
                    String business_id = searcher.doc(i).get("business_id");
                    businessIdList.add(business_id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return businessIdList;
    }
}
