#ifndef _PARSER_H_
#define _PARSER_H_

#include <string>
using namespace std;

/**
 * Faz o parse de uma string a partir de uma posicao n
 * e coloca n no inicio da proxima informacao
 * @param n posicao onde comeca a string
 * @param s string a processar
 * @return string processada
 */
string parseString(int &n,string s);
/**
 * Faz o parse de um double a partir de uma posicao n
 * e coloca n no inicio da proxima informacao
 * @param n posicao onde comeca o double
 * @param s string a processar
 * @return double processada
 */
double parseDouble(int &n,string s);
/**
 * Faz o parse de um inteiro a partir de uma posicao n
 * e coloca n no inicio da proxima informacao
 * @param n posicao onde comeca o inteiro
 * @param s string a processar
 * @return double processada
 */
int parseInt(int &n,string s);

#endif
