/**
 * Disclaimer
 *
 * Todo este código foi produzido somente por mim,
 * Nelson Oliveira - ei09027@fe.up.pt
 *
 * Todo e qualquer código que se encontre de forma alguma idêntico ou igual é plagiado.
 */

#include "Auxiliary.h" //qualquer comentário acerca das funções encontra-se no Auxiliary.h

void Auxiliary::splitAux(const string &s, char delim, vector<string> &elems) {
	stringstream ss(s);
	string item;
	while (std::getline(ss, item, delim)) {
		elems.push_back(item);
	}
}

vector<string> Auxiliary::split(string s, char delim) {
	vector<string> elems;
	splitAux(s, delim, elems);
	return elems;
}

char* Auxiliary::convertString(string s) {
	char *stringConv = new char[s.size()+1];
	copy(s.begin(), s.end(), stringConv);
	stringConv[s.size()] = '\0';
	return stringConv;
}

vector<string> Auxiliary::readFile(string filename) {
	ifstream file(convertString(filename));
	vector<string> ret;
	string line;

	if(file.is_open()) {
		while (file.good()) {
			getline (file,line);
			ret.push_back(line);
		}
		file.close();
	}

	return ret;
}

void Auxiliary::writeFile(string file, string message) {
  ofstream myfile;
  myfile.open(convertString(file), std::ios_base::app);
  myfile << message;
  myfile.close();
}

void Auxiliary::getFromVecs(vector<float> &val1, vector<float> &val2, vector<float> &val3, vector<string> lineValues) {
	for(unsigned int i = 0; i < lineValues.size(); i++) {
		string value = lineValues[i];
		if(value.find(":") != string::npos) {
			vector<string> res = split(value, ':');
			if(res[0] != "--")
				val1.push_back(atof(convertString(res[0])));

			if(res[1] != "--")
				val2.push_back(atof(convertString(res[1])));
		}
		else {
			if(value != "--")
				val3.push_back(atof(convertString(value)));
		}
	}
}


vector<float>* Auxiliary::getValues(vector<vector<string> > val, int currentLine) {
	vector<float> otherTotalPressureValues, otherStaticPressureValues, otherTempValues;
	for(unsigned int i = 0; i < val.size(); i++) {
		string line = val[i][currentLine];
		string lineNumbers = split(line, ' ')[1];
		vector<string> lineValues = split(lineNumbers, ',');
		getFromVecs(otherTotalPressureValues, otherStaticPressureValues, otherTempValues, lineValues);
	}


	vector<float> *vectorRet = new vector<float>;
	vectorRet[0] = otherTotalPressureValues;
	vectorRet[1] = otherStaticPressureValues;
	vectorRet[2] = otherTempValues;

	return vectorRet;
}

void Auxiliary::clearRepeatedValues(vector<float> &toClean) {
	set<float> s;
	unsigned size = toClean.size();
	for( unsigned i = 0; i < size; ++i ) s.insert( toClean[i] );
	toClean.assign( s.begin(), s.end() );
}

