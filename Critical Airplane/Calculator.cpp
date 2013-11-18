/**
 * Disclaimer
 *
 * Todo este código foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer código que se encontre de forma alguma idêntico ou igual é plagiado.
 */

#include "Calculator.h" //qualquer comentário acerca das funções encontra-se no Calculator.h

bool Calculator::percentualRelation(float first, float second) {
	first = abs(first);
	second = abs(second);
	float ten1 = first*PERCENTAGE;
	float ten2 = second*PERCENTAGE;
	bool rel12, rel21;
	rel12 = (second-ten2 <= first) && (first <= second+ten2);
	rel21 = (first-ten1 <= second) && (second <= first+ten1);

	if(!(rel12 && rel21))
		return false;

	return true;
}

float Calculator::calcDynamicPressure(float total, float staticPressure) {
	return total - staticPressure;
}

float Calculator::getKelvin(float celsius) {
	return celsius + 273.15;
}

float Calculator::calcMachNumber(float sPressure, float dPressure) {
	float pressureCalc = (float) dPressure/sPressure;
	pressureCalc += 1.0f;
	float power = (float) pow((float)pressureCalc, (2.0f/7.0f));
	return (float) sqrt(5*(power-1.0f));
}

float Calculator::calcOAT(float temperature, float mach) {
	float preCalc = (float) (1.0f + 0.2*mach*mach);
	return (float) (temperature/preCalc);
}

float Calculator::calcTAS(float mach, float OAT) {
	return AZERO * mach * sqrt((float)(OAT/TZERO));
}

void Calculator::brokenAugment(Sensor *sensor, int &count, float &avg, int val) {
	if(!sensor->isDisabled()) {
		if(!sensor->isBroken()) {
			count++;
			if(val == 0)
				avg += sensor->getTotalValue();
			else
				avg += sensor->getStaticValue();
		}
	}
}

float* Calculator::getUnbrokenPressureAverage(Sensor *presSensor1, Sensor *presSensor2, Sensor *presSensor3, vector<float> otherTotal, vector<float> otherStatic) {
	int nCountTotal = 0, nCountStatic = 0;
	float sumTotal = 0.0f, sumStatic = 0.0f;
	float otherTotalSum = 0.0f, otherStaticSum = 0.0f;
	int nOtherCountTotal = 0, nOtherCountStatic = 0;

	brokenAugment(presSensor1, nCountTotal, sumTotal, 0);
	brokenAugment(presSensor1, nCountStatic, sumStatic, 1);
	brokenAugment(presSensor2, nCountTotal, sumTotal, 0);
	brokenAugment(presSensor2, nCountStatic, sumStatic, 1);
	brokenAugment(presSensor3, nCountTotal, sumTotal, 0);
	brokenAugment(presSensor3, nCountStatic, sumStatic, 1);

	if(nCountTotal == 0 || nCountStatic == 0)
		return (float*) calloc (2,sizeof(float));

	float *result = new float[2];

	for(unsigned int i = 0; i < otherTotal.size(); i++) {
		otherTotalSum += otherTotal[i];
		nOtherCountTotal++;
	}

	for(unsigned int i = 0; i < otherStatic.size(); i++) {
		otherStaticSum += otherStatic[i];
		nOtherCountStatic++;
	}

	float firstAvgTotal = sumTotal/(float)nCountTotal;
	float secondAvgTotal = 0.0f;
	if(otherTotalSum != 0.0f)
		secondAvgTotal = otherTotalSum/(float)nOtherCountTotal;

	if(secondAvgTotal != 0.0f) {
		if(percentualRelation(firstAvgTotal, secondAvgTotal)) {
			sumTotal += otherTotalSum;
			nCountTotal += nOtherCountTotal;
		}
	}

	float firstAvgStatic = sumStatic/(float)nCountStatic;
	float secondAvgStatic = 0.0f;
	if(otherStaticSum != 0.0f)
		secondAvgStatic = otherStaticSum/(float)nOtherCountStatic;

	if(secondAvgStatic != 0.0f) {
		if(percentualRelation(firstAvgStatic, secondAvgStatic)) {
			sumStatic += otherStaticSum;
			nCountStatic += nOtherCountStatic;
		}
	}

	result[0] = sumTotal/(float)nCountTotal;
	result[1] = sumStatic/(float)nCountStatic;

	return result;
}

float Calculator::getUnbrokenTemperatureAverage(Sensor *tempSensor1, Sensor *tempSensor2, vector<float> otherTemp) {
	int nCountTotal = 0, nOther = 0;;
	float sumTotal = 0.0f, otherTotal = 0.0f;

	for(unsigned int i = 0; i < otherTemp.size(); i++) {
		otherTotal += otherTemp[i];
		nOther++;
	}

	brokenAugment(tempSensor1, nCountTotal, sumTotal, 0);
	brokenAugment(tempSensor2, nCountTotal, sumTotal, 0);

	if(nCountTotal == 0)
		return -9000.0f;

	float first = sumTotal/(float)nCountTotal;
	float second = 0.0f;

	if(second != 0.0f)
		second = otherTotal/(float)nOther;

	if(second != 0.0f) {
		if(percentualRelation(first, second)) {
			sumTotal += otherTotal;
			nCountTotal += nOther;
		}
	}

	return sumTotal/(float)nCountTotal;
}

bool Calculator::isnan_float (float f) {
	return (f != f);
}
