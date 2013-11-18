#include "Data.h"
#include <iostream>
#include <ctime>
using namespace std;

Data::Data() {dia=0;mes=0;ano=0;horas=0;minutos=0;}

Data::Data(string s) { // m-d-a h:m
	int temp=0;
	int campo=0;
	for (int n=0;n<s.size();n++) {
		if (s[n]=='/' || s[n]==' ' || s[n]==':' || s[n]=='-') {
			switch (campo) {
				case 0:setMes(temp);temp=0;break;
				case 1:setDia(temp);temp=0;break;
				case 2:setAno(temp);temp=0;break;
				case 3:setHoras(temp);temp=0;break;
				case 4:setMinutos(temp);temp=0;break;
			}
			temp=0;campo++;
		}
		else {temp*=10;temp+=s[n]-'0';}
	}
	if (campo==4) {setMinutos(temp);}
}

int Data::getDia() const {return dia;}
int Data::getMes() const {return mes;}
int Data::getAno() const {return ano;}
int Data::getHoras() const {return horas;}
int Data::getMinutos() const {return minutos;}
void Data::setDia(int d) {dia=d;}
void Data::setMes(int m) {mes=m;}
void Data::setAno(int a) {ano=a;}
void Data::setHoras(int h) {horas=h;}
void Data::setMinutos(int m) {minutos=m;}
bool Data::operator==(const Data &d) const {
	return	dia==d.getDia() &&
			mes==d.getMes() &&
			ano==d.getAno() &&
			horas==d.getHoras() &&
			minutos==d.getMinutos();
}

bool Data::operator<(const Data &d) const{
	return 	ano<d.getAno() ||
			(mes<d.getMes() && ano==d.getAno() ) ||
			(dia<d.getDia() && mes==d.getMes() && ano==d.getAno() ) ||
			(horas<d.getHoras() && dia==d.getDia() && mes==d.getMes() && ano==d.getAno() ) ||
			(minutos<d.getMinutos() && horas==d.getHoras() && dia==d.getDia() && mes==d.getMes() && ano==d.getAno() );
}

bool Data::operator<=(const Data &d) const{
	return 	*this<d || *this==d;
}

int Data::operator-(const Data &d) const{
	struct tm temp_time;
	temp_time.tm_sec=0;temp_time.tm_min=minutos;temp_time.tm_hour=horas;
	temp_time.tm_mday=dia;temp_time.tm_mon=mes;temp_time.tm_year=ano-1900;
	time_t t1=mktime(&temp_time);

	temp_time.tm_sec=0;temp_time.tm_min=d.getMinutos();temp_time.tm_hour=d.getHoras();
	temp_time.tm_mday=d.getDia();temp_time.tm_mon=d.getMes();temp_time.tm_year=d.getAno()-1900;
	time_t t2=mktime(&temp_time);
	int ret = (int) ((t1-t2)/60);
	if (ret<0) {
		cout << "BADRET" << endl;

	}
	return ret;
}

