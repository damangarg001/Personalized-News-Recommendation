package SearchPart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;


public class UserTopTenTweets {
	public static Queue<String> tw = new LinkedList<String>();
	public  void analyze(String args) throws IOException {
	
		tw.clear();
		String username=args;

		File file =new File ("src/SearchPart/twitter.py");
		String pythonScriptPath = file.getAbsolutePath();
		
		//StopWord stp = new StopWord();
		String[] cmd = new String[3];
		cmd[0] = "python2.7";
		cmd[1] = pythonScriptPath;
		cmd[2] = username;
		
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		 
		// retrieve output from python script
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
		
		String readLine = "";
		while((readLine = bfr.readLine()) != null) {
		// display each output line form python script
		System.out.println(readLine);
		}
		
		//
		boolean tweetFlag=false,tweetStartFlag=true;
		String fileName =username,temp="",line;
		int count=0;
		try{
			// extracting the 10 latest tweets
			Queue<String> queue = new LinkedList<String>();
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				if(line.equals("<tweet>")){
					tweetFlag=false;
					tweetStartFlag=true;
					continue;
				}
				else if(line.equals("</tweet>")){
					if(temp!=""){
						queue.add(temp);
						temp="";
					}
					continue;
				}
			 	else if(count>9){
					break;
				}
				else{
						if(tweetStartFlag==true){
							tweetStartFlag=false;
							if(tweetFlag==false && !line.trim().toLowerCase().startsWith("rt")){
								tweetFlag=true;
								count++;
							}
						}
						
						if(tweetFlag==true)
							temp=temp+line+" ";
						
				}
			}
			br.close();

			// writing last 10 tweets into a file
			try {
				/*
				 * Set expansion code is placed below in comment section
				 */
				/*
				file =new File ("src/SearchPart/final.py");
				String pyScriptPath = file.getAbsolutePath();
				System.out.println(pyScriptPath);
				PrintWriter out = new PrintWriter(fileName);
				String[] c = new String[3];
				c[0] = "python2.7";
				c[1] = pyScriptPath;
				
				while (!queue.isEmpty()) {
					String l = queue.remove();
					
					String []split = l.split("\\s+");
					int len=split.length;
					for(int i=0;i<len;i++){
						String term=split[i];
						//System.out.println(term);
						out.print("| "+term+"->");
						if(stp.stopListFun(term)){
							c[2]=term;
							Runtime r = Runtime.getRuntime();
							Process p = r.exec(c);
							 
							// retrieve output from python script
							BufferedReader bfreader = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
							line = "";
							
							while((line = bfreader.readLine()) != null) {								
								out.print(" "+line);
							}
						}
						
					}
					out.println();
	        	}
				out.close();
				*/
				
				PrintWriter out = new PrintWriter("src/SearchPart/"+fileName);
				
				
				while (!queue.isEmpty()) {
					String t=queue.remove();
					tw.add(t);
					out.println(t);
	        	}
				out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		catch (Exception e)
		{
			System.err.println(e.getMessage()); // handle exception
		}
	}
}
