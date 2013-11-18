#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include "Codec.h"
using namespace std;

map<string,int> dictionaryLZW;

void saveBinaryToFile(vector<byte> &output, ofstream &file) {
	for (size_t i=0;i<output.size();i++) {
			char out=output[i];
			file.write(&out,1);
		}
	file << (char) 0;
}

void saveBinaryToFile(vector<int> &output, ofstream &file) {
	for (size_t i=0;i<output.size();i++) {
			unsigned short int out=output[i];
			file.write((char *)&out,sizeof(short int));
		}
	file << (char) 0;

}

unsigned char chooseMenu()
{
	unsigned char ret;
	cout << "Escolha: ";
	cin >> ret;
	cin.get();
	cout << endl;
	return ret;
}

void compressHuffmanMenu()
{
	string ficheiro;
	cout << "Nome do ficheiro de input: ";
	cin >> ficheiro;
	cin.get();
	cout << endl;

	CodecHuffman codec(ficheiro);
	vector<byte> output = codec.comprimir();
	cout << "Nome do ficheiro de output (com extensao .huf): ";
	cin >> ficheiro;
	cin.get();
	ofstream file(ficheiro.c_str(),ios_base::binary);
	saveBinaryToFile(output,file);
	file.close();

	cout << endl << "Compressao em Huffman feita com sucesso!" << endl;
	system("pause");
	system("cls");
}

void decompressHuffmanMenu()
{
	string ficheiro;
	cout << "Nome do ficheiro com a informacao codificada: ";
	cin >> ficheiro;
	cin.get();
	cout << endl;

	CodecHuffman codec(ficheiro);
	cout << codec.decomprimir() << endl;

	cout << endl << "Descompressao em Huffman feita com sucesso!" << endl;
	system("pause");
	system("cls");
}

void compressLZWMenu()
{
	string ficheiro;
	cout << "Nome do ficheiro de input: ";
	cin >> ficheiro;
	cin.get();
	CodecLZW codec(ficheiro);

	cout << "Nome do ficheiro de output(com extensao .lzw): " << endl;
	cin >> ficheiro;
	cin.get();
	ofstream file(ficheiro.c_str(),ios_base::binary);

	vector<int> output = codec.comprimir();
	saveBinaryToFile(output,file);

	dictionaryLZW = codec.getDicCompress();

	cout << endl << "Compressao em LZW feita com sucesso!" << endl;
	system("pause");
	system("cls");
}

void decompressLZWMenu()
{
	string ficheiro;
	cout << "Nome do ficheiro comprimido (formato.lzw): " << endl;
	cin >> ficheiro;
	cin.get();
	CodecLZW codec(ficheiro);
	codec.setDicCompress(dictionaryLZW);
	string res = codec.decomprimir();
	cout << res << endl;

	cout << endl << "Descompressao em LZW feita com sucesso!" << endl;
	system("pause");
	system("cls");

}

void mainMenu()
{
	cout << "+-----------------------------+"<< endl;
	cout << "|  Compressor/Descompressor   |" << endl;
	cout << "+-----------------------------+"<< endl;
	cout << "| 1 | Comprimir em Huffman    |" << endl;
	cout << "| 2 | Descomprimir em Huffman |" << endl;
	cout << "| 3 | Comprimir em LZW        |" << endl;
	cout << "| 4 | Descomprimir em LZW     |" << endl;
	cout << "| 5 | Sair                    |" << endl;
	cout << "+-----------------------------+" << endl;
}

int main()
{
	mainMenu();

	while(true)
	{
		unsigned char choice = chooseMenu();
		switch(choice)
		{
			case '1':
				system("cls");
				compressHuffmanMenu();
				mainMenu();
				break;
			case '2':
				system("cls");
				decompressHuffmanMenu();
				mainMenu();
				break;
			case '3':
				system("cls");
				compressLZWMenu();
				mainMenu();
				break;
			case '4':
				system("cls");
				decompressLZWMenu();
				mainMenu();
				break;
			case '5':
				system("exit");
				break;
			default:
				cout << endl << "A opcao introduzida nao e valida!" << endl;
				system("pause");
				system("cls");
				mainMenu();
				break;
		}
	}

	return 0;
}
