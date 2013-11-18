#include "Codec.h"
#include "ArvoreBinaria.h"
#include <vector>
#include <string>
#include <fstream>
#include <iostream>
#include <algorithm>
#include <sstream>
#include <bitset>
using namespace std;

int binaryConversion(int k)
{
	return bitset<8>(k).to_ulong();
}

string toString(int k)
{
	stringstream s;
	s << k;
	return s.str();
}

int toBinary(int num)
{
	int ret = 0;
	while (num>0) {
		ret*=10;
		ret+=num%2;
		num=num>>1;
	}
	return ret;

}

vector<int> CodecLZW::comprimir()
{
	vector<int> result;
	int dictSize = 256;

	fstream file;
	file.open(filename.c_str());
	string entry = "";
	string temp;
	while (getline(file,temp))
	{
		entry += temp;
		entry +='\n';
	}
	entry +='\0';

	string w;
	for (string::const_iterator it = entry.begin(); it != entry.end(); ++it)
	{
		char c = *it;
		string wc = w + c;
		if (dictionaryCompress.count(wc))
		  w = wc;
		else
		{
		  result.push_back(dictionaryCompress[w]);
		  dictionaryCompress[wc] = dictSize;
		  dictSize += 1;
		  w = string(1, c);
		}
	}

	if (!w.empty())
		result.push_back(dictionaryCompress[w]);

	size = dictSize;
	return result;
}

string CodecLZW::decomprimir()
{
	for (map<string,int>::iterator it = dictionaryCompress.begin(); it != dictionaryCompress.end(); it++)
	{
	    dictionaryDecompress[(*it).second] = (*it).first;
	}
	ifstream file;
	file.open(filename.c_str(),ios_base::binary);
	vector<unsigned int> aux;

	unsigned short int tmp = 1;

	while(tmp != 0)
	{
		file.read((char*)&tmp,sizeof(short int));
		aux.push_back(tmp);
	}

	string result = "";
	for (vector<unsigned int>::iterator it = aux.begin(); it != aux.end(); it++)
	{
		int index = (*it);
		string corres = dictionaryDecompress[index];
		result += corres;
	}

	return result;
}
