#include "Avioes.h"
#include "Parser.h"
#include <cstdlib>
#include <iostream>
using namespace std;

Aviao::Aviao(string parse) {
	int n = 0;
	string temp;
	matricula = parseString(n,parse);
	nome = parseString(n,parse);
	tipo = parseString(n,parse);
	frota = parseString(n,parse);
	pesoMaximoDescolagem = parseDouble(n, parse);
	pesoMaximoAterragem = parseDouble(n, parse);
	custoTaxasDia = parseDouble(n,parse);
	custoManutencaoMinuto = parseDouble(n,parse);
	custoCombustivelMinuto = parseDouble(n,parse);
	comandantes = parseInt(n,parse);
	copilotos = parseInt(n,parse);
	supervisoresCabine = parseInt(n,parse);
	chefesCabine = parseInt(n,parse);
	comissariosAssistentes = parseInt(n,parse);
}

void Aviao::print() {
	cout << matricula << " " << nome << " " << pesoMaximoDescolagem << " " << copilotos << endl;
}

bool Aviao::operator<(const Aviao &a2) const {
	return custoCombustivelMinuto<a2.getCustoCombustivelMinuto();
}

bool compareAvioesPtr(Aviao *a1, Aviao *a2) {
	return (*a1)<(*a2);
}
