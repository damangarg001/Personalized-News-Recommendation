#include <bits/stdc++.h>
#include "stemmer.h"
using namespace std;
#define SZ(V) (long long )V.size()
#define ALL(V) V.begin(), V.end()
#define RALL(V) V.rbegin(), V.rend()
#define FORN(i, n) for(i = 0; i < n; i++)
#define FORAB(i, a, b) for(i = a; i <= b; i++)
#define PB push_back  
#define MP make_pair
#define MOD 1000000007LL
#define no_of_tags 3

typedef pair<int,int> PII;
typedef pair<double, double> PDD;
typedef long long LL;

char input[10000000];
vector< pair<string,LL> > memory;
vector<string> query,urls;
map<string,LL> stopword;
map<LL,LL> docs_arr_content,docs_arr_title;
map<LL,LL> docs_arr[no_of_tags];
map<LL,LL> results_wt;
vector< pair<LL,LL> > relevance_titles,relevance_content;
char tag[2];
LL wt[no_of_tags];


void store_urls(char name[])
{
	string str;
	LL ptr;
	ifstream fp;
	fp.open(name);
	while(fp >> str)
		urls.PB(str);
	fp.close();
	return ;	
}

void initialize_stop_words()
{
	stopword.clear();
	string str;
	ifstream fp;
	fp.open("stopword.txt");
	while(fp >> str) stopword[str]=1;
	fp.close();
	return ;
}
LL toint(string s)
{
	LL t;
	stringstream ss ;
	ss << s;
	ss >> t;
	return t;
}
void to_lower()
{
	LL j,len=strlen(input);
	FORN(j,len) if(input[j]>='A' && input[j]<='Z') input[j]=input[j]-'A'+'a';
	return ;
}

void load_ter_file(char name[])
{
	string str;
	LL ptr;
	ifstream fp;
	fp.open(name);
	while(fp >> str >> ptr)
		memory.PB(MP(str,ptr));
	fp.close();
	return ; 
}

void query_formation()
{
	string st="";
	query.clear();
	LL init,leninput=strlen(input);
	for(init=0;init<leninput;init++)
	{
		if(input[init]==' ' || input[init]<'a' || input[init] >'z')
		{
			if(st!= "" && st!="\n" && st!=" " && SZ(st)>1)
			query.PB(st);
			st="";
		}
		else if(input[init]>='a' && input[init]<='z') st+=input[init];
	}
	if(input[leninput-1]>='a' && input[leninput-1] <='z') query.PB(st);
	return ;
}

void store_in_docs_array(string docs,LL flag,string keyword)
{
	LL i,ss=SZ(docs);
	string st="";
	FORN(i,ss)
	{
		if(docs[i]==',') {
			docs_arr[flag][toint(st)]++;
			st="";
		}
		else st+=docs[i];
	}
	docs_arr[flag][toint(st)]++;
	return ;
}

void query_pri_file(string keyword, LL offset, char pri_name[],LL flag)
{
	string docs="";
	LL i,ss;
	char temp[100000];
	FILE *fp = fopen(pri_name,"r");
	fseek(fp,offset,SEEK_SET);
	fscanf(fp,"%s",temp);
	ss = strlen(temp);
	FORN(i,ss)
		if(temp[i]==':') break;
	for(i=i+1;i<ss;i++) docs+=temp[i];
	store_in_docs_array(docs,flag,keyword);
	fclose(fp);
}

void query_sec_file(string keyword,LL position,char sec_name[],char pri_name[],LL flag)
{
	char str[100000];
	LL ctr=0,sec_offset = memory[position].second,pri_offset,offset=-1;
	FILE *fp = fopen(sec_name,"r");
	fseek(fp,sec_offset,SEEK_SET);
	while(fscanf(fp,"%s %lld",str,&pri_offset)!=EOF && ctr<1000)
	{
		ctr++;
		if(str == keyword)
		{
			offset = pri_offset;
			break;
		}
		if(str > keyword) break;
	}
	if(offset==-1) return ;
	query_pri_file(keyword,offset,pri_name,flag);
	fclose(fp);
}

void use_weights()
{
	
	LL i;
	double w[3];
	FORN(i,3) w[i]=0.0;
	FORN(i,no_of_tags){
		for(map<LL,LL>::iterator it = docs_arr[i].begin();it!=docs_arr[i].end();it++){
			w[i]+=it->second;
		}
		if(w[i]>50)
			w[i]=15/sqrt(w[i]);
		else
			w[i]=1;
	}
	FORN(i,no_of_tags){
		for(map<LL,LL>::iterator it = docs_arr[i].begin();it!=docs_arr[i].end();it++)
			results_wt[it->first] += (LL)(it->second*wt[i]*w[i]);
	}
	FORN(i,3) docs_arr[i].clear();
}

void search_query(char pri_name[],char sec_name[])
{
	string keyword="";
	LL ss = SZ(query),i,j;
	FORN(i,ss)
	{
		FORN(j,no_of_tags)
		{
			keyword = query[i];
			if(stopword.find(keyword)==stopword.end())
			{
				keyword = stemword(keyword);
				vector< pair<string,LL> >::iterator low;
				low=lower_bound(memory.begin(),memory.end(),MP(keyword+"-"+tag[j],(LL)(LONG_MAX-100)));
				LL position=low-memory.begin();
				if(position>0) position--;
				query_sec_file(keyword+"-"+tag[j],position,pri_name,sec_name,j);
			}
		}
		use_weights();
	}
}

void clear_structures()
{
	relevance_titles.clear();
	relevance_content.clear();
	docs_arr_title.clear();
	docs_arr_content.clear();
}

int main(int argc,char *argv[])
{
	//t : title and topic
	//s : term
	//c : named entity
	tag[0]='c';tag[1]='s';tag[2]='t';
	wt[0]=6;wt[1]=1;wt[2]=3;
	if(argc != 6)
	{
		cout << "FORMAT : ./a.out ter_index_file sec_index_file index_file url_file username";
		return 0;
	}
	LL n,i,j,lp;
	char c;
	initialize_stop_words();
	store_urls(argv[4]);
	load_ter_file(argv[1]);
	clear_structures();
	FILE* fp = fopen(argv[5],"r");
	while(fscanf(fp,"%[^\n]%c",input,&c)!=EOF)
	{
		query.clear();
		// FORN(i,3) docs_arr[i].clear();
		to_lower();
		query_formation();
		search_query(argv[2],argv[3]);

		// double w[3];
		// FORN(i,3) w[i]=0.0;
		// FORN(i,no_of_tags){
		// 	for(map<LL,LL>::iterator it = docs_arr[i].begin();it!=docs_arr[i].end();it++)
		// 		w[i]+=it->second;
		// 	w[i]=100/sqrt(w[i]);
		// }
		// FORN(i,no_of_tags){
		// 	for(map<LL,LL>::iterator it = docs_arr[i].begin();it!=docs_arr[i].end();it++)
		// 		results_wt[it->first] += (LL)(it->second*wt[i]*w[i]);
		// }
	}

	vector< pair<LL,LL> > display;
	for(map<LL,LL>::iterator it = results_wt.begin();it!=results_wt.end();it++)
		display.PB(MP(it->second,it->first));
	sort(RALL(display));
	FORN(i,min(10LL,SZ(display)))
	{
		cout << urls[display[i].second-1] << endl;
	}
	return 0;
}
