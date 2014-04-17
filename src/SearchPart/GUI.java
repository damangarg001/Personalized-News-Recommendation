package SearchPart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class GUI extends JFrame{
	
	//Screen Dimensions
	private double width;
	private double height;
	
	private JFrame myFrame;
	private JPanel topPanel;
	private JPanel tweetPanel;
	private JPanel newsPanel;
	private ScrollPane upperCenterPanel;
	private ScrollPane lowerCenterPanel;
	//private JPanel bottompanel;
	
	private JLabel  bgTopPanel;
	private JLabel label;
	private JTextField textBox;
	private JLabel []tweetLabel;
	private JLabel []newsLabel;
	//private JLabel msgLabel;
	private JButton submitButton;
	private MyHandler mHandler;
	
	
	

	public void createFrame(){
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "Recent Tweets"); 
		TitledBorder title1 = BorderFactory.createTitledBorder(lowerEtched, "Recommended News"); 
		//Border lineBorder = BorderFactory.createLineBorder(Color.black);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		
		int calHeight,calWidth,temp;
		mHandler  = new MyHandler();
		
		myFrame = new JFrame("Personalized News Recommendation Based On Twitter Activity");
		myFrame.setLayout(null);
		myFrame.setSize((int)width,(int)height);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
		
		//GridBagConstraints gbc = new GridBagConstraints();
		
		topPanel = new JPanel();
		tweetPanel = new JPanel();
		newsPanel = new JPanel();
		upperCenterPanel = new ScrollPane();
		lowerCenterPanel = new ScrollPane();
		tweetPanel.setBorder(title);
		tweetPanel.setAutoscrolls(true);
		newsPanel.setBorder(title1);
		tweetPanel.setLayout(null);
		newsPanel.setLayout(null);
		//size
		
		
		label=new JLabel("Twitter User Screen Name");
		textBox = new JTextField(50);
		tweetLabel = new JLabel[10];
		bgTopPanel = new JLabel();
		newsLabel = new JLabel[10];
		submitButton = new JButton("submit");
		
		ImageIcon ic = new ImageIcon("/home/kapil/twitter.png");
		bgTopPanel.setSize((int)width,(int)(0.06*height));
		bgTopPanel.setLayout(new FlowLayout());
		bgTopPanel.setIcon(ic);
		bgTopPanel.add(label);
		bgTopPanel.add(textBox);
		bgTopPanel.add(submitButton);
		topPanel.add(bgTopPanel);
		
		for(int i=0;i<10;i++){
			tweetLabel[i] = new JLabel("");
			tweetLabel[i].setSize((int) width , 20);
			tweetLabel[i].setLocation(10,i*30+20);
			
			newsLabel[i] = new JLabel("");
			newsLabel[i].setSize((int) width , 20);
			newsLabel[i].setLocation(10,i*30+20);
			
			tweetPanel.add(tweetLabel[i]);
			newsPanel.add(newsLabel[i]);
		}
		
		/*
		 * Location Setting
		 */
		
		calHeight = (int)(height * 0.06) ;
		calWidth = (int)width;
		temp=calHeight;
		topPanel.setBackground(Color.GRAY);
		topPanel.setSize(calWidth, calHeight);
		topPanel.setLocation(0, 0);
		
		
		calHeight = (int)(height * 0.47) ;
		upperCenterPanel.setBackground(Color.BLACK);
		upperCenterPanel.setSize(calWidth, calHeight);
		upperCenterPanel.setLocation(0,temp);
		
		
		
	
		lowerCenterPanel.setBackground(Color.LIGHT_GRAY);
		lowerCenterPanel.setSize(calWidth, calHeight);
		lowerCenterPanel.setLocation(0,temp+calHeight);
		upperCenterPanel.add(tweetPanel);
		lowerCenterPanel.add(newsPanel);
		myFrame.add(topPanel);
		myFrame.add(upperCenterPanel);
		myFrame.add(lowerCenterPanel);
		
		
		
		submitButton.addActionListener(mHandler);
	}
	
	
	private class MyHandler implements ActionListener{
		
		private TwitterAnalysis ut;
		public void actionPerformed(ActionEvent event){
			
			if(event.getSource()==submitButton){
				String userId=textBox.getText();
				if(!userId.equals("")){
					textBox.setText("");
					ut = new TwitterAnalysis();
					ut.analyze(userId);
				
					
					int i,num=UserTopTenTweets.tw.size();
					for(i=0;i<10;i++){
						String temp="";
						if(i<num)
							temp=UserTopTenTweets.tw.poll();
						tweetLabel[i].setText(temp);
						
					}
					i=0;
					for(String temp : TwitterAnalysis.newsData){
						newsLabel[i].setText(temp);
						final String t=temp;
						newsLabel[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
						
						MouseListener[] arrMouseListeners = newsLabel[i].getMouseListeners();
						
						for ( MouseListener m : arrMouseListeners ) {
							newsLabel[i].removeMouseListener(m);
						}
						
						
				        newsLabel[i].addMouseListener(new MouseAdapter() {
				            @Override
				            public void mouseClicked(MouseEvent e) {
				                    try {
				                            Desktop.getDesktop().browse(new URI(t));
				                    } catch (URISyntaxException | IOException ex) {
				                            //It looks like there's a problem
				                    }
				            }
				        });
						i++;
					}
					
				}
				else{
					JOptionPane.showMessageDialog(null,"Text Box is empty");
				}
			}
		}
		/*
		public MyHandler() {
			//
			// TODO Auto-generated constructor stub
		}
		*/
		  
		
	}
	
	public static void main(String[] args){
		GUI gui=new GUI();
		gui.createFrame();	
	}

}

