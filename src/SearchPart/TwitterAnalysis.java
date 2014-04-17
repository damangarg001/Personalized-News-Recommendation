package SearchPart;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class TwitterAnalysis {
	public static Queue<String> newsData = new LinkedList<String>();
	/*
	public static void showTweet(){
		for(String temp : UserTopTenTweets.tw){
			System.out.println(temp);
		}
	}
	*/
	public void analyze(String userName){
		
		newsData.clear();
		UserTopTenTweets user = new UserTopTenTweets();
		try {
			user.analyze(userName);
			//System.out.println("-----------------Tweets-----------------------");
			//showTweet();
			//System.out.println("----------------------------------------------");
			
			File file =new File ("src/SearchPart/script.sh");
			String ScriptPath = file.getAbsolutePath();
			//System.out.println(ScriptPath);
			String[] cmd = new String[3];
			cmd[0] = "bash";
			cmd[1] = ScriptPath;
			cmd[2] = userName;
			
			
			// create runtime to execute external command
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(cmd);
			 
			// retrieve output from script
			BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
			
			String line = "";
			while((line = bfr.readLine()) != null) {
			// display each output line form  script
			newsData.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*
	 public static void main(String[] args){
		TwitterAnalysis twAnalysis = new TwitterAnalysis();
		twAnalysis.analyze("kapil_chhajer");
	}
	 */
}
