#include "Parser.h"
#include <cstdlib>
using namespace std;

string parseString(int &n,string s) {
	string ret;
	for (;n<s.size() && s[n]!=';';n++) {
		if (s[n]!='\"') {ret.append(1,s[n]);}
	}
	n++;
	return ret;
}

double parseDouble(int &n,string s) {
	string ret;
	for (;n<s.size() && s[n]!=';';n++) {
		if (s[n]==',') {ret.append(1,'.');}
		else if (s[n]!='\"') {ret.append(1,s[n]);}
	}
	n++;
	return atof(ret.c_str());
}

int parseInt(int &n,string s) {
	string ret;
	for (;n<s.size() && s[n]!=';';n++) {
		if (s[n]!='\"') {ret.append(1,s[n]);}
	}
	n++;
	return atoi(ret.c_str());
}
