/*
 * CodeHuffman.cpp
 *
 *  Created on: 27 de Mai de 2011
 *      Author: ei09027
 */

#include "Codec.h"
#include "ArvoreBinaria.h"
#include <vector>
#include <string>
#include <fstream>
#include <iostream>
#include <algorithm>
#include <set>
using namespace std;

// Huffman
vector<byte> CodecHuffman::comprimir() {
	vector <byte> ret;
	// Gera a tabela de frequencias
	for (int n=0;n<256;n++) {tabelaFreq[n]=0;}
	ifstream file(filename.c_str(),ios_base::binary);
	char tempchar;
	while (file.read(&tempchar,1)) {
			tabelaFreq[(byte)tempchar]++;
	}
	tabelaFreq[0]++;
	// Coloca a tabela de frequencias no vector
	for (int i=0;i<256;i++) {
		int freq=tabelaFreq[i];
		ret.push_back((char)(freq&0xFF));
		ret.push_back((char)((freq&0xFF00)>>8));
		ret.push_back((char)((freq&0xFF0000)>>16));
		ret.push_back((char)((freq&0xFF000000)>>24));
	}
	// Gera a arvore binaria
	vector<No*> arvore;
	for (int n=0;n<256;n++) {
		if (tabelaFreq[n]>0) {arvore.push_back(new No((unsigned char) n,tabelaFreq[n]));}
	}
	sort(arvore.begin(),arvore.end(),compareNoPtr);
	while (arvore.size()>1) {
		No* novoNo;
		novoNo=new No();
		novoNo->addNo(arvore[0]);
		novoNo->addNo(arvore[1]);
		arvore.push_back(novoNo);
		arvore.erase(arvore.begin());
		arvore.erase(arvore.begin());
		sort(arvore.begin(),arvore.end(),compareNoPtr);
	}
	// Vertifica a representacao de cada caracter
	vector <caracter> representacoes=getRepresentacoes(*arvore[0]);
	string repBuff[256];
	for (size_t n=0;n<representacoes.size();n++) {
		repBuff[(byte)representacoes[n].c]=representacoes[n].representacao;
	}
	// Coloca o output na forma de uma string de 1s e 0s
	string tempOutput;
	file.clear();
	file.seekg(0, ios::beg);
	while (file.read(&tempchar,1)) {
			tempOutput+=repBuff[(byte)tempchar];
			if (tempOutput.size()%8==0) {
				// Conversao intermedia do tempoutput para um array de bytes
				for (size_t i=0;i<tempOutput.size();i+=8) {
						byte newByte=0;
						for (int j=0;j<8;j++) {
							newByte=newByte<<1;
							if (i+j<tempOutput.size() && tempOutput[i+j]=='1') {newByte=newByte | 1;}
						}
						ret.push_back(newByte);
					}
				tempOutput.clear();
			}
	}
	file.close();
	tempOutput+=repBuff[0];
	// conversão final do temp output para um array de bytes
	for (size_t i=0;i<tempOutput.size();i+=8) {
		byte newByte=0;
		for (int j=0;j<8;j++) {
			newByte=newByte<<1;
			if (i+j<tempOutput.size() && tempOutput[i+j]=='1') {newByte=newByte | 1;}
		}
		ret.push_back(newByte);
	}
	return ret;
}

string CodecHuffman::decomprimir() {
	string ret;

	// Carrega a tabela de frequencias
	ifstream file(filename.c_str(),ios_base::binary);
	for (int n=0;n<256;n++) {
		int freq;
		char temp;
		file.read((char*)&freq,sizeof(int));
		tabelaFreq[n]=freq;
	}
	// Gera a arvore binaria
	vector<No*> arvore;
	for (int n=0;n<256;n++) {
		if (tabelaFreq[n]>0) {arvore.push_back(new No((unsigned char) n,tabelaFreq[n]));}
	}

	sort(arvore.begin(),arvore.end(),compareNoPtr);
	while (arvore.size()>1) {
		No* novoNo;
		novoNo=new No();
		novoNo->addNo(arvore[0]);
		novoNo->addNo(arvore[1]);
		arvore.push_back(novoNo);
		arvore.erase(arvore.begin());
		arvore.erase(arvore.begin());
		sort(arvore.begin(),arvore.end(),compareNoPtr);
	}

	// Descodifica o conteudo
	No* noActual=arvore[0];
	char decodedChar=-1;
	while (decodedChar!='\0' && !file.eof()) {
		char input;
		file.read((char*)&input,sizeof(char));
		for (int i=0;i<8;i++) {
			if ((input&0x80)==0x00) {noActual=noActual->getEsquerda();} // 0
			else if ((input&0x80)==0x80) {noActual=noActual->getDireita();} // 1
			input=input << 1; // shiftLeft
			if (noActual->isFolha()) {
				decodedChar=noActual->getChar();
				ret+=decodedChar;
				noActual=arvore[0];
			}
		}
	}
	file.close();

	return ret;
}
