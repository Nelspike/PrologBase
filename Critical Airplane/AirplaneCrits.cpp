/**
 * Disclaimer
 *
 * Todo este c�digo foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer c�digo que se encontre de forma alguma id�ntico ou igual � plagiado.
 */

#include "defines.h"
#include "Calculator.h"
#include "Sensor.h"
#include "Auxiliary.h"
#include "dataVerifier.h"

//Defini��es de vari�veis globais
Auxiliary *auxFunc = new Auxiliary;
DataVerifier *dVerify = new DataVerifier();
Calculator *c;
sint nVariants = 4;
vector<short int> variantVersions;

/**
 * Fun��o main() do programa.
 *
 * Esta fun��o tem com objectivo executar o ciclo principal onde:
 * -
 */
int main(int argc, char *argv[] ) {

	//Verifica��o do N�mero de Argumentos
	if(argc != 2) {
		cout << "Wrong arguments have been passed! One argument with the filename is needed." << endl;
		return -1; //retornar -1 se o programa falhou
	}

	//Argumento 1 da linha de comandos (vulgo, nome do ficheiro)
	string argument = argv[1];

	//Encontrar .txt na string do argumento
	uint found = argument.find(".txt");
	if(found == string::npos) {
		cout << "Invalid filename!" << endl;
		return -1; //retornar -1 se o programa falhou
	}

	//Encontrar todas as variantes que se encontram a funcionar no sistema
	char programCount = argument[0];
	sint nVariant;
	for(short int i = 1; i <= nVariants; i++)
		variantVersions.push_back(i);

	stringstream sstream, sstream1, intStream;

	//Passar o n�mero da variante que este programa se encontra a correr
	//Este n�mero � o que consta do nome do .txt que � passado como argumento
	intStream << programCount;
	intStream >> nVariant;

	//Apagar da lista de vers�es de variantes a pr�pia
	vector<sint>::iterator it = variantVersions.begin()+(nVariant-1);
	variantVersions.erase(it);

	//Strings de comando e de output para os ficheiros
	sstream << "command" << programCount << ".txt";
	string commandName = sstream.str();
	sstream1 << "out" << programCount << ".txt";
	string outName = sstream1.str();

	//Zona de leitura de todos os valores de todas as variantes
	//De notar que todas as variantes l�em os valores umas das outras de modos a saber que outros valores as variantes possuem
	vector<string> values = auxFunc->readFile(argument), others1, others2, others3;
	vector<vector<string> > valuesAux;

	for(unsigned int i = 0; i < variantVersions.size(); i++) {
		stringstream auxStream;
		auxStream << variantVersions[i] << ".txt";
		valuesAux.push_back(auxFunc->readFile(auxStream.str()));
	}
	//Fim da Zona de Leitura

	uint currentLine = 0;
	Sensor *presSensor1 = new Sensor(), *presSensor2 = new Sensor(), *presSensor3 = new Sensor(), *tempSensor1 = new Sensor(), *tempSensor2 = new Sensor();
	//Apontador onde todos os apontadores para os objectos Sensor desta variante est�o guardados
	//Assim, � garantida menos aloca��o de mem�ria do que a produ��o de c�pias integrais de cada objecto
	Sensor** sensors = (Sensor**) malloc(5*sizeof(Sensor*));
	sensors[0] = presSensor1;
	sensors[1] = presSensor2;
	sensors[2] = presSensor3;
	sensors[3] = tempSensor1;
	sensors[4] = tempSensor2;

	//Ciclo principal do programa, onde todos os calculos e verifica��es v�o sendo efectuados
	//Este ciclo executar� tantas vezes quanto o n�mero de linhas do .txt da variante em quest�o
	while(currentLine < values.size()) {
		sint expect = currentLine+1;
		sint real = 0;
		string command;

		if(OUTSTREAM)
			cout << "---------- ITERATION " << expect << " ----------" << endl;

		//Enquanto o n�mero de linhas do ficheiro de comando do votador n�o for igual � linha actual, o ciclo fica aqui preso
		//Ou seja, � esperado um comando do votador para que esta variante fa�a os seus c�lculos
		while(real < expect)
			real = auxFunc->readFile(auxFunc->convertString(commandName)).size();

		if(OUTSTREAM)
			cout << "I'll do my calculations now..." << endl;

		//A linha corrente que est� a ser lida e o seu split(divis�o da string em tokens mais pequenos) respectivo
		string line = values[currentLine];
		string lineNumbers = auxFunc->split(line, ' ')[1];

		//Zona de Verifica��o dos sensores da variante
		vector<string> lineValues = auxFunc->split(lineNumbers, ','); //Split dos valores por v�rgulas
		sensors = dVerify->sensorVerification(sensors[0], sensors[1], sensors[2], sensors[3], sensors[4], lineValues); //Verifica��o dos sensores
		//Fim da Zona de Verifica��o dos sensores da variante

		//Zona de Verifica��o de valores de outras variantes
		vector<float> otherTotalPressureValues, otherStaticPressureValues, otherTempValues;
		for(uint i = 0; i < valuesAux.size(); i++) {
			string line = valuesAux[i][currentLine];
			string lineNumbers = auxFunc->split(line, ' ')[1];
			vector<string> lineValues = auxFunc->split(lineNumbers, ',');
			auxFunc->getFromVecs(otherTotalPressureValues, otherStaticPressureValues, otherTempValues, lineValues); //push_back aos vectores com mais valores para serem analisados
			dVerify->verifyPressureInVals(otherTotalPressureValues); //verifica��o das press�es totais
			dVerify->verifyPressureInVals(otherStaticPressureValues); //verifica��o das press�es totais
			dVerify->verifyTemperatureInVals(otherTempValues); //verifica��o das temperaturas
			dVerify->verifySensorsInVal(otherTotalPressureValues); //verifica��o de valores entre sensores de press�o totais
			dVerify->verifySensorsInVal(otherStaticPressureValues); //verifica��o de valores entre sensores de press�o est�ticos
			dVerify->verifySensorsInVal(otherTempValues); //verifica��o de valores entre sensores de temperature
		}
		//elimina��o de repetidos nos vectores
		auxFunc->clearRepeatedValues(otherTotalPressureValues);
		auxFunc->clearRepeatedValues(otherStaticPressureValues);
		auxFunc->clearRepeatedValues(otherTempValues);
		//Fim da Zona de Verifica��o de valores de outras variantes

		//c�lculo de m�dias (mecanismo de adjudica��o desta variante) entres os valores conseguidos e v�lidos de todos os sensores
		float *averages = c->getUnbrokenPressureAverage(sensors[0], sensors[1], sensors[2], otherTotalPressureValues, otherStaticPressureValues);
		float totalPressure = averages[0], staticPressure = averages[1], temperature = c->getUnbrokenTemperatureAverage(sensors[3], sensors[4], otherTempValues);

		//Verifica��o do estado dos sensores (se podem voltar ao activo)
		presSensor1->checkBroken();
		presSensor2->checkBroken();
		presSensor3->checkBroken();
		tempSensor1->checkBroken();
		tempSensor2->checkBroken();
		//Fim da Verifica��o do estado dos sensores (se podem voltar ao activo)

		//Zona de C�lculos
		if(!c->isnan_float(totalPressure) && !(c->isnan_float(staticPressure))
				&& !(c->isnan_float(temperature)) && totalPressure != 0.0f
				&& staticPressure != 0.0f && temperature != -9000.0f) { //feita uma verifica��o se todos os valores representam realmente um n�mero, ou se est�o em condi��es v�lidas para serem c�lculados
			float machNumber = c->calcMachNumber(staticPressure, c->calcDynamicPressure(totalPressure, staticPressure)); //c�lculo do Mach Number
			float OAT = c->calcOAT(c->getKelvin(temperature), machNumber); //c�lculo da Open Air Temperature
			float TAS = c->calcTAS(machNumber, OAT); //c�lculo da True Air Speed

			if(OUTSTREAM)
				cout << expect << "#: " << TAS << "," << OAT << endl;

			//formata��o da linha a escrever num ficheiro de output
			stringstream ss;
			ss << expect << "#: " << TAS << "," << OAT;
			string result = ss.str();

			if(OUTSTREAM) {
				cout << "Total Average: " << totalPressure << endl;
				cout << "Static Average: " << staticPressure << endl;
				cout << "Temperature: " << temperature << endl;
				cout << "Result is: " << result << endl;
				cout << "Mach Number: " << machNumber << endl;
			}

			if(currentLine != 0)
				result = '\n' + result;

			auxFunc->writeFile(outName, result); //Escrita dos valores para um ficheiro de output, para que o votador os possa consultar
		}
		else { //caso a condi��o acima falhe, os valores a passar para o votador s�o nulos
			if(OUTSTREAM)
				cout << expect << "#: Cannot precise any values" << endl;

			//formata��o da linha a escrever num ficheiro de output
			stringstream ss;
			ss << expect << "#: --" << "," << "--";
			string result = ss.str();

			if(currentLine != 0)
				result = '\n' + result;

			auxFunc->writeFile(outName, result); //Escrita dos valores para um ficheiro de output, para que o votador os possa consultar
		}

		if(OUTSTREAM)
			cout << "---------- END OF ITERATION " << expect << " ----------" << endl << endl;

		currentLine++;
	}

	//Zona de liberta��o de mem�ria
	delete[] sensors;
	delete auxFunc;
	delete c;
	delete dVerify;
	//Fim da Zona de liberta��o de mem�ria

	return 0;
}
