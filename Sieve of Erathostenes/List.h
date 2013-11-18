#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
#include <time.h>
#include <math.h>
#include <bitset>
#include <stdlib.h>

using namespace std;

class NumberList
{
	private:
		unsigned long long realSize;
		unsigned int size;
		char* numbers;
		vector<unsigned long long> allNumbers;

	public:
		NumberList() {size=0; realSize = 0; numbers=new char[0];}
		NumberList(unsigned long long defSize);
		~NumberList() {delete numbers;};
		void unmarkedNumbers(vector<unsigned long long> &vec);
		void markMultiplesDivision(unsigned long long elem);
		void markMultiplesByMultiples(unsigned long long elem);
		void markMultiplesDivisionOpenMP(unsigned long long elem);
		void markMultiplesByMultiplesOpenMP(unsigned long long elem);
		void blockMultiples(unsigned long long blockSize);
		void blockMultiplesOpenMP(unsigned long long blockSize);
		void blockMultiplesMPI(unsigned long long blockSize, int process, int nProc);
		void markNumber(unsigned long long elem);
		unsigned long long findNextUnmarked(unsigned long long currentElement);
		vector<unsigned long long> getAllNumbers() {return allNumbers;}
};
