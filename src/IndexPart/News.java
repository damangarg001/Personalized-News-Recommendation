package IndexPart;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 *  News.java crawls sites pages 
 *  Here we are crawling www.theHindus.com
 *  dataSet folder contain article
 *  dataSetInfo folder contains article title and topic information and OutConfig file  
 */
public class News {
	String path;
	String keyword;
	String url;
	News(String path,String keyword,String url){
		this.path=path;
		this.keyword=keyword;
		this.url=url;
	}

	
 public void run() throws IOException{
	 System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
	 System.setProperty("http.proxyPort", "8080");
	 System.setProperty("https.proxyHost", "proxy.iiit.ac.in");
	 System.setProperty("https.proxyPort", "8080");
	 new File(path+"/dataSetInfo").mkdir();
	 new File(path+"/dataSet").mkdir();
	 Document doc;	
	 HashSet<String> urlHashSet = new HashSet<String>();
	 Queue<String>  urlQueue = new LinkedList<String>();
	 urlHashSet.add(url);
	 urlQueue.add(url);
	 int fileName=1;
	BufferedWriter outputConfig = new BufferedWriter(new FileWriter(path+"/dataSetInfo/"+"OutConfig"));
	while(!urlQueue.isEmpty() && fileName <5653){
		 try {
			Boolean flag=false;
			BufferedWriter out = new BufferedWriter(new FileWriter(path+"/dataSet/"+fileName));
			BufferedWriter outInfo = new BufferedWriter(new FileWriter(path+"/dataSetInfo/"+fileName));
			String pageUrl=urlQueue.poll();
			//System.out.println(pageUrl);
			doc=Jsoup.connect(pageUrl).get();
			Elements urls=doc.select("a");
			for(Element url : urls ){
				String term=url.attr("href");
				if(!term.contains(".png") && !term.contains(".gif") && !term.contains(".jpg") && !term.contains(".ico") &&  !term.contains(".css") && !term.contains("#comment") && !term.contains("facebook.com")  && !term.toLowerCase().contains("video") && !term.toLowerCase().startsWith("http://epaper.thehindu.com/") && !term.toLowerCase().startsWith("http://m.thehindu.com") && !term.toLowerCase().startsWith("http://tamil.thehindu.com")  && !term.toLowerCase().contains("twitter.com") && term.toLowerCase().startsWith("http") && term.contains(keyword)){
					String temp=term;
					if(term.contains("?")){
						 temp = term.substring(0,term.indexOf("?"));
					}
					if(!urlHashSet.contains(temp)){
						//System.out.println(term);
						urlHashSet.add(temp);
						urlQueue.add(temp);
					}
				}
			}
			String locDate= doc.getElementsByClass("dateline").text();
			outInfo.write(locDate);
			outInfo.write("\n");
			String articleTitle=doc.getElementsByClass("detail-title").text();
			outInfo.write(articleTitle);
			outInfo.write("\n");
			Elements topics=doc.getElementsByClass("cat");
			
			for(Element topic:topics){
				outInfo.write(topic.text());
				outInfo.write("\n");
			}
			Elements paragraphs=doc.getElementsByClass("body");
			if(paragraphs.size()>0){
				flag=true;
			}
			for(Element paragraph:paragraphs){
				out.write(paragraph.text());
				out.write("\n");
			}
			if(flag==true){
				outputConfig.write(pageUrl);
				outputConfig.write("\n");
				fileName++;
			}
			 out.close();
			 outInfo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	outputConfig.close();
 }
}
