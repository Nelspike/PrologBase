#ifndef ARVOREBINARIA_H_
#define ARVOREBINARIA_H_

#include <string>
#include <vector>
using namespace std;
/*
 * Caracter e a sua representacao
 */
struct caracter {
	unsigned char c;
	string representacao;
};

/*
 * \class No ArvoreBinaria.h "ArvoreBinaria.h"
 *
 * Classe abstracta que representa um no de uma arvore
 */
class No {
	private:
		int peso;
		bool folha;
		unsigned char caracter;
		No* esquerda;
		No* direita;
	public:
		/*
		 * Constructor do no
		 */
		No();
		/*
		 * Constructor do no folha
		 * @params c byte
		 * @params p frequencia
		 */
		No(unsigned char c,int p);
		/*
		 * Adiciona um no
		 * @params n No a adicionar
		 */
		void addNo(No* n);
		/*
		 * Retorna o apontador para o no a esquerda
		 * @return No a esquerda
		 */
		No* getEsquerda();
		/*
		 * Retorna o apontador para o no a direita
		 * @return No a direita
		 */
		No* getDireita();
		/*
		 * Retorna o peso(frequencia) do no
		 * @return Peso do no
		 */
		int getPeso();
		/*
		 * Retorna o caracter do no
		 * @return Caracter do no
		 */
		unsigned char getChar();
		/*
		 * Retorna se o no e uma folha
		 * @return Se for uma folha, retorna true
		 */
		bool isFolha();
};

/*
 * Retorna um no como juncao de dois nos
 * @param n1 No a esquerda
 * @param n1 No a direita
 * @return Novo no
 */
No juntaNos(No* n1,No* n2);
/*
 * Retorna as representacoes da arvore
 * @param n raiz da arvore
 * @return Vector de representacoes
 */
vector <caracter> getRepresentacoes(No &n);
/*
 * Recebe uma string s vazia e faz uma pesquisa em profundidade a arvore
 * retorna o vector de representacoes da arvore
 * @param n Raiz da arvore
 * @param s String que armazena a representacao actual
 * @param r Vector onde vao ficar guardadas as representacoes
 */
void dfs(No* n,string s,vector <caracter> &r);
/*
 * Retorna (*n1)<(*n2)
 * @param n1 No 1
 * @param n2 No 2
 * @return (*n1)<(*n2)
 */
bool compareNoPtr(No* n1, No* n2);

#endif
