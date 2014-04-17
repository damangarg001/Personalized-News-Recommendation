#include <bits/stdc++.h>

using namespace std;
#define CLR(a) memset(a, 0, sizeof(a))
#define ABS(X) ( (X) > 0 ? (X) : ( -(X) ) )
#define SZ(V) (long long )V.size()
#define ALL(V) V.begin(), V.end()
#define RALL(V) V.rbegin(), V.rend()
#define FORN(i, n) for(i = 0; i < n; i++)
#define FORAB(i, a, b) for(i = a; i <= b; i++)
#define si(n) scanf("%d",&n)
#define ss(s) scanf("%s",s)
#define prin(n) printf("%d\n",n)
#define pll pair < long long int, long long int >
#define pii pair < int, int >
#define psi pair < string, int >
#define PB push_back  
#define MP make_pair
#define F first
#define S second
#define MOD 1000000007LL

typedef long long LL;

int main(int argc,char *argv[])
{
	LL i,j,k;
	string str="aaa";
	FORN(k,26)
	{
		str[0]=k+'a';
		str[1]='a';
		str[2]='a';
		FORN(j,26)
		{
			str[1]=j+'a';
			str[2]='a';
			FORN(i,26)
			{
				str[SZ(str)-1]='a'+i;
				cout << str << "-c" << ":" << rand()%1000 << "," << rand()%1000 << "," << rand()%1000 << endl;
				cout << str << "-t" << ":" << rand()%1000 << "," << rand()%1000 << "," << rand()%1000 << endl;
			}
		}
	}
	return 0;
}