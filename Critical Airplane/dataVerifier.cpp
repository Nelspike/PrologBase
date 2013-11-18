/**
 * Disclaimer
 *
 * Todo este código foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer código que se encontre de forma alguma idêntico ou igual é plagiado.
 */

#include "dataVerifier.h" //qualquer comentário acerca das funções encontra-se no dataVerifier.h

bool DataVerifier::verifyPressure(float value) {
	return (value >= 100.0f) && (value <= 1500.0f);
}

bool DataVerifier::verifyTemperature(float value) {
	return (value > -100.0f) && (value < 100.0f);
}

bool DataVerifier::verifyPressureGap(float previous, float current) {
	return !(abs(previous - current) > 100.0f);
}

bool DataVerifier::verifyTempGap(float previous, float current) {
	return !(abs(previous - current) > 10.0f);
}

Sensor* DataVerifier::pressureSensorAnalysis(Sensor *sensor, vector<string> values, int index, int sensorNum) {
	if (sensor->isDisabled() || sensor->isBroken())
		return sensor; //se o sensor estiver bloqueado ou avariado, não é necessário fazer verificações

	vector<string> sensorValues;
	float prevTotal, prevStatic, currentTotal, currentStatic;

	Auxiliary *auxFunc = new Auxiliary;
	sensorValues = auxFunc->split(values[index], ':'); //split dos valores de pressão por ':', separando total de estática
	delete auxFunc;

	prevTotal = sensor->getTotalValue();
	prevStatic = sensor->getStaticValue();
	string total = sensorValues[0], stat = sensorValues[1];

	if (total == "--") { //se a total for '--', houve erro de leitura, logo o sensor ficará avariado
		if (OUTSTREAM)
			cout << "Pressure Sensor " << sensorNum
					<< " broke, couldn't read any total pressure value. ";
		sensor->setBroken(true);
		return sensor;
	} else
		currentTotal = atof(auxFunc->convertString(sensorValues[0])); //senão o valor total do sensor será o que foi lido

	if (stat == "--") { //se a estática for '--', houve erro de leitura, logo o sensor ficará avariado
		cout << "Pressure Sensor " << sensorNum
				<< " broke, couldn't read any static pressure value. ";
		sensor->setBroken(true);
		return sensor;
	} else
		currentStatic = atof(auxFunc->convertString(sensorValues[1])); //senão o valor estático do sensor será o que foi lido

	bool sensorStatus = false;
	if (prevTotal == DEFAULT_SENSOR_CONST && prevStatic == DEFAULT_SENSOR_CONST) //se for a primeira iteração, então não será necessário fazer a comparação com o valor anterior
		sensorStatus = true;

	if (sensorStatus) { //criação de um novo sensor visto que é a primeira iteração
		delete sensor;
		sensor = NULL;
		sensor = new Sensor(currentTotal, PRES, currentStatic);
		return sensor;
	}

	bool gapTest = verifyPressureGap(prevTotal, currentTotal)
			&& verifyPressureGap(prevStatic, currentStatic); //verificação da "distância" entre valores de leituras consecutivas

	if (!gapTest || sensorStatus) { //se essa "distância" for grande demais, então o sensor avaria
		if (prevTotal != DEFAULT_SENSOR_CONST
				&& prevStatic != DEFAULT_SENSOR_CONST) {
			cout << "Pressure Sensor " << sensorNum
					<< " broke due to differing too much from the last value read. ";
			sensor->setBroken(true);
			return sensor;
		}
	}

	//se todas as verificações funcionaram até aqui, os valores dos sensores serão os que foram lidos efectivamente
	sensor->setTotalValue(currentTotal);
	sensor->setStaticValue(currentStatic);

	if (prevTotal == currentTotal && prevStatic == currentStatic) { //se as leituras anteriores e actuais forem iguais, o sensor está "preso"
		sensor->incrementStuckAt();
	} else { //senão, não vai estar
		sensor->nullifyStuckAt();
		return sensor;
	}

	if (sensor->getStuckAtCount() == 4) { //se o sensor estiver preso há 4 iterações, então o sensor avaria
		cout << "Pressure Sensor " << sensorNum
				<< " broke due to being stuck at the same value for too long. ";
		sensor->setBroken(true);
	}

	return sensor; //retorna a posição de memória relativa ao sensor em questão
}

