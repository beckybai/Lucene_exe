package test1;

import java.awt.TextField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;



public class Hello {
	public static void main(String[] args){
        args = new String[] { 
                "-index", 
                "D:\\java\\ws\\index",
                "-docs", 
                "D:\\java\\ws\\docs" };
	
	String indexPath = "D:\\Becky\\ws\\index5";
	String docsPath =  "D:\\Becky\\ws\\test1.txt";
//	String docsPath =  "D:\\java\\CNKI_journal_v2.txt";
	final File docDir = new File(docsPath);
	
	if(!docDir.exists() || !docDir.canRead() ){
		System.out.println("can not read the source document");
		System.exit(1);
	}
	
	try{
		System.out.println("build the index" );
		Directory dir = FSDirectory.open(new File(indexPath));
		IKAnalyzer analyzer = new IKAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, analyzer );
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(dir,iwc);
		indexDocs(writer, docDir);
		writer.close();
	} catch(IOException e){
		System.out.println("caught a"+e.getClass()+"\n with message: " + e.getMessage());
	}
}

	
	
static void indexDocs(IndexWriter writer, File file) throws IOException {
	if(!file.canRead()){
		return;
	}
	int ii = 0;
	if(file.isDirectory()){
		String[] files = file.list();
		if(files != null){

			for (int i =0;i < files.length; i++){
				indexDocs(writer, new File(file,files[i]));
			}
		}
	}else{
		FileInputStream fis;
		String encoding = "UTF-8";
		String totTXT = null;
		String titleTXT = null;
		try {
			fis = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(fis,encoding);
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(read);
            String lineTXT = null;
            boolean flag = false;
            int inum = 0;
            while ((lineTXT = bufferedReader.readLine()) != null) {
 //           	System.out.print(lineTXT);
            	if(lineTXT.equals("<REC>")){
            		flag = true;
            		if(totTXT!= null){
            			
            			Document doc = new Document();
//            			doc.add(new TextField("contents",new BufferedReader(new InputStreamReader(fis,StandardCharsets.UTF_8))));
            	//		String title_tmp = bufferedReader.readLine();
            	//		String e_title_tm
            			inum = inum+1;
            			System.out.println(inum);
            			doc.add(new Field("title",titleTXT,Field.Store.YES,Field.Index.ANALYZED));
            			doc.add(new Field("contents",totTXT,Field.Store.YES,Field.Index.ANALYZED));
            			writer.addDocument(doc);
            			totTXT = null;
            		}
            	}
            	else{
            		if(flag){
            			String[] tmp = lineTXT.split("=");
        //    			System.out.println(tmp[1]);
            			titleTXT = tmp[1];
            			flag = false;
            		}
            		else
            			totTXT = totTXT + lineTXT;
            	}
            }
		//	System.out.println(totTXT);	
    		} catch (FileNotFoundException fnfe){
			return;
		}
	
		}
	}
}
