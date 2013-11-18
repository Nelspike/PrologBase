/*
 * Voos.cpp
 *
 *  Created on: 15 de Abr de 2011
 *      Author: ei09027
 */

#include "Voos.h"
#include "Parser.h"
#include <cstdlib>
#include <iostream>

Voo::Voo(string parsed)
{
	int n = 0;
	string temp;
	idVoo = parseInt(n, parsed);
	origem = parseString(n, parsed);
	destino = parseString(n, parsed);
	tipoAviao = parseInt(n, parsed);
	temp = parseString(n, parsed);
	partida = Data(temp);
	temp = parseString(n, parsed);
	chegada = Data(temp);
	lugaresExec = parseInt(n, parsed);
	lugaresEco = parseInt(n, parsed);
	lugaresExecVend = parseInt(n, parsed);
	lugaresEcoVend = parseInt(n, parsed);
	totLugares = lugaresExec + lugaresEco;
	totLugaresVendidos = lugaresExecVend + lugaresEcoVend;
}

bool Voo::operator==(const Voo &v2) {
	return idVoo==v2.getIdVoo() &&
			tipoAviao==v2.getTipoAviao() &&
			partida==v2.getPartida() &&
			chegada==v2.getChegada();
}

void Voo::print()
{	/*
	cout << idVoo << " " << tipoAviao << " " << origem << " " << destino << " "
		<< partida << " " << chegada << " " << lugaresExec << " " << lugaresEco << " "
		<< lugaresExecVend << " " << lugaresEcoVend << endl;*/
	cout << partida.getHoras() << ":" << partida.getMinutos() << endl;
}