Sensor* DataVerifier::tempSensorAnalysis(Sensor *sensor, vector<string> values, int index, int sensorNum) {
	if (sensor->isDisabled() || sensor->isBroken())
		return sensor; //se o sensor estiver bloqueado ou avariado, não é necessário fazer verificações

	vector<string> sensorValues;
	float prevTotal, currentTotal;

	Auxiliary *auxFunc = new Auxiliary;
	sensorValues = auxFunc->split(values[index], ':'); //split dos valores de pressão por ':', separando total de estática
	delete auxFunc;

	prevTotal = sensor->getTotalValue();
	string total = sensorValues[0];

	if (total == "--") { //se a total for '--', houve erro de leitura, logo o sensor ficará avariado
		cout << "Temperature Sensor " << sensorNum
				<< " broke, couldn't read any temperature value. ";
		sensor->setBroken(true);
		return sensor;
	} else
		currentTotal = atof(sensorValues[0].c_str()); //senão o valor total do sensor será o que foi lido

	bool sensorStatus = false;
	if (prevTotal == DEFAULT_SENSOR_CONST) //se for a primeira iteração, então não será necessário fazer a comparação com o valor anterior
		sensorStatus = true;

	if (sensorStatus) { //criação de um novo sensor visto que é a primeira iteração
		delete sensor;
		sensor = new Sensor(currentTotal, PRES, -9000.0f);
		return sensor;
	}

	bool gapTest = verifyPressureGap(prevTotal, currentTotal); //verificação da "distância" entre valores de leituras consecutivas

	if (!gapTest || sensorStatus) { //se essa "distância" for grande demais, então o sensor avaria
		if (prevTotal != DEFAULT_SENSOR_CONST) {
			cout << "Temperature Sensor " << sensorNum
					<< " broke due to differing too much from the last value read. ";
			sensor->setBroken(true);
			return sensor;
		}
	}

	//se todas as verificações funcionaram até aqui, os valores dos sensores serão os que foram lidos efectivamente
	sensor->setTotalValue(currentTotal);

	if (prevTotal == currentTotal) //se as leituras anteriores e actuais forem iguais, o sensor está "preso"
		sensor->incrementStuckAt();
	else { //senão, não vai estar
		sensor->nullifyStuckAt();
		return sensor;
	}

	if (sensor->getStuckAtCount() == 10) { //se o sensor estiver preso há 10 iterações, então o sensor avaria
		cout << "Temperature Sensor " << sensorNum
				<< " broke due to being stuck at the same value for too long. ";
		sensor->setBroken(true);
	}

	return sensor; //retorna a posição de memória relativa ao sensor em questão
}

Sensor* DataVerifier::verifyPressureSensor(Sensor *sensor, int num) {
	bool sensorValid = true;
	sensorValid = verifyPressure(sensor->getTotalValue()); //verifica a gama de pressão total
	sensorValid = sensorValid && verifyPressure(sensor->getStaticValue()); //verifica a gama de pressão estática
	if (!sensorValid) { //Se as gamas não for válida, o sensor avaria
		cout << "Pressure out of bounds in Sensor " << num << "!" << endl;
		sensor->setBroken(true);
	}

	return sensor; //retorna a posição de memória relativa ao sensor em questão
}

Sensor* DataVerifier::verifyTemperatureSensor(Sensor *sensor, int num) {
	bool sensorValid = true;
	sensorValid = verifyTemperature(sensor->getTotalValue()); //verifica a gama de temperatura total
	if (!sensorValid) { //Se a gama não for válida o sensor avaria
		cout << "Temperature out of bounds in Sensor " << num << "!" << endl;
		sensor->setBroken(true);
	}

	return sensor; //retorna a posição de memória relativa ao sensor em questão
}

Sensor* DataVerifier::setSensorBroken(Sensor *s, bool val, int num, string type) {
	if (!val) { //se o valor que vem de fora não for verdadeiro, nem o sensor estiver bloqueado nem avariado, então deve ser colocado em avaria
		if (!s->isDisabled()) {
			if (!s->isBroken()) {
				cout << type << " Sensor " << num
						<< " broke, due to differing too much from the other "
						<< type << " sensors. ";
				s->setBroken(true);
			}
		}
	}

	return s; //retorna a posição de memória relativa ao sensor em questão
}

