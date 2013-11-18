#include <vector>
#include "List.h"
#include "omp.h"
#include "mpi.h"

using namespace std;

class PrimeFinder {
	private:
		double timeTaken;
	public:
		PrimeFinder() {timeTaken = 0;}
		~PrimeFinder() {}
		void gatherPrimes(int method, unsigned long long size, unsigned long long blockSize);
		void gatherPrimesOpenMP(int method, unsigned long long size, unsigned long long blockSize);
		vector<unsigned long long> gatherPrimesMPI(unsigned long long size, unsigned long long blockSize, int proc, int nProc);
		double getTime() {return timeTaken;}
};
