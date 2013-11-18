#ifndef _AVIOES_H_
#define _AVIOES_H_

#include <string>
using namespace std;

/*
 * Classe Aviao
 *
 */
class Aviao {
	private:
		string matricula;
		string nome;
		string tipo;
		string frota;
		double pesoMaximoDescolagem;
		double pesoMaximoAterragem;

		double custoTaxasDia;
		double custoManutencaoMinuto;
		double custoCombustivelMinuto;

		int comandantes;
		int copilotos;
		int supervisoresCabine;
		int chefesCabine;
		int comissariosAssistentes;

	public:

		/**
		 * Construtor do aviao
		 * @param parse String do CSV referente ao aviao.
		 */
		Aviao(string parse);
		/**
		 * Imprime as informacoes do aviao (usada para testes).
		 */
		void print();

		//Gets

		/**
		 * Devolve a Matricula de um Aviao.
		 * @return Um inteiro com a matricula.
		 */
		string getMatricula() const {return matricula;}

		/**
		 * Devolve o nome de um Aviao.
		 * @return Uma string com a matricula.
		 */
		string getNome() const {return nome;}

		/**
		 * Devolve o tipo de Aviao.
		 * @return Uma string com a matricula.
		 */
		string getTipo() const {return tipo;}

		/**
		 * Devolve uma frota de um Aviao.
		 * @return Uma string com a frota.
		 */
		string getFrota() const {return frota;}

		/**
		 * Devolve o Peso Maximo para a Descolagem do Aviao em questao.
		 * @return Um tipo de dados double com o peso maximo para descolagem.
		 */
		double getPesoMaximoDescolagem() const {return pesoMaximoDescolagem;}

		/**
		 * Devolve o Peso Maximo de Aterragem do Aviao.
		 * @return Um tipo de dados double com o peso Maximo de Aterragem.
		 */
		double getPesoMaximoAtterragem() const {return pesoMaximoAterragem;}

		/**
		 * Devolve o Cutso de taxas por dia de um aviao.
		 * @return Um tipo de dados double com o custo de taxas por dia.
		 */
		double getCustoTaxasDia() const {return custoTaxasDia;}

		/**
		 * Devolve o custo de manutencao de cada aviao por minuto.
		 * @return Um tipo de dados double com o custo de manutencao por minuto.
		 */
		double getCustoManutencaoMinuto() const {return custoManutencaoMinuto;}

		/**
		 * Devolve o Custo de combustivel de um aviao por minuto.
		 * @return Um tipo de dados double com o custo de combustivel por minuto.
		 */
		double getCustoCombustivelMinuto() const {return custoCombustivelMinuto;}

		/**
		 * Devolve o numero de comandantes de um aviao.
		 * @return Um inteiro com o numero de dados de comandantes.
		 */
		int getComandantes() const {return comandantes;}

		/**
		 * Devolve o numero de Co-pilotos de um aviao.
		 * @return Um inteiro com o numero de co-pilotos.
		 */
		int getCopilotos() const {return copilotos;}

		/**
		 * Devolve o numero de supervisores de cabine de um aviao.
		 * @return Um inteiro com o numero de supervisores de cabine.
		 */
		int getSupervisoresCabine() const {return supervisoresCabine;}

		/**
		 * Devolve o numero de chefes de cabine de um aviao.
		 * @return Um inteiro com o numero de chefes de cabine.
		 */
		int getChefesCabine() const {return chefesCabine;}

		/**
		 * Devolve o numero de comissarios assistentes de aviao.
		 * @return Um inteiro com o numero de comissarios assistentes.
		 */
		int getComissariosAssistentes() const {return comissariosAssistentes;}

		//Sets

		/**
		 * Define a Matricula de um Aviao.
		 * @param mat A Matricula em questao.
		 */
		void setMatricula(string mat) {matricula = mat;}

		/**
		 * Define o nome de um Aviao.
		 * @param name O nome em questao.
		 */
		void setNome(string name) {nome = name;}

		/**
		 * Define o Tipo de um Aviao.
		 * @param tp O Tipo em questao.
		 */
		void setTipo(string tp) {tipo = tp;}

		/**
		 * Define a Frota de um Aviao.
		 * @param frot A Frota em questao.
		 */
		void setFrota(string frot) {frota = frot;}

		/**
		 * Define o Peso Maximo para a Descolagem do Aviao em questao.
		 * @param pMaxD O Peso Maximo para a Descolagem em questao.
		 */
		void setPesoMaximoDescolagem(double pMaxD) {pesoMaximoDescolagem = pMaxD;}

		/**
		 * Define o Peso Maximo para a Aterragem do Aviao em questao.
		 * @param pMaxA O Peso Maximo para a Aterragem em questao.
		 */
		void setPesoMaximoAtterragem(double pMaxA) {pesoMaximoAterragem = pMaxA;}

		/**
		 * Define o Custo de Taxas por Dia do Aviao em questao.
		 * @param custoTxD O Custo de Taxas por Dia em questao.
		 */
		void setCustoTaxasDia(double custoTxD) {custoTaxasDia = custoTxD;}

		/**
		 * Define o Custo de Manutencao por Minuto do Aviao em questao.
		 * @param custoManutMin O Custo de Manutencao por Minuto em questao.
		 */
		void setCustoManutencaoMinuto(double custoManutMin) {custoManutencaoMinuto = custoManutMin;}

		/**
		 * Define o Custo de Combustivel por Minuto do Aviao em questao.
		 * @param custoCombMin O Custo de Combustivel por Minuto em questao.
		 */
		void setCustoCombustivelMinuto(double custoCombMin) {custoCombustivelMinuto = custoCombMin;}

		/**
		 * Define o numero de comandantes de um aviao.
		 * @param com O numero de comandantes em questao.
		 */
		void setComandantes(int com) {comandantes = com;}

		/**
		 * Define o numero de co-pilotos de um aviao.
		 * @param cop O numero de co-pilotos em questao.
		 */
		void setCopilotos(int cop) {copilotos = cop;}

		/**
		 * Define o numero de supervisores de cabine de um aviao.
		 * @param supC O numero de supervisores de cabine em questao.
		 */
		void setSupervisoresCabine(int supC) {supervisoresCabine = supC;}

		/**
		 * Define o numero de chefes de cabine de um aviao.
		 * @param chefC O numero de chefes de cabine em questao.
		 */
		void setChefesCabine(int chefC) {chefesCabine = chefC;}

		/**
		 * Define o numero de comissarios assistentes de um aviao.
		 * @param comAss O numero de comissarios assistentes em questao.
		 */
		void setComissariosAssistentes(int comAss) {comissariosAssistentes = comAss;}
		bool operator<(const Aviao &a2) const;

};

bool compareAvioesPtr(Aviao *a1, Aviao *a2);


#endif
