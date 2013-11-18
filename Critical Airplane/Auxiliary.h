/**
 * Disclaimer
 *
 * Todo este código foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer código que se encontre de forma alguma idêntico ou igual é plagiado.
 */

#ifndef AUXILIARY_H_
#define AUXILIARY_H_

#include "defines.h"

/**
 *	Definição da classe Auxiliary
 *	Uma classe apenas usada para funções auxiliares
 */
class Auxiliary {

	private:
		vector<string> other1, other2, other3;
		void splitAux(const string &s, char delim, vector<string> &elems); //Função auxiliar do split
	public:
		Auxiliary() {} //Construtor por defeito de Auxiliary
		~Auxiliary() {} //Destrutor por defeito de Auxiliary
		vector<string> split(string s, char delim); //função que delimita strings em tokens, através de um caracter delimitador
		char* convertString(string s); //função que converte uma string para um apontador de caracteres
		vector<string> readFile(string filename); //função que lê todas as linhas de um ficheiro e guarda num vector de strings
		void writeFile(string file, string message); //função que escreve uma linha para um ficheiro
		void getFromVecs(vector<float> &val1, vector<float> &val2, vector<float> &val3, vector<string> lineValues); //função que guarda em fectores de floats, valores lidos de um ficheiro
		vector<float> *getValues(vector<vector<string> > val, int currentLine); //função que vai buscar todos os valores de uma linha de um ficheiro e guarda num vector de floats
		void clearRepeatedValues(vector<float> &toClean); //função que elimina repetidos num vector
};

#endif /* AUXILIARY_H_ */
