#include "ArvoreBinaria.h"


No::No() {peso=0;folha=false;esquerda=NULL;direita=NULL;}

No::No(unsigned char c,int p) {peso=p;caracter=c;folha=true;esquerda=NULL;direita=NULL;}

void No::addNo(No* n) {
	if (esquerda==NULL) {esquerda=n;peso+=n->getPeso();}
	else if (direita==NULL) {direita=n;peso+=n->getPeso();}
}
No* No::getEsquerda() {
	return esquerda;
}
No* No::getDireita() {
	return direita;
}
int No::getPeso() {
	return peso;
}
unsigned char No::getChar() {
	if (folha) {return caracter;}
	return -1;
}
bool No::isFolha() {return folha;}

No juntaNos(No* n1,No* n2) {
	No ret;
	ret.addNo(n1);
	ret.addNo(n2);
	return ret;
}

vector <caracter> getRepresentacoes(No &n) {
	vector <caracter> ret;
	dfs(&n,"",ret);
	return ret;
}
void dfs(No* n,string s,vector <caracter> &r) {
	if (n->getEsquerda()!=NULL) {dfs(n->getEsquerda(),s+"0",r);}
	if (n->getDireita()!=NULL) {dfs(n->getDireita(),s+"1",r);}
	if (n->isFolha()) {
		caracter chr={n->getChar(),s};
		r.push_back(chr);}
}
bool compareNoPtr(No* n1, No* n2) {return n1->getPeso()<n2->getPeso();}
