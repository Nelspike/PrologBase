#include "List.h"
#include "mpi.h"
#include "omp.h"

using namespace std;

void printbincharpad(char c)
{
    for (int i = 7; i >= 0; --i) {
        cout << ( (c & (1 << i)) ? '1' : '0' );
    }
    cout << endl;
}

NumberList::NumberList(unsigned long long defSize) {
	realSize = (unsigned long long) defSize;
	size = (unsigned int)((defSize/2)/8) + (unsigned int)((defSize/2)%8);
	numbers = (char*) calloc (size, sizeof(char));
}

void NumberList::unmarkedNumbers(vector<unsigned long long> &vec) {
	vec.clear();
	for(unsigned long long i = 3; i < realSize; i+=2) {
		unsigned long long trueElem = (i-1)/2;
		unsigned long long currentElemPos = (unsigned long long) trueElem/8;

		short int toShift = 0;
		if(trueElem%8 != 0)
			toShift = 8-(trueElem%8);

		if(!( numbers[currentElemPos] & (1 << toShift)))
			vec.push_back(i);
	}
}

void NumberList::markMultiplesDivision(unsigned long long elem) {
	unsigned long long elemMult = elem*elem;
	for(unsigned long long i = elemMult; i < realSize; i+=2) {
		if(i%elem == 0) markNumber(i);
	}
}

void NumberList::markMultiplesByMultiples(unsigned long long elem) {
	unsigned long long elemMult = elem*elem;
	for(unsigned long long i = elemMult; i < realSize; i+=(elem*2))
		markNumber(i);
}

void NumberList::markMultiplesDivisionOpenMP(unsigned long long elem) {
	unsigned long long elemMult = elem*elem;

	#pragma omp for
	for(unsigned long long i = elemMult; i < realSize; i+=2) {
		if(i%elem == 0) markNumber(i);
	}
}

void NumberList::markMultiplesByMultiplesOpenMP(unsigned long long elem) {
	unsigned long long elemMult = elem*elem;

	#pragma omp for
	for(unsigned long long i = elemMult; i < realSize; i+=(elem*2))
		markNumber(i);
}

void NumberList::blockMultiples(unsigned long long blockSize) {
	unsigned long long k = 3;
	unsigned long long start;
	unsigned long long cycleCount = 0;

	for(unsigned long long i = k*k; i < realSize; i+=blockSize) {
		unsigned long long realSizeCycle = i+blockSize;
		if(realSizeCycle > realSize) realSizeCycle = realSize;
		while(k*k <= realSizeCycle) {
			if(cycleCount == 0) start = k*k;
			else {
				if(i%k != 0) start = i + (k - (i%k));
				else start = i;
			}

			if(start%2 == 0) start+=k;
		
			for(unsigned long long j = start; j < realSizeCycle; j+=(k*2))
				if(j >= i) markNumber(j);

			k = findNextUnmarked(k);
		}
		k = 3;
		cycleCount++;
	}
}

void NumberList::blockMultiplesOpenMP(unsigned long long blockSize) {
	unsigned long long k = 3;
	unsigned long long start;
	unsigned long long cycleCount = 0;

	#pragma omp for private(start, k)
	for(unsigned long long i = k*k; i < realSize; i+=blockSize) {
		unsigned long long realSizeCycle = i+blockSize;
		if(realSizeCycle > realSize) realSizeCycle = realSize;
		while(k*k <= realSizeCycle) {
			if(cycleCount == 0) start = k*k;
			else {
				if(i%k != 0) start = i + (k - (i%k));
				else start = i;
			}

			if(start%2 == 0) start+=k;
		
			#pragma omg for
			for(unsigned long long j = start; j < realSizeCycle; j+=(k*2))
				if(j >= i) markNumber(j);

			k = findNextUnmarked(k);
		}
		k = 3;
		cycleCount++;
	}
}

void NumberList::blockMultiplesMPI(unsigned long long blockSize, int process, int nProc) {
		MPI_Status status;
        unsigned long long k = 3;
		unsigned long long length = (realSize/nProc);
        unsigned long long start = process*length, innerStart;
        unsigned long long cycleCount = 0;
        unsigned long long finalPos = start + length;
		char* aux = new char[size];

        if(process != 0) {
                if(start%k != 0) start += (k - (start%k));
				cycleCount = 1;
        }
        else {
            start = k*k;
        }

        for(unsigned long long i = start; i < finalPos; i+=blockSize) {
			//if(process != 0) MPI_Recv( numbers, size, MPI_CHAR, process-1, 0, MPI_COMM_WORLD, &status );
                unsigned long long realSizeCycle = i+blockSize;
                if(realSizeCycle > finalPos) realSizeCycle = finalPos;
                while(k*k <= realSizeCycle) {
                        if(cycleCount == 0) innerStart = k*k;
                        else {
                                if(i%k != 0) innerStart = i + (k - (i%k));
                                else innerStart = i;
                        }

                        if(innerStart%2 == 0) innerStart+=k;
                
                        for(unsigned long long j = innerStart; j < realSizeCycle; j+=(k*2))
                                if(j >= i) markNumber(j);

                        k = findNextUnmarked(k);
                }
                k = 3;
                cycleCount++;
			//if(process != nProc-1) { 
				for(unsigned long long i = 0; i < (size/nProc); i++)
					aux[i] = numbers[i];

				MPI_Barrier(MPI_COMM_WORLD);
				MPI_Bcast(numbers, size, MPI_CHAR, 0, MPI_COMM_WORLD);

				for(unsigned long long i = 0; i < (size/nProc); i++)
					numbers[i] = numbers[i] bitor aux[i];
				//MPI_Send( numbers, size, MPI_CHAR, process+1, 0, MPI_COMM_WORLD );
			//}
        }

		delete aux;
}

void NumberList::markNumber(unsigned long long elem) {
	unsigned long long trueElem = (elem-1)/2;
	unsigned long long pos = (unsigned long long) (trueElem)/8;

	short int toShift = 0;
	if(trueElem%8 != 0)
		toShift = 8-(trueElem%8);

	char toSet = numbers[pos] | (1 << toShift);
	numbers[pos] = toSet;
}

unsigned long long NumberList::findNextUnmarked(unsigned long long currentElement) {
	currentElement += 2;
	unsigned long long ret = 0;
	
	for(unsigned long long i = currentElement; i < realSize; i+=2) {
		unsigned long long trueElem = i/2;
		unsigned long long currentElemPos = (unsigned long long) trueElem/8;

		short int toShift = 0;
		if(trueElem%8 != 0)
			toShift = 8-(trueElem%8);

		if(!( numbers[currentElemPos] & (1 << toShift))) {
			ret = i;
			break;
		}
	}


	return ret;
}