Sensor** DataVerifier::verifyPressureSensorStability(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3) {
	Sensor** sensRes = (Sensor**) malloc(3 * sizeof(Sensor*)); //os três sensores de pressão
	bool pSensor1 = true, pSensor2 = true, pSensor3 = true;
	float totalValue1, staticValue1, totalValue2, staticValue2, totalValue3, staticValue3;

	//Zona de verificação do Estado
	//Nesta zona, o programa verifica se os sensores estão aptos para serem comparados percentualmente uns com os outros

	if (presSensor1->isDisabled()) {
		pSensor1 = false;
	} else {
		if (presSensor1->isBroken())
			pSensor1 = false;
		else {
			totalValue1 = presSensor1->getTotalValue();
			staticValue1 = presSensor1->getStaticValue();
		}
	}

	if (presSensor2->isDisabled()) {
		pSensor2 = false;
	} else {
		if (presSensor2->isBroken())
			pSensor2 = false;
		else {
			totalValue2 = presSensor2->getTotalValue();
			staticValue2 = presSensor2->getStaticValue();
		}
	}

	if (presSensor3->isDisabled()) {
		pSensor3 = false;
	} else {
		if (presSensor3->isBroken())
			pSensor3 = false;
		else {
			totalValue3 = presSensor3->getTotalValue();
			staticValue3 = presSensor3->getStaticValue();
		}
	}
	//Fim da Zona de verificação do Estado

	//Verificação Percentual dos sensores em termos de valor total
	verifySensors(totalValue1, totalValue2, pSensor1, pSensor2, totalValue3, pSensor3);
	presSensor1 = setSensorBroken(presSensor1, pSensor1, 1, PRES);
	presSensor2 = setSensorBroken(presSensor2, pSensor2, 2, PRES);
	presSensor3 = setSensorBroken(presSensor3, pSensor3, 3, PRES);

	//Verificação Percentual dos sensores em termos de valor estático
	verifySensors(staticValue1, staticValue2, pSensor1, pSensor2, staticValue3, pSensor3);
	presSensor1 = setSensorBroken(presSensor1, pSensor1, 1, PRES);
	presSensor2 = setSensorBroken(presSensor2, pSensor2, 2, PRES);
	presSensor3 = setSensorBroken(presSensor3, pSensor3, 3, PRES);

	sensRes[0] = presSensor1;
	sensRes[1] = presSensor2;
	sensRes[2] = presSensor3;

	return sensRes; //retorna a posição de memória relativa aos sensores em questão
}

Sensor** DataVerifier::verifyTempSensorStability(Sensor *tempSensor1, Sensor *tempSensor2) {
	Sensor** sensRes = (Sensor**) malloc(2 * sizeof(Sensor*)); //os dois sensores de pressão
	bool tSensor1 = true, tSensor2 = true;
	float totalValue1, totalValue2;

	//Zona de verificação do Estado
	//Nesta zona, o programa verifica se os sensores estão aptos para serem comparados percentualmente uns com os outros

	if (tempSensor1->isDisabled()) {
		tSensor1 = false;
	} else {
		if (tempSensor1->isBroken())
			tSensor1 = false;
		else {
			totalValue1 = tempSensor1->getTotalValue();
		}
	}

	if (tempSensor2->isDisabled()) {
		tSensor2 = false;
	} else {
		if (tempSensor2->isBroken())
			tSensor2 = false;
		else {
			totalValue2 = tempSensor2->getTotalValue();
		}
	}
	//Fim da Zona de verificação do Estado

	//Verificação Percentual dos sensores em termos de valor total
	bool var = false;
	verifySensors(totalValue1, totalValue2, tSensor1, tSensor2, -9999.0f, var);
	tempSensor1 = setSensorBroken(tempSensor1, tSensor1, 1, TEMP);
	tempSensor2 = setSensorBroken(tempSensor2, tSensor2, 2, TEMP);

	sensRes[0] = tempSensor1;
	sensRes[1] = tempSensor2;

	return sensRes; //retorna a posição de memória relativa aos sensores em questão
}

