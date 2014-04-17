package IndexPart;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class Xml {
		String dirpath;
		Xml(String dPath){
			this.dirpath=dPath;
		}
		public void run(String inputFolder,String outputFolder) throws SAXException, IOException{
			String iPath=this.dirpath+"/"+inputFolder+"/";
			File []file = finder(iPath);
			new File(this.dirpath+"/Index").mkdir();
			String path=this.dirpath+"/"+outputFolder+"/";
			XmlParse.filePath=path;
			XmlParse.dirPath=this.dirpath;
			
			for(File f : file){
			XmlParse.fileName=f.getName();
			 XMLReader readObj = XMLReaderFactory.createXMLReader();
			  readObj.setContentHandler(new XmlParse());
			  readObj.parse(f.getAbsolutePath());
			}
		    
			File dir=new File(path);
			int noOfFiles=dir.listFiles().length;
			//System.out.println("number of Files "+noOfFiles);
			
			Merge(noOfFiles,"OutResult",null);
			noOfFiles=(int)(Math.ceil(noOfFiles/1000.0));
			Merge(6,"FinalIndex","OutResult");
			
			
		}
		
		public void Merge(int noOfFiles,String outputFileStartName, String inputFileStartName ) throws IOException{
			
			int filenumber=1;
			int slot=(int)(Math.ceil(noOfFiles/1000.0));
			int currentSlot=0;

			while(currentSlot<slot){
				int numberOfFilesPerSlot=1000;
				if(currentSlot+1==slot){
					numberOfFilesPerSlot=noOfFiles-currentSlot*1000;
				}
				currentSlot++;
				BufferedReader[] br=new BufferedReader[numberOfFilesPerSlot+1] ;
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File(XmlParse.filePath+outputFileStartName+currentSlot)));
				if(inputFileStartName==null){
					for(Integer x=1;x<=numberOfFilesPerSlot;x++){
						br[x]=new BufferedReader(new FileReader(new File(XmlParse.filePath+filenumber)));
						filenumber++;
					}
				}
				else{
					System.out.println("Here"+ numberOfFilesPerSlot);
					for(Integer x=1;x<=numberOfFilesPerSlot;x++){
						br[x]=new BufferedReader(new FileReader(new File(XmlParse.filePath+inputFileStartName+filenumber)));
						filenumber++;
					}
				}
				
				
				TreeMap<String,PriorityQueue<Integer>> tree = new TreeMap<String,PriorityQueue<Integer>>();
				TreeMap<String,ArrayList<Integer>> map = new TreeMap<String,ArrayList<Integer>>();
				ArrayList<Integer> list;
				ArrayList<Integer> lNode;
				PriorityQueue<Integer> p;
				PriorityQueue<Integer> qNode;
				boolean flag;
				
				for(Integer x=1;x<=numberOfFilesPerSlot;x++){
					flag=false;
					while(flag==false){
							String line=br[x].readLine();
							if(line==null){
								flag=true;
								continue;
							}
							String []split=line.split(":");
							try{
								Float.parseFloat(split[0]);
							}catch(NumberFormatException e){
								//System.out.println("float :"+split[0]+split[1]+"      "+line+"  "+x);
								flag=true;
							}
							
							if(flag==true){
									String key=split[0].toString();
									String []num=split[1].split(",");
									
									if(!tree.containsKey(key)){
										p=new PriorityQueue<Integer>();
										for(int k=0;k<num.length;k++){
											int s=Integer.parseInt(num[k]);
											p.add(s);	
										}
										tree.put(key, p);
										list = new ArrayList<Integer>();
										list.add(x);
										map.put(key, list);
									}
									else{
										p=tree.get(key);
										list=map.get(key);
										list.add(x);
										for(int k=0;k<num.length;k++){
											int s=Integer.parseInt(num[k]);
											p.add(s);	
										}
									}
								
							}
							 		
					}
					
				}
				
				while(!tree.isEmpty()){
					String key=tree.firstKey();
					p=tree.get(key);
					tree.remove(key);
					list=map.get(key);
					map.remove(key);
					for(Integer i:list){
						flag=false;
						while(flag==false){
							String line=br[i].readLine();
							if(line!=null){
								String []split=line.split(":");
								String k=split[0].toString();
								String []num=split[1].split(",");
								if(key.equals(k)){
									
								}
								else if(!tree.containsKey(k)){
									qNode=new PriorityQueue<Integer>();
									for(int l=0;l<num.length;l++){
										int s=Integer.parseInt(num[l]);
										qNode.add(s);	
									}
									tree.put(k, qNode);
									lNode = new ArrayList<Integer>();
									lNode.add(i);
									map.put(k, lNode);
								}
								else{
									qNode=tree.get(k);
									lNode=map.get(k);
									for(int l=0;l<num.length;l++){
										int s=Integer.parseInt(num[l]);
										qNode.add(s);	
									}
									lNode.add(i);
									flag=true;
								}
							}
							else{
								flag=true;
							}
						}
					}
					bw.write(key+":");
					boolean j=false;
					for(Integer i:p){
						if(j==false){
							bw.write(i.toString());
							j=true;
						}
						else{
							bw.write(","+i.toString());
						}
					}
					bw.write("\n");
				}
				bw.close();
				for(Integer x=1;x<=numberOfFilesPerSlot;x++){
					br[x].close();
				}
			}
			
		}
		public static File[] finder( String dirName){
	    	File dir = new File(dirName);

	    	return dir.listFiles(new FilenameFilter() { 
	    	         public boolean accept(File dir, String filename)
	    	              { return filename.endsWith(".xml"); }
	    	} );

	    }
		
		
}
