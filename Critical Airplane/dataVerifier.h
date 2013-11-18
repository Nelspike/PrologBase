#ifndef DATAVERIFIER_H_
#define DATAVERIFIER_H_

#include "defines.h"
#include "Sensor.h"
#include "Auxiliary.h"

/**
 *	Definição da classe DataVerifier
 *	Esta classe tem como objectivo fazer todas e quaisquer verificações a valores.
 */
class DataVerifier {
	public:
		DataVerifier() {} //Construtor por defeito de DataVerifier
		~DataVerifier() {} //Destrutor por defeito de DataVerifier
		bool verifyPressure(float value); //função que efectua a verificação da gama de valores de pressão
		bool verifyTemperature(float value); //função que efectua a verificação da gama de valores de temperatura
		bool verifyPressureGap(float previous, float current); //função que efectua a verificação da diferença entre dois valores de pressão
		bool verifyTempGap(float previous, float current); //função que efectua a verificação da diferença entre dois valores de temperatura
		void verifySensors(float sensor1Val, float sensor2Val, bool &sensor1, bool &sensor2, float sensor3Val, bool &sensor3); //função que verifica se os valores correspondem 10% entre si
		void verifySensorsInVal(vector<float> &val); //função que verifica se os valores num vector correspondem 10% entre si
		void verifyPressureInVals(vector<float> &vals); //função que efectua a verificação da gama de valores de pressão num vector
		void verifyTemperatureInVals(vector<float> &vals); //função que efectua a verificação da gama de valores de temperatura num vector
		Sensor** verifySensorsStability(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, Sensor *tempSensor1, Sensor *tempSensor2); //função que verifica se todos os valores dos sensores estão estáveis
		Sensor** sensorVerification(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, Sensor *tempSensor1, Sensor *tempSensor2, vector<string> lineValues); //função de verificação dos sensores
		Sensor** verifyPressureSensorStability(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3); //função que verifica se todos os valores dos sensores de pressão estão estáveis
		Sensor** verifyTempSensorStability(Sensor *tempSensor1, Sensor *tempSensor2); //função que verifica se todos os valores dos sensores de temperatura estão estáveis
		Sensor* pressureSensorAnalysis(Sensor *sensor, vector<string> values, int index, int sensorNum); //função que analisa os sensores de pressão no momento de leitura dos dados
		Sensor* tempSensorAnalysis(Sensor *sensor, vector<string> values, int index, int sensorNum); //função que analisa os sensores de temperatura no momento de leitura dos dados
		Sensor* verifyPressureSensor(Sensor *sensor, int num); //função que verifica a estabilidade dos valores de acordo com as gamas de pressão
		Sensor* verifyTemperatureSensor(Sensor *sensor, int num); //função que verifica a estabilidade dos valores de acordo com as gamas de temperatura
		Sensor* setSensorBroken(Sensor *s, bool val, int num, string type); //função que dita se o sensor está broken pelos valores não entratrem em concordância de 10%
};

#endif /* DATAVERIFIER_H_ */