void DataVerifier::verifySensorsInVal(vector<float> &val) {
	if (val.size() <= 1) //se o tamanho for 1, não há nada a comparar
		return;
	else if (val.size() == 2) { //se for 2, então há que comparar dois valores
		float first = abs(val[0]), ten1 = first * 0.1f; //10% do primeiro valor
		float second = abs(val[1]), ten2 = second * 0.1f; //10% do segundo valor
		bool rel12, rel21; //relação mútua entre valores (rel1->2, rel 2->1)
		rel12 = (second - ten2 <= first) && (first <= second + ten2);
		rel21 = (first - ten1 <= second) && (second <= first + ten1);

		//se os dois não forem recíprocos, então não deve de haver nenhum valor a ser usado
		if (!(rel12 && rel21))
			val.clear();

		return;
	}

	//No caso de chegar aqui, o tamanho do vector é 3

	float first = abs(val[0]), ten1 = first * 0.1f; //10% do primeiro valor
	float second = abs(val[1]), ten2 = second * 0.1f; //10% do segundo valor
	float third = abs(val[2]), ten3 = third * 0.1f; //10% do terceiro valor

	 //relação mútua entre valores (exemplo: rel1->2, rel 2->1)
	bool rel12 = (second - ten2 <= first) && (first <= second + ten2);
	bool rel21 = (first - ten1 <= second) && (second <= first + ten1);
	bool rel13 = (third - ten3 <= first) && (first <= third + ten3);
	bool rel31 = (first - ten1 <= third) && (third <= first + ten1);
	bool rel23 = (third - ten3 <= second) && (second <= third + ten3);
	bool rel32 = (second - ten2 <= third) && (third <= second + ten2);
	vector<float>::iterator it = val.begin();

	//verificação das relações, e que valores devem ser usados
	if (rel12 && rel21) {
		if (rel13 && rel31)
			return; //todos devem ser usados
		else {
			it += 2;
			val.erase(it); //apenas o primeiro e o segundo devem ser usados
			return;
		}
	} else if (rel13 && rel31) {
		if (rel23 && rel32)
			return; //todos devem ser usados
		else {
			it += 1;
			val.erase(it); //apenas o primeiro e o terceiro devem ser usados
			return;
		}
	} else if (rel23 && rel32) {
		val.erase(it); //apenas o segundo e o terceiro devem ser usados
		return;
	} else
		val.clear(); //nenhum deve ser usado
}

Sensor** DataVerifier::verifySensorsStability(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, Sensor *tempSensor1, Sensor *tempSensor2) {
	//verificação dos sensores respectivos, caso não estejam avariados nem bloqueados
	if (!presSensor1->isDisabled() && !presSensor1->isBroken())
		presSensor1 = verifyPressureSensor(presSensor1, 1);
	if (!presSensor2->isDisabled() && !presSensor2->isBroken())
		presSensor2 = verifyPressureSensor(presSensor2, 2);
	if (!presSensor3->isDisabled() && !presSensor3->isBroken())
		presSensor3 = verifyPressureSensor(presSensor3, 3);
	if (!tempSensor1->isDisabled() && !tempSensor1->isBroken())
		tempSensor1 = verifyTemperatureSensor(tempSensor1, 1);
	if (!tempSensor2->isDisabled() && !tempSensor2->isBroken())
		tempSensor2 = verifyTemperatureSensor(tempSensor2, 2);

	Sensor** presSensors = verifyPressureSensorStability(presSensor1, presSensor2, presSensor3);
	Sensor** tempSensors = verifyTempSensorStability(tempSensor1, tempSensor2);

	Sensor** sensRes = (Sensor**) malloc(5 * sizeof(Sensor*));
	sensRes[0] = presSensors[0];
	sensRes[1] = presSensors[1];
	sensRes[2] = presSensors[2];
	sensRes[3] = tempSensors[0];
	sensRes[4] = tempSensors[1];

	delete[] presSensors;
	delete[] tempSensors;

	return sensRes;
}

Sensor** DataVerifier::sensorVerification(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, Sensor *tempSensor1, Sensor *tempSensor2, vector<string> lineValues) {
	//análise inicial de cada um dos sensores
	presSensor1 = pressureSensorAnalysis(presSensor1, lineValues, 0, 1);
	presSensor2 = pressureSensorAnalysis(presSensor2, lineValues, 1, 2);
	presSensor3 = pressureSensorAnalysis(presSensor3, lineValues, 2, 3);
	tempSensor1 = tempSensorAnalysis(tempSensor1, lineValues, 3, 1);
	tempSensor2 = tempSensorAnalysis(tempSensor2, lineValues, 4, 2);

	return verifySensorsStability(presSensor1, presSensor2, presSensor3, tempSensor1, tempSensor2);
}

