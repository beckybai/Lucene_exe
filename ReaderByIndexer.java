package test1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
 
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
 
public class ReaderByIndexer {
 
    public static void main(String[] args) throws Exception {
//        args = new String[] { 
//                "-index", 
//                "F:\\lucene-4.10.2\\index"
//            };
 //       if (args.length > 0
 //               && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
 //           System.exit(0);
 //       }
 
        String index = "index";
        String field = "contents";
        String queries = null;
        int repeat = 0;
        boolean raw = false;
        String queryString = null;
        int hitsPerPage = 20000;
        
        index = "D:\\Becky\\ws\\index5";
 
        Directory reader = FSDirectory.open(new File(index));
        IndexSearcher searcher = new IndexSearcher(reader);
        // :Post-Release-Update-Version.LUCENE_XY:
		IKAnalyzer analyzer = new IKAnalyzer();
	//	IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, analyzer ); 
        
        // 读入
        BufferedReader in = null;
		String encoding = "GBK";
        in = new BufferedReader(new InputStreamReader(System.in,
		        encoding));
        // :Post-Release-Update-Version.LUCENE_XY:
        QueryParser parser = new QueryParser(Version.LUCENE_35,"contents",analyzer);
        while (true) {
            if (queries == null && queryString == null) { // prompt the user
                System.out.println("输入查询关键字: ");
            }
 
            String line = queryString != null ? queryString : in.readLine();
 
            if (line == null || line.length() == -1) {
                break;
            }
 
            line = line.trim();
            if (line.length() == 0) {
                break;
            }
 
            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));
            
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, raw);
            //		.create(hitsPerPage);
            searcher.search(query, collector);
           ScoreDoc[] hits = collector.topDocs().scoreDocs;
           
           // 4. display results
            System.out.println("Found " + hits.length + " hits.");
   //         for(int i=0;i<hits.length;++i) {
   //         	int docId = hits[i].doc;
   //             Document d = searcher.doc(docId);
   //             System.out.println((i + 1) + ". " + d.get("contents") + "\t" );
  //              }
            //读入结束
 //			doPagingSearch(in, searcher, query, hitsPerPage, raw,
 //                   queries == null && queryString == null);
 
            if (queryString != null) {
                break;
            }
        }
        reader.close();
    }
 
    public static void doPagingSearch(BufferedReader in,
            IndexSearcher searcher, Query query, int hitsPerPage, boolean raw,
            boolean interactive) throws IOException {
 
        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;
 
        int numTotalHits = results.totalHits;
        System.out.println(numTotalHits + " total matching documents");
 
        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);
    }
}