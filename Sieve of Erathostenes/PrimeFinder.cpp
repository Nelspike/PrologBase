#include "PrimeFinder.h"


/**
 * Reduzir cache misses -> Carregar um bloco de cada vez
 */

void PrimeFinder::gatherPrimes(int method, unsigned long long size, unsigned long long blockSize) {
	NumberList *numbers = new NumberList(size);
	unsigned long long firstNumber = 3, lastNumber = size;
	
	clock_t startTime, endTime;

	if(method == 2) {
		startTime = clock();
		numbers->blockMultiples(blockSize);
	}
	else {
		startTime = clock();
		unsigned long long k2 = 0, inc = 0, sqrtLast = trunc(sqrt(lastNumber));
		for(unsigned long long k = firstNumber; k <= sqrtLast; k += inc ) {
			k2 = k*k;
			if(method == 0) numbers->markMultiplesDivision(k);
			else if (method == 1) numbers->markMultiplesByMultiples(k);
			inc = (numbers->findNextUnmarked(k))-k;
		}

	}

	endTime = clock();
	timeTaken =  (double)(endTime-startTime)/(double)CLOCKS_PER_SEC;
	delete numbers;
}

void PrimeFinder::gatherPrimesOpenMP(int method, unsigned long long size, unsigned long long blockSize) {
	NumberList *numbers = new NumberList(size);
	unsigned long long firstNumber = 3, lastNumber = size;
	
	double startTime, endTime;

	if(method == 2) {
		startTime = omp_get_wtime();
		#pragma omp parallel num_threads(8)
		{
			numbers->blockMultiplesOpenMP(blockSize);
		}
	}
	else {
		startTime = omp_get_wtime();
		unsigned long long k2 = 0, inc = 0, sqrtLast = (unsigned long long)trunc(sqrt(lastNumber));

		#pragma omp parallel
		{
			for(unsigned long long k = firstNumber; k <= sqrtLast; k += inc ) {
				k2 = k*k;
				if(method == 0) numbers->markMultiplesDivisionOpenMP(k);
				else if (method == 1) numbers->markMultiplesByMultiplesOpenMP(k);
				inc = (numbers->findNextUnmarked(k))-k;
			}
		}

	}

	endTime = omp_get_wtime();
	timeTaken =  (double)(endTime-startTime);
	delete numbers;

}

vector<unsigned long long> PrimeFinder::gatherPrimesMPI(unsigned long long size, unsigned long long blockSize, int proc, int nProc) {
	NumberList *numbers = new NumberList(size);
	vector<unsigned long long> retVec;
	
	double_t startTime, endTime;

	startTime = MPI_Wtime();
	numbers->blockMultiplesMPI(blockSize, proc, nProc);

	endTime = MPI_Wtime();
	timeTaken =  (double)(endTime-startTime);
	numbers->unmarkedNumbers(retVec);
	delete numbers;

	return retVec;

}
