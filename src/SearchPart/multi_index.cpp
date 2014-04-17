#include <bits/stdc++.h>

using namespace std;
#define SZ(V) (long long )V.size()
#define ALL(V) V.begin(), V.end()
#define RALL(V) V.rbegin(), V.rend()
#define FORN(i, n) for(i = 0; i < n; i++)
#define FORAB(i, a, b) for(i = a; i <= b; i++)
#define PB push_back  
#define MP make_pair
#define MOD 1000000007LL

typedef long long LL;

LL length(LL a)
{
	if(a==0) return 1;
	LL res=0,p=a;
	while(a>0)
	{
		a/=10;
		res++;
	}
	// cout << p << " : " << res << endl;
	return res;
}
string to_lower(string a)
{
	LL i,ss=SZ(a);
	FORN(i,ss) if(a[i]>='A' && a[i]<='Z') a[i]=a[i]-'A' + 'a';
	return a;
}
int main(int argc,char *argv[])
{
	if(argc!=2) {
		cout << " Format : ./a.out index_file"; return 0;
	}
	string str,keyword,data;
	LL i,n,ctr=0,ctr2=0,iter=0;
	ifstream fp;
	fp.open("src/SearchPart/"+argv[1]);
	FILE *fp1,*fp2;
	fp1=fopen("src/SearchPart/sec_index.txt","w");
	fp2=fopen("src/SearchPart/ter_index.txt","w");
	while(fp >> str) 
	{
		n=SZ(str);
		FORN(i,n) if(str[i]==':')break;
		keyword=str.substr(0,i);
		data = str.substr(i+1,n);
		keyword = to_lower(keyword);
		data=to_lower(data);
		if(iter%1000==0) fprintf(fp2, "%s %lld\n",keyword.c_str(),ctr2 ); 
		fprintf(fp1,"%s %lld\n",keyword.c_str(),ctr);
		ctr2 +=SZ(keyword) + length(ctr) + 2;
		ctr += SZ(keyword) + SZ(data) + 2;
		// cerr << keyword  << " : " << SZ(keyword ) << " + " << ctr <<  " : " << length(ctr)  << endl	;
		iter++;	
	}
	cout << "Done!" << endl;
	fp.close();
	fclose(fp1);
	fclose(fp2);
	return 0;
}
