#/usr/bin/python
from bs4 import BeautifulSoup
from table_parser import *
import urllib, urllib2, json
import string
import sys

global_min = 100000000000
global_list = []
global_edit_min = 100000000000
# initialize this for every new search

def substCost(x,y):
	if x == y: 
		return 0
	else:
		return 2

def minEditDistR(word1, word2):
	len_1=len(word1)
	len_2=len(word2)
	x =[[0]*(len_2+1) for _ in range(len_1+1)]
	for i in range(0,len_1+1):
		x[i][0]=i
	for j in range(0,len_2+1):
		x[0][j]=j
	for i in range (1,len_1+1):
		    for j in range(1,len_2+1):
		    	if word1[i-1]==word2[j-1]:
			    x[i][j] = x[i-1][j-1] 
		    	else :
			    x[i][j]= min(x[i][j-1],x[i-1][j],x[i-1][j-1])+1

	return x[i][j]







def extract_data(lst,query):
	global global_min
	global global_list
	global global_edit_min
	l = len(query)
	dic = {}
	for i in range(l):
		dic[query[i]]=0
	dis = 0
	
	newlst=[]
	for x in lst:
		if(len(re.compile(r'[\s\w-]+').findall(x))>0):
			newlst.append(re.compile(r'[\s\w-]+').findall(x)[0])
	lst = [x for x in newlst if len(x)>0] 
	lst = [x for x in lst if not str.isdigit(x)] 
	for words in lst:
		if(words == ""):
			continue
	 	flag = 0
		for needle in query:
			for needles in needle.split(' '):
				if(len(needles)<=1):
					continue;
				if(needles[-1] == 's'):
					needles = needles[0:-1]	
				try: 
					p = re.search(needles.lower(),words.lower())
				except:
					p = False

				if p:
					dis += minEditDistR(needle.lower(),words.lower())
					dic[needle]=1
					flag = 1
					break
			if(flag == 1):
			  	break
	for key in dic.keys():
		if dic[key] != 1:
			return
	if dis < global_edit_min and len(lst)>3:
	 	global_edit_min = dis
		global_list = lst
	elif dis == global_edit_min:
		if len(lst) < global_min and len(lst)>3:
			global_list = lst
			global_min = len(lst)
	return 

def search_google(query):
#	proxy = urllib2.ProxyHandler({'http': 'http://proxy.iiit.ac.in:8080/'})
#	opener = urllib2.build_opener(proxy)
#	urllib2.install_opener(opener)
	url="http://www.google.co.in/search?q="
	for token in query.split():
		url+="+";
		url+=token;
	user_agent = 'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.7) Gecko/2009021910 Firefox/3.0.7'
	headers={'User-Agent':user_agent,} 
	req = urllib2.Request(url,None,headers)
	response = urllib2.urlopen(req)
	html = response.read()
	parsed_html = BeautifulSoup(html,'html.parser')
	resultLinks=[]
	for link in parsed_html.body.findAll('h3'):
		resultLink = link.find('a')['href'].split("=")[1].split("&")[0]
		if "http://" in resultLink and "wikipedia" in resultLink:
			resultLinks.append(resultLink)
	for link in parsed_html.body.findAll('h3'):
		resultLink = link.find('a')['href'].split("=")[1].split("&")[0]
		if "http://" in resultLink and "wikipedia" not in resultLink:
			resultLinks.append(resultLink)
	return resultLinks #array containing resulting wikipeida links	

def extract2(data,query):
	soup = BeautifulSoup(data,'html.parser')
	for table in soup.findAll('tr'):
		for each in table.find_all('td'):
			
			p = each.get_text().encode('utf-8').strip('\n').split('\n')
			extract_data(p,query)
			for rows in each.find_all('ul'):
				p =  rows.get_text().encode('utf-8').strip('\n').split('\n')
				extract_data(p,query)

	for rows in soup.find_all('ul'):
		p =  rows.get_text().encode('utf-8').strip('\n').split('\n')
		extract_data(p,query)
	lis = []
	rows = soup.find_all('span',attrs={'class':"toctext"})
	for each in rows:
		lis.append(each.get_text().encode('utf-8'))
	extract_data(lis,query)
		
	return	


def main():
#	print "HIII",
#	f = "".join(sys.argv[1:])
	# f1 = open("input.txt","r").read().split('\n')
	f1 = []
	f1.append(sys.argv[1]);
#	f2 = open("output.txt,"w")
#	f1 = []
#	f1.append(f)
#	sys.stdout.flush()
#	return
	for each in f1:
		if len(each)==0:
			continue;

		query = [x.lower().strip(' ') for x in each.split(',')]

		
	 	quer = "list "
	 	for el in query:
			quer +=el.lower()
	 		quer+=" "
		quer += " wikipedia"
	 	urls = search_google(quer)
		i=0;
	 	while(True):
			global global_min
			global global_list
			global global_edit_min
#print url
		 	try:
				req = urllib2.Request(urls[i])
				response = urllib2.urlopen(req)
		 		data = response.read()
			except:
				i+=1
				if(len(global_list)>0 or len(urls)<=i):
					for each in global_list:
						f2.write(each)
					print global_list,
					break
				else:	
					continue
			
			global_min =  100000000000
			global_list = []
			global_edit_min = 100000000000
			extract2(data,query)	
			i+=1
			if(len(global_list)>0 or len(urls)<=i):
#				for each in global_list:
#					f2.write(each)
				# print global_list,
				for item in global_list:
					print item
				break
if __name__=='__main__':
	main()
	
