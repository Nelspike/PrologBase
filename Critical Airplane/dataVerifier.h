#ifndef DATAVERIFIER_H_
#define DATAVERIFIER_H_

#include "defines.h"
#include "Sensor.h"
#include "Auxiliary.h"

/**
 *	Defini��o da classe DataVerifier
 *	Esta classe tem como objectivo fazer todas e quaisquer verifica��es a valores.
 */
class DataVerifier {
	public:
		DataVerifier() {} //Construtor por defeito de DataVerifier
		~DataVerifier() {} //Destrutor por defeito de DataVerifier
		bool verifyPressure(float value); //fun��o que efectua a verifica��o da gama de valores de press�o
		bool verifyTemperature(float value); //fun��o que efectua a verifica��o da gama de valores de temperatura
		bool verifyPressureGap(float previous, float current); //fun��o que efectua a verifica��o da diferen�a entre dois valores de press�o
		bool verifyTempGap(float previous, float current); //fun��o que efectua a verifica��o da diferen�a entre dois valores de temperatura
		void verifySensors(float sensor1Val, float sensor2Val, bool &sensor1, bool &sensor2, float sensor3Val, bool &sensor3); //fun��o que verifica se os valores correspondem 10% entre si
		void verifySensorsInVal(vector<float> &val); //fun��o que verifica se os valores num vector correspondem 10% entre si
		void verifyPressureInVals(vector<float> &vals); //fun��o que efectua a verifica��o da gama de valores de press�o num vector
		void verifyTemperatureInVals(vector<float> &vals); //fun��o que efectua a verifica��o da gama de valores de temperatura num vector
		Sensor** verifySensorsStability(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, Sensor *tempSensor1, Sensor *tempSensor2); //fun��o que verifica se todos os valores dos sensores est�o est�veis
		Sensor** sensorVerification(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, Sensor *tempSensor1, Sensor *tempSensor2, vector<string> lineValues); //fun��o de verifica��o dos sensores
		Sensor** verifyPressureSensorStability(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3); //fun��o que verifica se todos os valores dos sensores de press�o est�o est�veis
		Sensor** verifyTempSensorStability(Sensor *tempSensor1, Sensor *tempSensor2); //fun��o que verifica se todos os valores dos sensores de temperatura est�o est�veis
		Sensor* pressureSensorAnalysis(Sensor *sensor, vector<string> values, int index, int sensorNum); //fun��o que analisa os sensores de press�o no momento de leitura dos dados
		Sensor* tempSensorAnalysis(Sensor *sensor, vector<string> values, int index, int sensorNum); //fun��o que analisa os sensores de temperatura no momento de leitura dos dados
		Sensor* verifyPressureSensor(Sensor *sensor, int num); //fun��o que verifica a estabilidade dos valores de acordo com as gamas de press�o
		Sensor* verifyTemperatureSensor(Sensor *sensor, int num); //fun��o que verifica a estabilidade dos valores de acordo com as gamas de temperatura
		Sensor* setSensorBroken(Sensor *s, bool val, int num, string type); //fun��o que dita se o sensor est� broken pelos valores n�o entratrem em concord�ncia de 10%
};

#endif /* DATAVERIFIER_H_ */
