package IndexPart;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


public class XmlParse extends DefaultHandler {
	static TreeMap<String,ArrayList<String>> dict=new TreeMap<String,ArrayList<String>>() ;
	TreeMap<String,Integer> NamedEntity=new TreeMap<String,Integer>() ;
	StringBuilder buffer = new StringBuilder();
	StringBuilder tempBuffer = new StringBuilder();
	StringBuilder databuffer = new StringBuilder();
	static String fileName;
	static Integer fileId;
	static String filePath;
	static String dirPath;
	static boolean nerFlag,wordFlag;
	public void startDocument(){
		fileId=Integer.parseInt(fileName.substring(0,fileName.indexOf('.')));
		nerFlag=false;
		wordFlag=false;
	}
	
	public void endDocument(){
		try {
			ArrayList<String> node;
			TreeMap<String,Integer> Name=new TreeMap<String,Integer>() ;
			BufferedReader br = new BufferedReader(new FileReader(dirPath+"/dataSetInfo/"+fileName.substring(0, fileName.indexOf('.'))));
			br.readLine();
			String readLine=br.readLine();
			StopWord stop=new StopWord();
			Stemmer stemObj=new Stemmer();
			while(readLine!=null){
				String []split=readLine.split("\\P{Alpha}+");
				int size=split.length;
					
				for(int i=0;i<size;i++){
					String strToken=split[i];
					if(strToken.length()>1 && strToken.charAt(0)>=97 && strToken.charAt(0)<=122  ){
						if(stop.stopListFun(strToken)){
							stemObj.add(strToken.toLowerCase().toCharArray(), strToken.length());
							stemObj.stem();
							strToken=stemObj.toString();
							strToken=strToken.toLowerCase()+"-t";
							if(!NamedEntity.containsKey(strToken)){
								NamedEntity.put(strToken,fileId);
							}
						}
					}
				}
				readLine=br.readLine();
			}
			br.close();
			
			File file =new File ("final.py");
			String pythonScriptPath = file.getAbsolutePath();
			int len=NamedEntity.size();
			System.out.println(len);
			int l=1;
			for(String key :NamedEntity.keySet()){
				System.out.println(l);
				l++;
				String []split=key.split("-");
				
				Integer value=NamedEntity.get(key);
				//NamedEntity.remove(key);
				String temp=split[0];
				stemObj.add(temp.toLowerCase().toCharArray(), temp.length());
				stemObj.stem();
				temp=stemObj.toString();
				temp=temp+"-"+split[1];
				Name.put(temp,value);
				if(!XmlParse.dict.containsKey(split[0])){
						String[] cmd = new String[3];
						cmd[0] = "python2.7";
						cmd[1] = pythonScriptPath;
						cmd[2] = split[0];
						// create runtime to execute external command
						Runtime rt = Runtime.getRuntime();
						Process pr = rt.exec(cmd);
						 
						// retrieve output from python script
						System.out.println("hi....");
						BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
						System.out.println("hi....");
						String line = "";
						node = new ArrayList<String>();
						while((line = bfr.readLine()) != null) {
						// display each output line form python script
							String token=line;
							if(stop.stopListFun(token)){
								stemObj.add(token.toLowerCase().toCharArray(), token.length());
								stemObj.stem();
								token=stemObj.toString();
								node.add(token);
								token=token+"-e";
								if(!Name.containsKey(token)){
									Name.put(token,fileId);
								}
							}
						}
						bfr.close();
						XmlParse.dict.put(split[0], node);
				}
				else{
					String tempKey=split[0];
					node=XmlParse.dict.get(tempKey);
					for(String k : node){
						if(!Name.containsKey(k)){
							Name.put(k,fileId);
						}
					}
				}
			}
			NamedEntity.clear();
			for(String key :Name.keySet()){
				Integer value=Name.get(key);
				Name.remove(key);
				NamedEntity.put(key, value);
			}
			
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath+fileName.substring(0, fileName.indexOf('.'))));
			for(String key :NamedEntity.keySet()){
				//if(!key.contains(":")){
					out.write(key+":"+NamedEntity.get(key)+"\n");
				//}
			}
			out.close();
			Name.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buffer.setLength(0);
		NamedEntity.clear();
		
	}
	public void startElement(String nameSpaceURI,String localName,String qName, Attributes atts){
		qName=qName.toLowerCase();
		switch(qName){
			case "word":
							wordFlag=true;
							nerFlag=false;
							//System.out.println("wordFlag start");
							break;
			case "ner" :
							nerFlag=true;
							wordFlag=false;
							//System.out.println("nerFlag start");
							break;
			default:
							wordFlag=false;
							nerFlag=false;
							
		}
	}
	public void endElement(String nameSpaceURI,String localName,String qName){
		qName=qName.toLowerCase();
		switch(qName){
		
		}
			
	}
	
	public void characters(char []ch ,int start ,int length){
		tempBuffer.append(ch,start,length);
		StopWord stop=new StopWord();
		Stemmer	stemObj = new Stemmer();
		if(wordFlag==true){
			databuffer=databuffer.append(tempBuffer.toString().trim());
		}
		else if(nerFlag==true && (tempBuffer.toString().trim().length()>=2) ){
			String tempData=databuffer.toString().toLowerCase();
			String []split = tempData.split("\\P{Alpha}+");	
			int splitLength=split.length;
			for(int j=0; j<splitLength;j++){
				String temp=split[j];
				if(temp.length()>1 && temp.charAt(0)>=97 && temp.charAt(0)<=122  ){
					if(stop.stopListFun(temp)){
						stemObj.add(temp.toLowerCase().toCharArray(), temp.length());
						stemObj.stem();
						temp=stemObj.toString();
						temp=temp+"-c";
						if(!NamedEntity.containsKey(temp)){
							NamedEntity.put(temp,fileId);
						}
					}
				}
			}
				
			
			databuffer.setLength(0);
		}
		else if(nerFlag==true ){
			String tempData=databuffer.toString().toLowerCase();
			String []split = tempData.split("\\P{Alpha}+");	
			int splitLength=split.length;
			for(int j=0; j<splitLength;j++){
				String temp=split[j];
				if(temp.length()>1 && temp.charAt(0)>=97 && temp.charAt(0)<=122  ){
					if(stop.stopListFun(temp)){
						stemObj.add(temp.toLowerCase().toCharArray(), temp.length());
						stemObj.stem();
						temp=stemObj.toString();
						temp=temp+"-s";
						if(!NamedEntity.containsKey(temp)){
							NamedEntity.put(temp,fileId);
						}
					}
				}
			}
			databuffer.setLength(0);
		}
		
		tempBuffer.setLength(0);
		
	}
		
}
