/**
 * Disclaimer
 *
 * Todo este código foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer código que se encontre de forma alguma idêntico ou igual é plagiado.
 */

#include "Sensor.h" //qualquer comentário acerca das funções encontra-se no Sensor.h

//Zona de Construtores
Sensor::Sensor() : totalValue(DEFAULT_SENSOR_CONST), staticValue(DEFAULT_SENSOR_CONST),
					broken(false), stuckAtCount(0), brokenCount(0), brokenFor(0), disabled(false) {}
Sensor::Sensor(float total, string t, float staticV) : totalValue(total), staticValue(staticV), type(t), broken(false),
														stuckAtCount(0), brokenCount(0), brokenFor(0), disabled(false) {}
//Fim da Zona de Construtores

//Zona de Funções
float Sensor::getTotalValue() {
	return totalValue;
}

float Sensor::getStaticValue() {
	return staticValue;
}

string Sensor::getType() {
	return type;
}

bool Sensor::isBroken() {
	return broken;
}

bool Sensor::isDisabled() {
	return disabled;
}

void Sensor::setBroken(bool broke) {
	broken = broke;
	if(broke) {
		brokenCount+=1;
		if(brokenCount == 2) { //se já está avariado pela segunda vez, vai ficar bloqueado
			if(OUTSTREAM) cout << endl << "Warning: This sensor will be disabled from here onwards." << endl;
			disabled = true;
		}
		else {
			if(OUTSTREAM) cout << "Will be broken for 10 iterations!" << endl;
		}
	}
	else {
		if(OUTSTREAM) cout << "Sensor is operational again!" << endl;
	}
}

void Sensor::checkBroken() {
	if(!isDisabled()) { //se não está bloqueado
		if(isBroken()) { //se estiver avariado
			if(getBrokenFor() == 10) setBroken(false); //ao fim de 10 iterações, volta a estar activo
			else incrementBrokenFor(); //senão, incrementa por um as iterações de há quanto temp está bloquado
		}
	}
}

void Sensor::setTotalValue(float val) {
	totalValue = val;
}

void Sensor::setStaticValue(float val) {
	staticValue = val;
}
