#ifndef _DATA_H_
#define _DATA_H_

#include <string>
using namespace std;

class Data {
	private:
		int dia;
		int mes;
		int ano;
		int horas;
		int minutos;
	public:
		/**
		 * Construtor por defeito de Data.
		 */
		Data();

		/**
		 * Construtor de Data, onde se passa uma string com toda a informacao precisa a uma Data.
		 * @param s A string que contem toda a informacao.
		 */
		Data(string s);

		/**
		 * Devolve o dia.
		 * @return Um inteiro com o dia.
		 */
		int getDia() const;

		/**
		 * Devolve o mes.
		 * @return Um inteiro com o mes.
		 */
		int getMes() const;

		/**
		 * Devolve o ano.
		 * @return Um inteiro com o ano.
		 */
		int getAno() const;

		/**
		 * Devolve as horas.
		 * @return Um inteiro com as horas.
		 */
		int getHoras() const;

		/**
		 * Devolve os minutos.
		 * @return Um inteiro com os minutos.
		 */
		int getMinutos() const;

		/**
		 * Define o dia.
		 * @param d O dia em questao.
		 */
		void setDia(int d);

		/**
		 * Define o mes.
		 * @param m O mes em questao.
		 */
		void setMes(int m);

		/**
		 * Define o ano.
		 * @param a O ano em questao.
		 */
		void setAno(int a);

		/**
		 * Define as horas.
		 * @param h As horas em questao.
		 */
		void setHoras(int h);

		/**
		 * Define os minutos.
		 * @param m Os minutos em questao.
		 */
		void setMinutos(int m);

		/**
		 * Operador de igualidade de Data.
		 * @param d Data a comparar com a data em questao.
		 * @return Um booleano com a informacao da igualidade.
		 */
		bool operator==(const Data &d) const;

		/**
		 * Operador de comparacao menor de Data.
		 * @param d Data a comparar com a data em questao.
		 * @return Um booleano com a informacao da minoridade.
		 */
		bool operator<(const Data &d) const;

		/**
		 * Operador de comparacao menor ou igual de Data.
		 * @param d Data a comparar com a data em questao.
		 * @return Um booleano com a informacao da igualidade ou minoridade.
		 */
		bool operator<=(const Data &d) const;

		/**
		 * Operador de subtraccao de Data.
		 * @param d Data a subtrair com a data em questao.
		 * @return Um inteiro com a diferenca entre as datas.
		 */
		int operator-(const Data &d) const;
};

#endif