void DataVerifier::verifySensors(float sensor1Val, float sensor2Val, bool &sensor1, bool &sensor2, float sensor3Val, bool &sensor3) {
	sensor1Val = abs(sensor1Val);
	sensor2Val = abs(sensor2Val);
	sensor3Val = abs(sensor3Val);

	float ten1 = abs(sensor1Val * 0.1), ten2 = abs(sensor2Val * 0.1), ten3 = abs(sensor3Val * 0.1); //10% de cada um dos valores
	bool rel12, rel13, rel21, rel23, rel31, rel32;

	if (!sensor1 && !sensor2 && !sensor3) //se os sensores estiverem avariados/bloqueados, não há necessidade de verificação
		return;

	//relações mutuas a serem testadas

	if (!sensor3) { //Se o sensor 3 estiver avariado/bloqueado
		if (!sensor1) //Se o sensor 1 estiver avariado/bloqueado
			sensor2 = false; //então, nenhum sensor deverá utilizado os seus valores para o cálculo final
		else { //senão, se o sensor1 estiver activo
			if (!sensor2) //mas o sensor 2 não
				sensor1 = false; //então, nenhum sensor deverá utilizado os seus valores para o cálculo final
			else { //se o sensor 2 estiver
				rel12 = (sensor2Val - ten2 <= sensor1Val)
						&& (sensor1Val <= sensor2Val + ten2);
				rel21 = (sensor1Val - ten1 <= sensor2Val)
						&& (sensor2Val <= sensor1Val + ten1);
				if (rel12 && rel21) { //e as suas relações coincidirem, então os dois valores vão ser usados
					sensor1 = true;
					sensor2 = true;
				} else { //senão, nenhum valor é usado
					sensor1 = false;
					sensor2 = false;
				}
			}
		}
	} else { //se o sensor 3 estiver operacional
		if (!sensor2) { //mas o sensor 2 não
			if (!sensor1) //nem o sensor 1
				sensor3 = false; //então, nenhum sensor deverá utilizado os seus valores para o cálculo final
			else { //senão, se o sensor 1 estiver
				rel13 = (sensor3Val - ten3 <= sensor1Val)
						&& (sensor1Val <= sensor3Val + ten3);
				rel31 = (sensor1Val - ten1 <= sensor3Val)
						&& (sensor3Val <= sensor1Val + ten1);
				if (rel13 && rel31) { //e as suas relações coincidirem, então os dois valores vão ser usados
					sensor1 = true;
					sensor3 = true;
				} else { //senão, nenhum valor é usado
					sensor1 = false;
					sensor3 = false;
				}
			}
		} else if (!sensor1) { //senão se o sensor 1 não estiver activo
			rel23 = (sensor3Val - ten3 <= sensor2Val)
					&& (sensor2Val <= sensor3Val + ten3);
			rel32 = (sensor2Val - ten2 <= sensor3Val)
					&& (sensor3Val <= sensor2Val + ten2);
			if (rel23 && rel32) { //as relações entre 2 e 3 serão analisadas
				sensor2 = true;
				sensor3 = true;
			} else { //senão, nenhum valor é usado
				sensor2 = false;
				sensor3 = false;
			}
		} else { //se então todos estiverem operacionais, então todas serão testadas
			rel12 = (sensor2Val - ten2 <= sensor1Val)
					&& (sensor1Val <= sensor2Val + ten2);
			rel21 = (sensor1Val - ten1 <= sensor2Val)
					&& (sensor2Val <= sensor1Val + ten1);
			rel13 = (sensor3Val - ten3 <= sensor1Val)
					&& (sensor1Val <= sensor3Val + ten3);
			rel31 = (sensor1Val - ten1 <= sensor3Val)
					&& (sensor3Val <= sensor1Val + ten1);
			rel23 = (sensor3Val - ten3 <= sensor2Val)
					&& (sensor2Val <= sensor3Val + ten3);
			rel32 = (sensor2Val - ten2 <= sensor3Val)
					&& (sensor3Val <= sensor2Val + ten2);

			if (rel12 && rel21) {
				if (rel13 && rel31) { //todos serão usados
					sensor1 = true;
					sensor2 = true;
					sensor3 = true;
				} else { //apenas o sensor 1 e 2 serão usados
					sensor1 = true;
					sensor2 = true;
					sensor3 = false;
				}
			} else if (rel13 && rel31) {
				if (rel23 && rel32) { //todos serão usados
					sensor1 = true;
					sensor2 = true;
					sensor3 = true;
				} else { //apenas o sensor 1 e 3 serão usados
					sensor1 = true;
					sensor2 = false;
					sensor3 = true;
				}
			} else if (rel23 && rel32) { //apenas o sensor 2 e 3 serão usados
				sensor1 = false;
				sensor2 = true;
				sensor3 = true;
			} else { //nenhum será usado
				sensor1 = false;
				sensor2 = false;
				sensor3 = false;
			}

		}
	}
}

void DataVerifier::verifyPressureInVals(vector<float> &vals) {
	if (vals.size() == 0)
		return;
	vector<float>::iterator it = vals.begin();

	while (it != vals.end()) {
		if (verifyPressure(*it))
			it++;
		else
			vals.erase(it);
	}
}

void DataVerifier::verifyTemperatureInVals(vector<float> &vals) {
	if (vals.size() == 0)
		return;
	vector<float>::iterator it = vals.begin();

	while (it != vals.end()) {
		if (verifyTemperature(*it))
			it++;
		else
			vals.erase(it);
	}
}
