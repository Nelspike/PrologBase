#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include "graphviewer.h"
#include "Avioes.h"
#include "Voos.h"
#include "Graph.h"
using namespace std;


void carregarVoos(Graph<VertexInfo> &grafo,VertexInfo inicio, VertexInfo fim);
void carregarAvioes(vector<Aviao> &avioes);

int main() {


	Graph <VertexInfo> grafo;
	vector <Aviao> avioes;


	VertexInfo inicio={"DKR",Data("09/01/2009 00:00"),1};
	VertexInfo fim={"LIS",Data("09/01/2009 8:00"),2};

	carregarVoos(grafo,inicio,fim);
	carregarAvioes(avioes);

	cout << "Carregado" << endl;
	cout << "Calculando tempo minimo..." << endl;
	grafo.calculateMinTC(inicio);
	cout << "Calculado" << endl;

	GraphViewer *gv;
	int input=-1;
	while (input!=0) {
		cout << "--- GESTOR DE VOOS ---" << endl;
		cout << "| Partida:\t" << printVertexInfo(grafo.getVertex(inicio)->getInfo()) << endl;
		cout << "| Chegada:\t" << printVertexInfo(grafo.getVertex(fim)->getInfo()) << endl;
		cout << "--- MENU ---" << endl;
		cout << "|\t1 - Mostrar caminho mais curto" << endl;
		cout << "|\t2 - Mostrar voos" << endl;
		cout << "|\t3 - Mostrar distribuicao dos avioes" << endl;
		cout << "|\t4 - Mostrar diagrama de Gantt" << endl;
		cout << "|\t-----" << endl;
		cout << "|\t5 - Alterar partida" << endl;
		cout << "|\t6 - Alterar chegada" << endl;
		cout << "|\t-----" << endl;
		cout << "|\t0 - Sair" << endl;
		cout << "\tEscolha:";
		cin >> input;
		VertexInfo newInfo;
		switch (input) {
			case 1:case 2:case 3:
				gv=new GraphViewer(SCREEN_W*MULT_SIZE,SCREEN_H*MULT_SIZE,true);
				break;
			case 4:
				gv=new GraphViewer(SCREEN_W*MULT_SIZE,SCREEN_H*MULT_SIZE,false);
				break;
			case 5:case 6:
				string temp;
				stringstream tempstream;
				cout << "Local:"; cin >> newInfo.local;
				cout << "Dia:"; cin >> temp;
				tempstream << "09/"<< temp <<"/2009 ";
				cout << "Horas(hh:mm):"; cin >> temp;
				tempstream << temp;
				newInfo.data=Data(tempstream.str());
				newInfo.tipo=input-4;
			break;
		}
		if (input>=1 && input<=4) {
			gv->createWindow(SCREEN_W,SCREEN_H);
			gv->defineVertexColor("gray");
			gv->defineEdgeColor("gray");
		}
		switch (input) {
			case 1:
				grafo.showPath(gv);
				break;
			case 2:
				grafo.showTopological(gv);
				break;
			case 3:
				grafo.showAvioes(gv,avioes);
				break;
			case 4:
				grafo.showGantt(gv);
				break;
			case 5:
				inicio=newInfo;
				break;
			case 6:
				fim=newInfo;
				break;
		}
		if (input==5 || input==6) {
			carregarVoos(grafo,inicio,fim);
			cout << "Carregado" << endl;
			cout << "Calculando tempo minimo..." << endl;
			grafo.calculateMinTC(inicio);
			cout << "Calculado" << endl;

		}
	}
	return 0;
}


void carregarVoos(Graph<VertexInfo> &grafo,VertexInfo inicio, VertexInfo fim) {
	grafo.clearGraph();
	cout << "Carregando os voos..." << endl;
	ifstream fVoos("CPAL_Voos_Set09.csv");
	string info;

	grafo.addVertex(inicio);grafo.addVertex(fim);

	getline(fVoos,info); // Passa a primeira linha do ficheiro

	// Adiciona os vertices (dois por voo) e liga-os
	while (getline(fVoos,info)) {
		Voo temp(info);
		if (temp.getPartida()<fim.data && inicio.data<temp.getChegada()) {
			VertexInfo origem={temp.getOrigem(),temp.getPartida(),3};
			VertexInfo chegada={temp.getDestino(),temp.getChegada(),4};
			grafo.addVertex(origem);grafo.addVertex(chegada);

			double peso=temp.getChegada()-temp.getPartida();
			grafo.addEdge(origem,chegada, peso);
		}
	}

	grafo.sortVertex();

	// Adiciona as arestas adicionais
	for (size_t i=0;i<grafo.getVertexSet().size();i++) {
		while (grafo.getVertexSet()[i]->getInfo().tipo==3) {i++;} // As partidas ja estao todas ligadas

		for (size_t j=0;j<grafo.getVertexSet().size();j++) {
			if (grafo.getVertexSet()[j]->getInfo().tipo==4) {break;} // Nao vale a pena ligar chegada->chegada
																	 // NOTA: vector ordenado por tipo
			while (j<grafo.getVertexSet().size() &&
					grafo.getVertexSet()[j]->getInfo().data < grafo.getVertexSet()[i]->getInfo().data)
					{j++;}

			double peso=(grafo.getVertexSet()[j]->getInfo().data-grafo.getVertexSet()[i]->getInfo().data);
			if (grafo.getVertexSet()[i]->getInfo().local.compare(grafo.getVertexSet()[j]->getInfo().local)==0
					&&
					grafo.getVertexSet()[i]->getInfo().data < grafo.getVertexSet()[j]->getInfo().data) {
				grafo.addEdge(grafo.getVertexSet()[i]->getInfo(),grafo.getVertexSet()[j]->getInfo(),peso);
			}
		}
	}
	fVoos.close();
	cout << "Voos Carregados" << endl;
}

void carregarAvioes(vector<Aviao> &avioes) {
	cout << "Carregando os avioes..." << endl;
	ifstream fAvioes("CPAL_Avioes.csv");

	string info;
	getline(fAvioes,info); // Passa a primeira linha do ficheiro

	while (getline(fAvioes,info)) {
		Aviao temp(info);
		avioes.push_back(temp);
	}
	sort(avioes.begin(),avioes.end());
	cout << "Avioes Carregados" << endl;
	fAvioes.close();
}
