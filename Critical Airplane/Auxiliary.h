/**
 * Disclaimer
 *
 * Todo este c�digo foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer c�digo que se encontre de forma alguma id�ntico ou igual � plagiado.
 */

#ifndef AUXILIARY_H_
#define AUXILIARY_H_

#include "defines.h"

/**
 *	Defini��o da classe Auxiliary
 *	Uma classe apenas usada para fun��es auxiliares
 */
class Auxiliary {

	private:
		vector<string> other1, other2, other3;
		void splitAux(const string &s, char delim, vector<string> &elems); //Fun��o auxiliar do split
	public:
		Auxiliary() {} //Construtor por defeito de Auxiliary
		~Auxiliary() {} //Destrutor por defeito de Auxiliary
		vector<string> split(string s, char delim); //fun��o que delimita strings em tokens, atrav�s de um caracter delimitador
		char* convertString(string s); //fun��o que converte uma string para um apontador de caracteres
		vector<string> readFile(string filename); //fun��o que l� todas as linhas de um ficheiro e guarda num vector de strings
		void writeFile(string file, string message); //fun��o que escreve uma linha para um ficheiro
		void getFromVecs(vector<float> &val1, vector<float> &val2, vector<float> &val3, vector<string> lineValues); //fun��o que guarda em fectores de floats, valores lidos de um ficheiro
		vector<float> *getValues(vector<vector<string> > val, int currentLine); //fun��o que vai buscar todos os valores de uma linha de um ficheiro e guarda num vector de floats
		void clearRepeatedValues(vector<float> &toClean); //fun��o que elimina repetidos num vector
};

#endif /* AUXILIARY_H_ */
