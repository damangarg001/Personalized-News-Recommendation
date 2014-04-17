package IndexPart;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import org.xml.sax.SAXException;


public class Index {

		public void execute(String path){
			File dir = new File(path+"/dataSet/");
			int noOfFiles=dir.listFiles().length;
			StopWord stp = new StopWord();
			new File(path+"/data/").mkdir();
			Stemmer stem=new Stemmer();
			System.out.println(noOfFiles);
			for(int i=1;i<=noOfFiles;i++){
				try {
					BufferedReader br = new BufferedReader(new FileReader(path+"/dataSet/"+i));
					String readLine;
					TreeMap<String,Integer> map=new TreeMap<String,Integer>();
					
					readLine = br.readLine();
					
					while(readLine!=null){
						String []split=readLine.split(" ");
						int length=split.length;
						
						for(int j=0;j<length;j++){
							String temp=split[j];
							if(stp.stopListFun(temp)){
								stem.add(temp.toCharArray(),temp.length());
								stem.stem();
								temp=stem.toString();
								if(!map.containsKey(temp)){
									map.put(temp,i);
								}
							}
						}
						readLine=br.readLine();
					}
					br.close();
					BufferedWriter  bw = new BufferedWriter(new FileWriter(path+"/data/"+i));
					while(!map.isEmpty()){
					 String temp = map.firstKey();
					 bw.write(temp+":"+map.get(temp)+"\n");
					 map.remove(temp);
					}
					bw.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Xml obj = new Xml(path);
			try {
				obj.run("dataSet", "data");
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
