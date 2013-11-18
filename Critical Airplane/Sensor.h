/**
 * Disclaimer
 *
 * Todo este código foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer código que se encontre de forma alguma idêntico ou igual é plagiado.
 */

#ifndef SENSOR_H_
#define SENSOR_H_

#include "defines.h"

/**
 *	Definição da classe Sensor
 *	Um sensor é um objecto que consegue obter valores do meio externo ao veículo, e que tem a capacidade de estar avariado e/ou bloqueado.
 *	Cada sensor lê apenas um tipo (type) de dados.
 */
class Sensor {
	private:
		float totalValue; //valor total de pressão/temperatura
		float staticValue; //valor estático de pressão
		string type; //tipo de sensor (Pressão ou Temperatura)
		bool broken; //Sensor avariado
		int stuckAtCount; //variável que define há quantas iterações o sensor está preso num determinado valor
		int brokenCount; //quantas vezes o sensor está avariado
		int brokenFor; //há quantas iterações está o sensor avariado
		bool disabled; //Sensor Bloqueado
	public:
		Sensor(); //Construtor por defeito de Sensor
		Sensor(float total, string t, float staticV=0.0f); //Constructor personalizado de Sensor
		~Sensor() {} //Destrutor de Sensor
		float getTotalValue(); //função que devolve o valor total do sensor
		float getStaticValue();//função que devolve o valor estático do sensor
		string getType(); //função que devolve o tipo do sensor
		bool isBroken(); //função que devolve informação relativa ao estado de avaria do sensor
		bool isDisabled();//função que devolve informação relativa ao estado de bloqueio do sensor
		void setTotalValue(float val); //função que permite dar um valor total ao sensor
		void setStaticValue(float val); //função que permite dar um valor estático ao sensor
		void setBroken(bool broke); //função que permite colocar um sensor avariado ou de volta ao seu estado normal
		void checkBroken(); //função que permite verificar o estado de avaria do sensor (há quantas iterações e se está pronto a ser operacional de novo)
		void incrementBrokenFor() {brokenFor++;}  //função que permite incrementar há quantas iterações o sensor está avariado
		void incrementStuckAt() {stuckAtCount++;} //função que permite incrementar há quantas iterações o sensor está preso num valor
		void nullifyStuckAt() {stuckAtCount = 0;} //função que permite colocar a zeros há quantas iterações o sensor está preso num valor
		int getStuckAtCount() {return stuckAtCount;} //função que devolve há quantas iterações o sensor está preso num valor
		int getBrokenFor() {return brokenFor;} //função que devolve quantas vezes um sensor avariou
		int getBrokenCount() {return brokenCount;} //função que devolve há quantas iterações um sensor
};


#endif /* SENSOR_H_ */
