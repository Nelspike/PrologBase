/**
 * Disclaimer
 *
 * Todo este código foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer código que se encontre de forma alguma idêntico ou igual é plagiado.
 */

#ifndef CALCULATOR_H_
#define CALCULATOR_H_


#include "defines.h"
#include "Sensor.h"

/**
 *	Definição da classe Auxiliary
 *	Uma classe apenas usada para funções auxiliares
 */
class Calculator {

	public:
		Calculator() {} //Construtor por defeito de Calculator
		~Calculator() {}  //Destrutor por defeito de Auxiliary
		float calcDynamicPressure(float total, float staticPressure); //função que calcula a pressão dinâmica, através da total e estática
		float getKelvin(float celsius); //função que converte graus celsius em Kelvin
		float calcMachNumber(float sPressure, float dPressure); //função que calcula o Mach Number
		float calcOAT(float temperature, float mach); //função que calcula a Open Air Temperature
		float calcTAS(float mach, float OAT); //função que calcula a True AirSpeed
		void brokenAugment(Sensor *sensor, int &count, float &avg, int val); //função que verifica se o valor do sensor deverá entrar para o cálculo da média
		float* getUnbrokenPressureAverage(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, vector<float> otherTotal, vector<float> otherStatic); //função que calcula a média das pressões de sensores activos
		float getUnbrokenTemperatureAverage(Sensor *tempSensor1, Sensor *tempSensor2, vector<float> otherTemp); //função que calcula a média das temperaturas de sensores activos
		bool isnan_float (float f); //função que verifica que se o número é realmente um número
		bool percentualRelation(float first, float second); //função que verifica a relação percentual entre dois valores
};

#endif /* CALCULATOR_H_ */
