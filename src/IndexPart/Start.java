package IndexPart;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xml.sax.SAXException;


public class Start {
	public static void main(String[] args) throws IOException, SAXException{
		if(args.length==2){
			
			String path=args[0];
			String nlpLinbraryPath=args[1];
			
			String keyword="thehindu.com"; //this keyword should always in url
			String Url = "http://www.thehindu.com";
			News obj= new News(path,keyword,Url);
			try {
				obj.run();
			} catch (IOException e) {
				System.out.println("Error : Start.java for obj.run() ");
			}
			
			File file =new File ("script.sh");
			String ScriptPath = file.getAbsolutePath();
			System.out.println(ScriptPath);
			String[] cmd = new String[4];
			cmd[0] = "bash";
			cmd[1] = ScriptPath;
			cmd[2] = path+"/dataSet";
			cmd[3] = nlpLinbraryPath;		
			// create runtime to execute external command
			Runtime rt = Runtime.getRuntime();
			Process pr =rt.exec(cmd);
			BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));
			
			String readLine = "";
			while((readLine = bfr.readLine()) != null) {
			// display each output line form python script
			System.out.println(readLine);
			}
			Xml xmlObj = new Xml(path);
			xmlObj.run("dataSetXML","Index");
			
			
			
		}
		else{
			System.out.println("Run Format : java Start <Path where Index is going to store> <Path of CoreNLP library>");
			
		}
		
	}
}
