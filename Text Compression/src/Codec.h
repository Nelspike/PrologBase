#ifndef COMPRESSOR_H_
#define COMPRESSOR_H_

#include <string>
#include <vector>
#include <set>
#include <map>

using namespace std;

typedef unsigned char byte;
/**
 * Classe abstracta que representa um codec.
 */
class Codec {
protected:
	string filename;
public:
	/**
	 * Constructor de um codec.
	 * Recebe o nome do ficheiro a carregar para comprimir ou descomprimir
	 *
	 * @param file nome do ficheiro
	 */
	Codec(string file) {filename=file;}
	/**
	 * Decomprime o ficheiro
	 *
	 * @return texto do ficheiro
	 */
	string virtual decomprimir() = 0;
};

/**
 *
 * Codec que usa os codigos de Huffman
 */
class CodecHuffman: public Codec {
private:
	int tabelaFreq[256];
public:
	CodecHuffman(string file) : Codec(file) {};
	vector<byte> comprimir();
	string decomprimir();
};


/**
 *
 * Codec que usa Lempel-Ziv-Welch
 */
class CodecLZW: public Codec {
protected:
	int size;
	map<string,int> dictionaryCompress;
	map<int,string> dictionaryDecompress;
public:
	CodecLZW(string file) : Codec(file)
	{
		int i;
		for (i = 0; i < 256; i++)
		{
		    dictionaryCompress[string(1, i)] = i;
		}
		size = 256;
	};
	vector<int> comprimir();
	string decomprimir();
	void setDicCompress(map<string,int> d)
	{
		dictionaryCompress = d;
	}
	map<string,int> getDicCompress()
	{
		return dictionaryCompress;
	}
};


#endif
