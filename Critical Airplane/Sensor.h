/**
 * Disclaimer
 *
 * Todo este c�digo foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer c�digo que se encontre de forma alguma id�ntico ou igual � plagiado.
 */

#ifndef SENSOR_H_
#define SENSOR_H_

#include "defines.h"

/**
 *	Defini��o da classe Sensor
 *	Um sensor � um objecto que consegue obter valores do meio externo ao ve�culo, e que tem a capacidade de estar avariado e/ou bloqueado.
 *	Cada sensor l� apenas um tipo (type) de dados.
 */
class Sensor {
	private:
		float totalValue; //valor total de press�o/temperatura
		float staticValue; //valor est�tico de press�o
		string type; //tipo de sensor (Press�o ou Temperatura)
		bool broken; //Sensor avariado
		int stuckAtCount; //vari�vel que define h� quantas itera��es o sensor est� preso num determinado valor
		int brokenCount; //quantas vezes o sensor est� avariado
		int brokenFor; //h� quantas itera��es est� o sensor avariado
		bool disabled; //Sensor Bloqueado
	public:
		Sensor(); //Construtor por defeito de Sensor
		Sensor(float total, string t, float staticV=0.0f); //Constructor personalizado de Sensor
		~Sensor() {} //Destrutor de Sensor
		float getTotalValue(); //fun��o que devolve o valor total do sensor
		float getStaticValue();//fun��o que devolve o valor est�tico do sensor
		string getType(); //fun��o que devolve o tipo do sensor
		bool isBroken(); //fun��o que devolve informa��o relativa ao estado de avaria do sensor
		bool isDisabled();//fun��o que devolve informa��o relativa ao estado de bloqueio do sensor
		void setTotalValue(float val); //fun��o que permite dar um valor total ao sensor
		void setStaticValue(float val); //fun��o que permite dar um valor est�tico ao sensor
		void setBroken(bool broke); //fun��o que permite colocar um sensor avariado ou de volta ao seu estado normal
		void checkBroken(); //fun��o que permite verificar o estado de avaria do sensor (h� quantas itera��es e se est� pronto a ser operacional de novo)
		void incrementBrokenFor() {brokenFor++;}  //fun��o que permite incrementar h� quantas itera��es o sensor est� avariado
		void incrementStuckAt() {stuckAtCount++;} //fun��o que permite incrementar h� quantas itera��es o sensor est� preso num valor
		void nullifyStuckAt() {stuckAtCount = 0;} //fun��o que permite colocar a zeros h� quantas itera��es o sensor est� preso num valor
		int getStuckAtCount() {return stuckAtCount;} //fun��o que devolve h� quantas itera��es o sensor est� preso num valor
		int getBrokenFor() {return brokenFor;} //fun��o que devolve quantas vezes um sensor avariou
		int getBrokenCount() {return brokenCount;} //fun��o que devolve h� quantas itera��es um sensor
};


#endif /* SENSOR_H_ */
