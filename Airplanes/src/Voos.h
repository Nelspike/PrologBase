/*
 * Voos.h
 *
 *  Created on: 15 de Abr de 2011
 *      Author: ei09027
 */

#ifndef VOOS_H_
#define VOOS_H_

#include <iostream>
#include <fstream>
#include <string>
#include "Data.h"
using namespace std;

class Voo
{
	private:
		int idVoo, tipoAviao;
		string origem, destino;
		Data partida, chegada;
		int totLugares;
		int totLugaresVendidos;
		int lugaresExec, lugaresEco;
		int lugaresExecVend, lugaresEcoVend;

	public:
		/**
		 * Construtor por defeito de Voo.
		 * @param parsed String com toda a informacao relativa a um voo a ser processada.
		 */
		Voo(string parsed);

		//Gets da classe

		/**
		 * Devolve o ID de Voo de um aviao.
		 * @return Um inteiro.
		 */
		int getIdVoo() const {return idVoo;}

		/**
		 * Devolve o tipo de aviao utilizado para um voo.
		 * @return Um inteiro.
		 */
		int getTipoAviao() const {return tipoAviao;}

		/**
		 * Devolve o total de lugares de um aviao.
		 * @return Um inteiro.
		 */
		int getTotLugares() const {return totLugares;}

		/**
		 * Devolve o numero total de lugares vendidos de um aviao.
		 * @return Um inteiro.
		 */
		int getTotLugaresVendidos() const {return totLugaresVendidos;}

		/**
		 * Devolve o numero de lugares executivos de um aviao.
		 * @return Um inteiro.
		 */
		int getLugaresExec() const {return lugaresExec;}

		/**
		 * Devolve o numero de lugares economicos de um aviao.
		 * @return Um inteiro.
		 */
		int getLugaresEco() const {return lugaresEco;}

		/**
		 * Devolve o numero de lugares executivos vendidos de um aviao.
		 * @return Um inteiro.
		 */
		int getLugaresExecVend() const {return lugaresExecVend;}

		/**
		 * Devolve o numero de lugares economicos vendidos de um aviao.
		 * @return Um inteiro.
		 */
		int getLugaresEcoVend() const {return lugaresEcoVend;}

		/**
		 * Devolve a origem de um voo.
		 * @return Uma string.
		 */
		string getOrigem() const {return origem;}

		/**
		 * Devolve o destino de um voo.
		 * @return Uma string.
		 */
		string getDestino() const {return destino;}

		/**
		 * Devolve o local de partida de um voo.
		 * @return Uma string.
		 */
		Data getPartida() const {return partida;}

		/**
		 * Devolve o local de chegada de um voo.
		 * @return Uma string.
		 */
		Data getChegada() const {return chegada;}


		//Sets da Classe

		/**
		 * Define o ID de um Voo.
		 * @param id O ID do Voo a ser definido.
		 */
		void setIdVoo(int id) {idVoo = id;}

		/**
		 * Define o tipo de aviao a ser utilizado no Voo.
		 * @param tipo O tipo de aviao a ser definido.
		 */
		void setTipoAviao(int tipo) {tipoAviao = tipo;}

		/**
		 * Define o numero Total de lugares de um aviao.
		 * @param tot O Numero total de lugares a ser definido.
		 */
		void setTotLugares(int tot) {totLugares = tot;}

		/**
		 * Define namero total de lugares do aviao que foram vendidos.
		 * @param vend O numero total de lugares vendidos a ser definido.
		 */
		void setTotLugaresVendidos(int vend) {totLugaresVendidos = vend;}

		/**
		 * Define o numero de lugares executivos de um aviao.
		 * @param exec O numero de lugares executivos a ser definido.
		 */
		void setLugaresExec(int exec) {lugaresExec = exec;}

		/**
		 * Define o numero de lugares economicos de um aviao.
		 * @param eco O numero de lugares economicos a ser definido.
		 */
		void setLugaresEco(int eco) {lugaresEco = eco;}

		/**
		 * Define numero de lugares executivos vendidos de um aviao.
		 * @param execVend O numero de lugares executivos que foram vendidos.
		 */
		void setLugaresExecVend(int execVend) {lugaresExecVend = execVend;}

		/**
		 * Define numero de lugares economicos vendidos de um aviao.
		 * @param execVend O numero de lugares economicos que foram vendidos.
		 */
		void setLugaresEcoVend(int ecoVend) {lugaresEcoVend = ecoVend;}

		/**
		 * Define a origem de um voo.
		 * @param ori A origem de um voo a ser definida.
		 */
		void setOrigem(string ori) {origem = ori;}

		/**
		 * Define o destino de um voo.
		 * @param dest O destino de um voo a ser definido.
		 */
		void setDestino(string dest) {destino = dest;}

		/**
		 * Define a partida de um voo.
		 * @param part A partida de um voo a ser definida.
		 */
		void setPartida(string part) {partida = Data(part);}

		/**
		 * Define a chegada de um voo.
		 * @param cheg A chegada de um voo a ser definida.
		 */
		void setChegada(string cheg) {chegada = Data(cheg);}

		/**
		 * Imprime no ecra toda a informacao relativa a um voo.
		 */
		void print();

		/**
		 * Compara dois voos.
		 * @param v2 Voo a comparar.
		 * @return Um booleano com a informacao de igualdade de datas.
		 */
		bool operator==(const Voo &v2);
};

#endif /* VOOS_H_ */
