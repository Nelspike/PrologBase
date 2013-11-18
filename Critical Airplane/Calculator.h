/**
 * Disclaimer
 *
 * Todo este c�digo foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer c�digo que se encontre de forma alguma id�ntico ou igual � plagiado.
 */

#ifndef CALCULATOR_H_
#define CALCULATOR_H_


#include "defines.h"
#include "Sensor.h"

/**
 *	Defini��o da classe Auxiliary
 *	Uma classe apenas usada para fun��es auxiliares
 */
class Calculator {

	public:
		Calculator() {} //Construtor por defeito de Calculator
		~Calculator() {}  //Destrutor por defeito de Auxiliary
		float calcDynamicPressure(float total, float staticPressure); //fun��o que calcula a press�o din�mica, atrav�s da total e est�tica
		float getKelvin(float celsius); //fun��o que converte graus celsius em Kelvin
		float calcMachNumber(float sPressure, float dPressure); //fun��o que calcula o Mach Number
		float calcOAT(float temperature, float mach); //fun��o que calcula a Open Air Temperature
		float calcTAS(float mach, float OAT); //fun��o que calcula a True AirSpeed
		void brokenAugment(Sensor *sensor, int &count, float &avg, int val); //fun��o que verifica se o valor do sensor dever� entrar para o c�lculo da m�dia
		float* getUnbrokenPressureAverage(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, vector<float> otherTotal, vector<float> otherStatic); //fun��o que calcula a m�dia das press�es de sensores activos
		float getUnbrokenTemperatureAverage(Sensor *tempSensor1, Sensor *tempSensor2, vector<float> otherTemp); //fun��o que calcula a m�dia das temperaturas de sensores activos
		bool isnan_float (float f); //fun��o que verifica que se o n�mero � realmente um n�mero
		bool percentualRelation(float first, float second); //fun��o que verifica a rela��o percentual entre dois valores
};

#endif /* CALCULATOR_H_ */
