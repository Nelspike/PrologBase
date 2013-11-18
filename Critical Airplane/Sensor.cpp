/**
 * Disclaimer
 *
 * Todo este c�digo foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer c�digo que se encontre de forma alguma id�ntico ou igual � plagiado.
 */

#include "Sensor.h" //qualquer coment�rio acerca das fun��es encontra-se no Sensor.h

//Zona de Construtores
Sensor::Sensor() : totalValue(DEFAULT_SENSOR_CONST), staticValue(DEFAULT_SENSOR_CONST),
					broken(false), stuckAtCount(0), brokenCount(0), brokenFor(0), disabled(false) {}
Sensor::Sensor(float total, string t, float staticV) : totalValue(total), staticValue(staticV), type(t), broken(false),
														stuckAtCount(0), brokenCount(0), brokenFor(0), disabled(false) {}
//Fim da Zona de Construtores

//Zona de Fun��es
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
		if(brokenCount == 2) { //se j� est� avariado pela segunda vez, vai ficar bloqueado
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
	if(!isDisabled()) { //se n�o est� bloqueado
		if(isBroken()) { //se estiver avariado
			if(getBrokenFor() == 10) setBroken(false); //ao fim de 10 itera��es, volta a estar activo
			else incrementBrokenFor(); //sen�o, incrementa por um as itera��es de h� quanto temp est� bloquado
		}
	}
}

void Sensor::setTotalValue(float val) {
	totalValue = val;
}

void Sensor::setStaticValue(float val) {
	staticValue = val;
}
