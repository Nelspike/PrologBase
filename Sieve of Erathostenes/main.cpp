#include <iostream>
#include <iomanip>
#include <stdlib.h> 
#include <math.h>
#include <bitset>
#include <fstream>
#include "mpi.h"
#include "PrimeFinder.h"

#define PARALLELISM 1

using namespace std;

int main(int argc, char** argv) {
	PrimeFinder *finder;

	ofstream myfile;
	myfile.open ("outputs2.txt");
	
	if(PARALLELISM == 0) {
		for(int i = 25; i < 33; i++) {
			unsigned long long primeSize = (unsigned long long) pow(2, i);
			myfile << "Array size = (2^" << i << ")" << endl;
			
			/*finder = new PrimeFinder;			
			finder->gatherPrimes(0, primeSize, 0);
			myfile << "First method (Single-Core) = " << finder->getTime() << " seconds" << endl;
			delete finder;*/

			finder = new PrimeFinder;
			finder->gatherPrimes(1, primeSize, 0);
			myfile << "Second method = " << finder->getTime() << " seconds" << endl;
			delete finder;

			for(int j = 32; j < 513; j*=2) {
				int blockSize = j*1024*1024;

				finder = new PrimeFinder;
				finder->gatherPrimes(2, primeSize, blockSize);
				myfile << "Third method (Single-Core) with Block Size (" << j << "MB) = " << finder->getTime() << " seconds" << endl;
				delete finder;

				finder = new PrimeFinder;
				finder->gatherPrimesOpenMP(2, primeSize, blockSize);
				myfile << "Third method (OpenMP) with Block Size (" << j << "MB) = " << finder->getTime() << " seconds" << endl;
				delete finder;
			}
			myfile << endl << "------------------" << endl << endl;
		}
	}
        else {
		
            int currentProcess, nProc;
            MPI_Init( &argc, &argv );
            MPI_Comm_rank( MPI_COMM_WORLD, &currentProcess );
            MPI_Comm_size( MPI_COMM_WORLD, &nProc );               

			//for(int i = 5; i < 6; i++) {
				unsigned long long primeSize = (unsigned long long) pow(2, 7);
				//for(int j = 32; j < 513; j*=2) {
					int blockSize = /*j*1024*1024*/16;
					finder = new PrimeFinder;
					vector<unsigned long long> derp = finder->gatherPrimesMPI(primeSize, blockSize, currentProcess, nProc);
					cout << "Third method (MPI) by process " << currentProcess << " for array (2^" << 6 << ") and block (" << 16 << "MB) = " << finder->getTime() << " seconds" << endl;
					cout << "Size " << derp.size() << endl;
					delete finder;
				//}
			//}
			MPI_Finalize();
        }

	myfile.close();

	return 0;
}
