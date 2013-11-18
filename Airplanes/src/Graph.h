/*
 * Graph.h
 */
#ifndef GRAPH_H_
#define GRAPH_H_

#include <vector>
#include <queue>
#include <list>
#include <limits.h>
#include <cmath>
#include <string>
#include <sstream>
#include <algorithm>
#include "Data.h"
#include "graphviewer.h"
#include "Avioes.h"
using namespace std;

template <class T> class Edge;
template <class T> class Graph;


const int SCREEN_W=1024;
const int SCREEN_H=768;
const int MULT_SIZE=2;
const int MARGIN=15;

const int NOT_VISITED = 0;
const int BEING_VISITED = 1;
const int DONE_VISITED = 2;
const int INT_INFINITY = INT_MAX;

/*
 * ================================================================================================
 * Struct VertexInfo
 * ================================================================================================
 */

struct VertexInfo {
	string local;
	Data data;
	int tipo; // 0 - normal
			  // 1 - inicio
			  // 2 - fim
			  // 3 - partida
			  // 4 - chegada
	double mtc;
	double mintc;
	double utc;
	int minAvioes; // Minimo de avioes que tem de estar no vertice
	vector <Aviao *> avioes;
};
bool operator==(const VertexInfo &v1,const VertexInfo &v2) {
	return v1.local.compare(v2.local)==0 && v1.data==v2.data && v1.tipo==v2.tipo;
}
bool operator!=(const VertexInfo &v1,const VertexInfo &v2) {
	return !(v1==v2);
}
bool operator<(const VertexInfo &v1,const VertexInfo &v2) {
	if (v1.tipo==4 && v2.tipo!=4) {return false;}
	else if (v1.tipo!=4 && v2.tipo==4) {return true;}

	if (v1.data==v2.data) {return v1.local<v2.local;}
	return v1.data<v2.data;
}


string printVertexInfo(VertexInfo vi) {
	stringstream output;
	output << vi.local;
	output << " (" << vi.data.getDia() << "/" << vi.data.getMes() << "/" << vi.data.getAno();
	output << " ";
	if (vi.data.getHoras()<10) {output << 0;}
	output << vi.data.getHoras() << ":";
	if (vi.data.getMinutos()<10) {output << 0;}
	output << vi.data.getMinutos() << ")";
	if (vi.tipo==2) {output << " Dur.Min.: " << vi.mintc << "m";}
	return output.str();
}
/*
 * ================================================================================================
 * Class Vertex
 * ================================================================================================
 */
template <class T>
class Vertex {
	T info;
	vector<Edge<T>  > adj;
	bool visited;
	bool processing;
	int indegree;
	int dist;
public:

	Vertex(T in);
	friend class Graph<T>;

	void addEdge(Vertex<T> *dest, double w);
	bool removeEdgeTo(Vertex<T> *d);

	T getInfo() const;
	void setInfo(T info);

	int getDist() const;
	int getIndegree() const;

	bool operator<(const Vertex<T> vertex);

	Vertex* path;
};


template <class T>
struct vertex_greater_than {
    bool operator()(Vertex<T> * a, Vertex<T> * b) const {
        return a->getDist() > b->getDist();
    }
};


template <class T>
bool Vertex<T>::removeEdgeTo(Vertex<T> *d) {
	d->indegree--;
	typename vector<Edge<T> >::iterator it= adj.begin();
	typename vector<Edge<T> >::iterator ite= adj.end();
	while (it!=ite) {
		if (it->dest == d) {
			adj.erase(it);
			return true;
		}
		else it++;
	}
	return false;
}

template <class T>
Vertex<T>::Vertex(T in): info(in), visited(false), processing(false), indegree(0), dist(0) {
	path = NULL;
}


template <class T>
void Vertex<T>::addEdge(Vertex<T> *dest, double w) {
	Edge<T> edgeD(dest,w);
	adj.push_back(edgeD);
}

template <class T>
T Vertex<T>::getInfo() const {
	return this->info;
}

template <class T>
int Vertex<T>::getDist() const {
	return this->dist;
}


template <class T>
void Vertex<T>::setInfo(T info) {
	this->info = info;
}

template <class T>
int Vertex<T>::getIndegree() const {
	return this->indegree;
}




/* ================================================================================================
 * Class Edge
 * ================================================================================================
 */
template <class T>
class Edge {
	Vertex<T> * dest;
	double weight;
public:
	Edge(Vertex<T> *d, double w);
	friend class Graph<T>;
	friend class Vertex<T>;
};

template <class T>
Edge<T>::Edge(Vertex<T> *d, double w): dest(d), weight(w){}





/* ================================================================================================
 * Class Graph
 * ================================================================================================
 */
template <class T>
class Graph {
	vector<Vertex<T> *> vertexSet;
	void dfs(Vertex<T> *v, vector<T> &res) const;

	int numCycles;
	void dfsVisit(Vertex<T> *v);
	void dfsVisit();
	void getPathTo(Vertex<T> *origin, list<T> &res);

	int ** pathArray;
	int ** pathArrayAux;

public:
	bool addVertex(const T &in);
	bool addEdge(const T &sourc, const T &dest, double w);
	bool removeVertex(const T &in);
	bool removeEdge(const T &sourc, const T &dest);
	void clearGraph();
	vector<T> dfs() const;
	vector<T> bfs(Vertex<T> *v) const;
	int maxNewChildren(Vertex<T> *v, T &inf) const;
	vector<Vertex<T> * > getVertexSet() const;
	int getNumVertex() const;

	Vertex<T>* getVertex(const T &v) const;
	int getVertexIndex(Vertex<T>* v) const;
	void resetIndegrees();
	vector<Vertex<T>*> getSources() const;
	int getNumCycles();
	vector<T> topologicalOrder();
	vector<T> getPath(const T &origin, const T &dest);
	void unweightedShortestPath(const T &v);
	bool isDAG();

	void bellmanFordShortestPath(const T &s);
	void dijkstraShortestPath(const T &s);
	void floydWarshallShortestPath();
	int edgeCost(int vOrigIndex, int vDestIndex);
	vector<T> getfloydWarshallPath(const T &origin, const T &dest);
	void getfloydWarshallPathAux(int index1, int index2, vector<T> & res);

	void calculateMTC(const T &s);
	void calculateMinTC(const T &s);
	void calculateUTC(const T &e);
	void calculateMinAvioes(const T &s,const T &e);
	void associaAvioes(Vertex<T> *v,vector <Aviao *> avioesDisponiveis);


	void sortVertex();
	void show(GraphViewer *gv);
	void showPath(GraphViewer *gv);
	void showTopological(GraphViewer *gv);
	void showAvioes(GraphViewer *gv,vector <Aviao> &avioes);
	void showGantt(GraphViewer *gv);
};

template <class T>
int Graph<T>::getNumVertex() const {
	return vertexSet.size();
}
template <class T>
vector<Vertex<T> * > Graph<T>::getVertexSet() const {
	return vertexSet;
}

template <class T>
int Graph<T>::getNumCycles() {
	numCycles = 0;
	dfsVisit();
	return this->numCycles;
}

template <class T>
bool Graph<T>::isDAG() {
	return (getNumCycles() == 0);
}

template <class T>
bool Graph<T>::addVertex(const T &in) {
	typename vector<Vertex<T>*>::iterator it= vertexSet.begin();
	typename vector<Vertex<T>*>::iterator ite= vertexSet.end();
	for (; it!=ite; it++)
		if ((*it)->info == in) return false;
	Vertex<T> *v1 = new Vertex<T>(in);
	vertexSet.push_back(v1);
	return true;
}

template <class T>
bool Graph<T>::removeVertex(const T &in) {
	typename vector<Vertex<T>*>::iterator it= vertexSet.begin();
	typename vector<Vertex<T>*>::iterator ite= vertexSet.end();
	for (; it!=ite; it++) {
		if ((*it)->info == in) {
			Vertex<T> * v= *it;
			vertexSet.erase(it);
			typename vector<Vertex<T>*>::iterator it1= vertexSet.begin();
			typename vector<Vertex<T>*>::iterator it1e= vertexSet.end();
			for (; it1!=it1e; it1++) {
				(*it1)->removeEdgeTo(v);
			}

			typename vector<Edge<T> >::iterator itAdj= v->adj.begin();
			typename vector<Edge<T> >::iterator itAdje= v->adj.end();
			for (; itAdj!=itAdje; itAdj++) {
				itAdj->dest->indegree--;
			}
			delete v;
			return true;
		}
	}
	return false;
}

template <class T>
bool Graph<T>::addEdge(const T &sourc, const T &dest, double w) {
	typename vector<Vertex<T>*>::iterator it= vertexSet.begin();
	typename vector<Vertex<T>*>::iterator ite= vertexSet.end();
	int found=0;
	Vertex<T> *edgeS, *edgeD;
	while (found!=2 && it!=ite ) {
		if ( (*it)->info == sourc )
			{ edgeS=*it; found++;}
		if ( (*it)->info == dest )
			{ edgeD=*it; found++;}
		it ++;
	}
	if (found!=2) return false;
	edgeD->indegree++;

	edgeS->addEdge(edgeD,w);

	return true;
}

template <class T>
bool Graph<T>::removeEdge(const T &sourc, const T &dest) {
	typename vector<Vertex<T>*>::iterator it= vertexSet.begin();
	typename vector<Vertex<T>*>::iterator ite= vertexSet.end();
	int found=0;
	Vertex<T> *edgeS, *edgeD;
	while (found!=2 && it!=ite ) {
		if ( (*it)->info == sourc )
			{ edgeS=*it; found++;}
		if ( (*it)->info == dest )
			{ edgeD=*it; found++;}
		it ++;
	}
	if (found!=2)
		return false;

	edgeD->indegree--;

	return edgeS->removeEdgeTo(edgeD);
}
template <class T>
void Graph<T>::clearGraph() {
	for (size_t n=0;n<vertexSet.size();n++) {
		delete vertexSet[n];
	}
	vertexSet.clear();
}


template <class T>
vector<T> Graph<T>::dfs() const {
	typename vector<Vertex<T>*>::const_iterator it= vertexSet.begin();
	typename vector<Vertex<T>*>::const_iterator ite= vertexSet.end();
	for (; it !=ite; it++)
		(*it)->visited=false;
	vector<T> res;
	it=vertexSet.begin();
	for (; it !=ite; it++)
	    if ( (*it)->visited==false )
	    	dfs(*it,res);
	return res;
}

template <class T>
void Graph<T>::dfs(Vertex<T> *v,vector<T> &res) const {
	v->visited = true;
	res.push_back(v->info);
	typename vector<Edge<T> >::iterator it= (v->adj).begin();
	typename vector<Edge<T> >::iterator ite= (v->adj).end();
	for (; it !=ite; it++)
	    if ( it->dest->visited == false ){
	    	dfs(it->dest, res);
	    }
}

template <class T>
vector<T> Graph<T>::bfs(Vertex<T> *v) const {
	vector<T> res;
	queue<Vertex<T> *> q;
	q.push(v);
	v->visited = true;
	while (!q.empty()) {
		Vertex<T> *v1 = q.front();
		q.pop();
		res.push_back(v1->info);
		typename vector<Edge<T> >::iterator it=v1->adj.begin();
		typename vector<Edge<T> >::iterator ite=v1->adj.end();
		for (; it!=ite; it++) {
			Vertex<T> *d = it->dest;
			if (d->visited==false) {
				d->visited=true;
				q.push(d);
			}
		}
	}
	return res;
}

template <class T>
int Graph<T>::maxNewChildren(Vertex<T> *v, T &inf) const {
	vector<T> res;
	queue<Vertex<T> *> q;
	queue<int> level;
	int maxChildren=0;
	inf =v->info;
	q.push(v);
	level.push(0);
	v->visited = true;
	while (!q.empty()) {
		Vertex<T> *v1 = q.front();
		q.pop();
		res.push_back(v1->info);
		int l=level.front();
		level.pop(); l++;
		int nChildren=0;
		typename vector<Edge<T> >::iterator it=v1->adj.begin();
		typename vector<Edge<T> >::iterator ite=v1->adj.end();
		for (; it!=ite; it++) {
			Vertex<T> *d = it->dest;
			if (d->visited==false) {
				d->visited=true;
				q.push(d);
				level.push(l);
				nChildren++;
			}
		}
		if (nChildren>maxChildren) {
			maxChildren=nChildren;
			inf = v1->info;
		}
	}
	return maxChildren;
}

template <class T>
Vertex<T>* Graph<T>::getVertex(const T &v) const {
	for(unsigned int i = 0; i < vertexSet.size(); i++)
		if (vertexSet[i]->info == v) return vertexSet[i];
	return NULL;
}

template <class T>
int Graph<T>::getVertexIndex(Vertex<T>* v) const {
	for(unsigned int i = 0; i < vertexSet.size(); i++)
		if (vertexSet[i] == v) return i;
	return -1;
}

template<class T>
void Graph<T>::resetIndegrees() {
	//colocar todos os indegree em 0;
	for(unsigned int i = 0; i < vertexSet.size(); i++) vertexSet[i]->indegree = 0;

	//actualizar os indegree
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		//percorrer o vector de Edges, e actualizar indegree
		for(unsigned int j = 0; j < vertexSet[i]->adj.size(); j++) {
			vertexSet[i]->adj[j].dest->indegree++;
		}
	}
}


template<class T>
vector<Vertex<T>*> Graph<T>::getSources() const {
	vector< Vertex<T>* > buffer;
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		if( vertexSet[i]->indegree == 0 ) buffer.push_back( vertexSet[i] );
	}
	return buffer;
}


template <class T>
void Graph<T>::dfsVisit() {
	typename vector<Vertex<T>*>::const_iterator it= vertexSet.begin();
	typename vector<Vertex<T>*>::const_iterator ite= vertexSet.end();
	for (; it !=ite; it++)
		(*it)->visited=false;
	it=vertexSet.begin();
	for (; it !=ite; it++)
	    if ( (*it)->visited==false ){
	    	(*it)->processing = true;
	    	dfsVisit(*it);
	    	(*it)->processing = false;
	    }
}

template <class T>
void Graph<T>::dfsVisit(Vertex<T> *v) {
	v->visited = true;
	typename vector<Edge<T> >::iterator it= (v->adj).begin();
	typename vector<Edge<T> >::iterator ite= (v->adj).end();
	for (; it !=ite; it++) {
		if ( it->dest->processing == true) numCycles++;
	    if ( it->dest->visited == false ){
	    	dfsVisit(it->dest);
	    }
	}
}

template<class T>
vector<T> Graph<T>::topologicalOrder() {
	//vector com o resultado da ordenacao
	vector<T> res;

	//verificar se � um DAG
	if( getNumCycles() > 0 ) {
		cout << "Ordenacao Impossivel!" << endl;
		return res;
	}

	//garantir que os "indegree" estao inicializados correctamente
	this->resetIndegrees();

	queue<Vertex<T>*> q;

	vector<Vertex<T>*> sources = getSources();
	while( !sources.empty() ) {
		q.push( sources.back() );
		sources.pop_back();
	}
	//cout << q.size() << endl;

	//processar fontes
	while( !q.empty() ) {
		Vertex<T>* v = q.front();
		q.pop();

		res.push_back(v->info);

		for(unsigned int i = 0; i < v->adj.size(); i++) {
			v->adj[i].dest->indegree--;
			if( v->adj[i].dest->indegree == 0) q.push( v->adj[i].dest );
		}
	}

	//testar se o procedimento foi bem sucedido
	if ( res.size() != vertexSet.size() ) {
		while( !res.empty() ) res.pop_back();
	}

	//garantir que os "indegree" ficam actualizados ao final
	this->resetIndegrees();

	return res;
}


template<class T>
vector<T> Graph<T>::getPath(const T &origin, const T &dest){

	list<T> buffer;
	Vertex<T>* v = getVertex(dest);

	buffer.push_front(v->info);
	while ( v->path != NULL &&  v->path->info != origin) {
		v = v->path;
		buffer.push_front(v->info);
	}
	if( v->path != NULL )
		buffer.push_front(v->path->info);


	vector<T> res;
	while( !buffer.empty() ) {
		res.push_back( buffer.front() );
		buffer.pop_front();
	}
	return res;
}

template<class T>
vector<T> Graph<T>::getfloydWarshallPath(const T &origin, const T &dest){

	int originIndex = -1, destinationIndex = -1;

	for(unsigned int i = 0; i < vertexSet.size(); i++)
	{
		if(vertexSet[i]->info == origin)
			originIndex = i;
		if(vertexSet[i]->info == dest)
			destinationIndex = i;

		if(originIndex != -1 && destinationIndex != -1)
			break;
	}


	vector<T> res;

	//se n�o foi encontrada solu��o poss�vel, retorna lista vazia
	if(pathArray[originIndex][destinationIndex] == INT_INFINITY)
		return res;

	res.push_back(vertexSet[originIndex]->info);

	//se houver pontos interm�dios...
	if(pathArrayAux[originIndex][destinationIndex] != -1)
	{

		getfloydWarshallPathAux(originIndex, pathArrayAux[originIndex][destinationIndex], res);

		res.push_back(vertexSet[pathArrayAux[originIndex][destinationIndex]]->info);

		getfloydWarshallPathAux(pathArrayAux[originIndex][destinationIndex],destinationIndex, res);
	}

	res.push_back(vertexSet[destinationIndex]->info);


	return res;
}



template<class T>
void Graph<T>::getfloydWarshallPathAux(int index1, int index2, vector<T> & res)
{
	if(pathArrayAux[index1][index2] != -1)
	{
		getfloydWarshallPathAux(index1, pathArrayAux[index1][index2], res);

		res.push_back(vertexSet[pathArrayAux[index1][index2]]->info);

		getfloydWarshallPathAux(pathArrayAux[index1][index2],index2, res);
	}
}


template<class T>
void Graph<T>::unweightedShortestPath(const T &s) {

	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->path = NULL;
		vertexSet[i]->dist = INT_INFINITY;
	}

	Vertex<T>* v = getVertex(s);
	v->dist = 0;
	queue< Vertex<T>* > q;
	q.push(v);

	while( !q.empty() ) {
		v = q.front(); q.pop();
		for(unsigned int i = 0; i < v->adj.size(); i++) {
			Vertex<T>* w = v->adj[i].dest;
			if( w->dist == INT_INFINITY ) {
				w->dist = v->dist + 1;
				w->path = v;
				q.push(w);
			}
		}
	}
}


template<class T>
void Graph<T>::bellmanFordShortestPath(const T &s) {

	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->path = NULL;
		vertexSet[i]->dist = INT_INFINITY;
	}

	Vertex<T>* v = getVertex(s);
	v->dist = 0;
	queue< Vertex<T>* > q;
	q.push(v);

	while( !q.empty() ) {
		v = q.front(); q.pop();
		for(unsigned int i = 0; i < v->adj.size(); i++) {
			Vertex<T>* w = v->adj[i].dest;
			if(v->dist + v->adj[i].weight < w->dist) {
				w->dist = v->dist + v->adj[i].weight;
				w->path = v;
				q.push(w);
			}
		}
	}
}

template<class T>
void Graph<T>::dijkstraShortestPath(const T &s) {

	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->path = NULL;
		vertexSet[i]->dist = INT_INFINITY;
		vertexSet[i]->processing = false;
	}

	Vertex<T>* v = getVertex(s);
	v->dist = 0;

	vector< Vertex<T>* > pq;
	pq.push_back(v);

	make_heap(pq.begin(), pq.end());


	while( !pq.empty() ) {

		v = pq.front();
		pop_heap(pq.begin(), pq.end());
		pq.pop_back();

		for(unsigned int i = 0; i < v->adj.size(); i++) {
			Vertex<T>* w = v->adj[i].dest;

			if(v->dist + v->adj[i].weight < w->dist ) {

				w->dist = v->dist + v->adj[i].weight;
				w->path = v;

				//se j� estiver na lista, apenas a actualiza
				if(!w->processing)
				{
					w->processing = true;
					pq.push_back(w);
				}

				make_heap (pq.begin(),pq.end(),vertex_greater_than<VertexInfo>());
			}
		}
	}
}

template<class T>
int Graph<T>::edgeCost(int vOrigIndex, int vDestIndex)
{
	for(unsigned int i = 0; i < vertexSet[vOrigIndex]->adj.size(); i++)
	{
		if(vertexSet[vOrigIndex] == vertexSet[vDestIndex])
			return 0;

		if(vertexSet[vOrigIndex]->adj[i].dest == vertexSet[vDestIndex])
			return vertexSet[vOrigIndex]->adj[i].weight;
	}

	return INT_INFINITY;
}


void printSquareArray(int ** arr, unsigned int size)
{
	for(unsigned int k = 0; k < size; k++)
	{
		if(k == 0)
		{
			cout <<  "   ";
			for(unsigned int i = 0; i < size; i++)
				cout <<  " " << i+1 << " ";
			cout << endl;
		}

		for(unsigned int i = 0; i < size; i++)
		{
			if(i == 0)
				cout <<  " " << k+1 << " ";

			if(arr[k][i] == INT_INFINITY)
				cout << " - ";
			else
				cout <<  " " << arr[k][i] << " ";
		}

		cout << endl;
	}
}


template<class T>
void Graph<T>::floydWarshallShortestPath() {

	pathArray = new int * [vertexSet.size()];
	pathArrayAux = new int * [vertexSet.size()];
	for(unsigned int i = 0; i < vertexSet.size(); i++)
	{
		pathArray[i] = new int[vertexSet.size()];
		pathArrayAux[i] = new int[vertexSet.size()];
		for(unsigned int j = 0; j < vertexSet.size(); j++)
		{
			pathArray[i][j] = edgeCost(i,j);
			pathArrayAux[i][j] = -1;
		}
	}


	for(unsigned int k = 0; k < vertexSet.size(); k++)
		for(unsigned int i = 0; i < vertexSet.size(); i++)
			for(unsigned int j = 0; j < vertexSet.size(); j++)
			{
				//se somarmos qualquer coisa ao valor INT_INFINITY, ocorre overflow, o que resulta num valor negativo, logo nem conv�m considerar essa soma
				if(pathArray[i][k] == INT_INFINITY || pathArray[k][j] == INT_INFINITY)
					continue;

				int val = min ( pathArray[i][j], pathArray[i][k]+pathArray[k][j] );
				if(val != pathArray[i][j])
				{
					pathArray[i][j] = val;
					pathArrayAux[i][j] = k;
				}
			}
}

template <class T>
void Graph<T>::show(GraphViewer *gv) {
	// Vertices
	for (size_t i=0;i<vertexSet.size();i++) {
		gv->addNode(i);
		if (vertexSet[i]->getInfo().tipo==1) {gv->setVertexColor(i,"green");} // inicio
		else if (vertexSet[i]->getInfo().tipo==2) {gv->setVertexColor(i,"red");} // fim
		gv->setVertexLabel(i,printVertexInfo(vertexSet[i]->info));
	}
	// Arestas
	int a=0;
	for (size_t i=0;i<vertexSet.size();i++) {
		for (size_t j=0;j<vertexSet[i]->adj.size();j++) {
			int dest=getVertexIndex(vertexSet[i]->adj[j].dest);
			if (dest>=0) {
				gv->addEdge(a,i,dest,EdgeType::DIRECTED);
				a++;
			}
		}
	}
	gv->rearrange();
}

template <class T>
bool compareHelper(Vertex<T> *v1, Vertex<T> *v2) {
	return v1->getInfo()<v2->getInfo();
}

template <class T>
void Graph<T>::sortVertex() {
	sort(vertexSet.begin(),vertexSet.end(),compareHelper<T>);
}

template <class T>
void Graph<T>::showPath(GraphViewer *gv) {
	// Vertices
	VertexInfo inicio;
	VertexInfo fim;

	for (size_t i=0;i<vertexSet.size();i++) {
		if (vertexSet[i]->getInfo().tipo==1) {inicio=vertexSet[i]->getInfo();} // inicio
		else if (vertexSet[i]->getInfo().tipo==2) {fim=vertexSet[i]->getInfo();} // fim
	}
	dijkstraShortestPath(inicio);

	vector <T> v=getPath(inicio,fim);
	for (size_t i=0;i<v.size();i++) {
		gv->addNode(i);
		if (v[i].tipo==1) {gv->setVertexColor(i,"green");} // inicio
		else if (v[i].tipo==2) {gv->setVertexColor(i,"red");} // fim
		gv->setVertexLabel(i,printVertexInfo(v[i]));
	}
	for (size_t i=0;i<v.size()-1;i++) {
		gv->addEdge(i,i,i+1,EdgeType::DIRECTED);
	}

	gv->rearrange();
}

template <class T>
void Graph<T>::showTopological(GraphViewer *gv) {
	// Vertices
	VertexInfo inicio;
	VertexInfo fim;
	for (size_t i=0;i<vertexSet.size();i++) {
		if (vertexSet[i]->getInfo().tipo==1) {inicio=vertexSet[i]->getInfo();} // inicio
		else if (vertexSet[i]->getInfo().tipo==2) {fim=vertexSet[i]->getInfo();} // fim
	}
	calculateMTC(inicio);
	calculateUTC(fim);
	calculateMinTC(inicio);
	vector <T> v=topologicalOrder();
	vector <int> indices;
	bool started=false;
	for (size_t i=0;i<v.size();i++) {
		if (v[i].tipo==1) {started=true;}
		if (started && v[i].utc<INT_INFINITY) {
			gv->addNode(i);
			gv->setVertexLabel(i,printVertexInfo(v[i]));
			indices.push_back(i);
			if (v[i].tipo==1) {gv->setVertexColor(i,"green");} // inicio
			else if (v[i].tipo==2) {gv->setVertexColor(i,"red");/*break;*/} // fim
		}
	}
	int a=0;
	for (size_t i=0;i<indices.size();i++) {
		Vertex<VertexInfo>* vertice = getVertex(v[indices[i]]);
		for (size_t j=0;j<vertice->adj.size();j++) {
			int dest=-1;
			for (size_t x=0;x<indices.size();x++) {
				if (v[indices[x]]==vertice->adj[j].dest->info) {dest=indices[x];}
			}
			if (dest>=0) {
				gv->addEdge(a,indices[i],dest,EdgeType::DIRECTED);
				stringstream label;
				double peso=vertice->adj[j].weight;
				double gap=vertice->adj[j].dest->info.utc-vertice->info.mtc-peso;
				if (vertice->adj[j].dest->info.utc<INT_INFINITY) {
					label << "Dur: "<< peso << "m / Folga:" << gap << "m";
				}
				else {label << "Dur: " << peso << "m";}
				if (vertice->adj[j].dest->info.local!=vertice->info.local) {
					gv->setEdgeLabel(a,label.str());gv->setEdgeThickness(a,2);
					if (gap==0) {gv->setEdgeColor(a,"red");} // Voo Critico
				} // Voo
				else {gv->setEdgeThickness(a,1);} // Espera
				a++;
			}
		}
	}

	gv->rearrange();
}

template <class T>
void Graph<T>::showAvioes(GraphViewer *gv,vector <Aviao> &avioes) {
	// Vertices
	VertexInfo inicio;
	VertexInfo fim;
	for (size_t i=0;i<vertexSet.size();i++) {
		if (vertexSet[i]->getInfo().tipo==1) {inicio=vertexSet[i]->getInfo();} // inicio
		else if (vertexSet[i]->getInfo().tipo==2) {fim=vertexSet[i]->getInfo();} // fim
	}
	// Associa os avioes
	calculateMinAvioes(inicio,fim);
	typename vector<Vertex<T>*>::const_iterator it= vertexSet.begin();
	typename vector<Vertex<T>*>::const_iterator ite= vertexSet.end();
	for (; it !=ite; it++) {(*it)->visited=false;}

	vector <Aviao *> avioesDisp;
	for (size_t i=0;i<avioes.size();i++) {
		avioesDisp.push_back(&(avioes[i]));
	}
	associaAvioes(getVertex(inicio),avioesDisp);

	vector <T> v=topologicalOrder();
	vector <int> indices;

	bool started=false;
	for (size_t i=0;i<v.size();i++) {
		if (v[i].tipo==1) {started=true;}
		if (started && v[i].utc<INT_INFINITY) {
			gv->addNode(i);
			gv->setVertexLabel(i,printVertexInfo(v[i]));
			indices.push_back(i);
			if (v[i].tipo==1) {gv->setVertexColor(i,"green");} // inicio
			else if (v[i].tipo==2) {gv->setVertexColor(i,"red");/*break;*/} // fim
		}
	}
	int a=0;
	for (size_t i=0;i<indices.size();i++) {
		Vertex<VertexInfo>* vertice = getVertex(v[indices[i]]);
		for (size_t j=0;j<vertice->adj.size();j++) {
			int dest=-1;
			for (size_t x=0;x<indices.size();x++) {
				if (v[indices[x]]==vertice->adj[j].dest->info) {dest=indices[x];}
			}
			if (dest>=0) {
				gv->addEdge(a,indices[i],dest,EdgeType::DIRECTED);
				stringstream label;
				int avioes=vertice->adj[j].dest->info.minAvioes;
				for (size_t av1=0;av1<vertice->adj[j].dest->info.avioes.size();av1++) {
					string matricula1=vertice->adj[j].dest->info.avioes[av1]->getMatricula();
					for (size_t av2=0;av2<vertice->info.avioes.size();av2++) {
						string matricula2=vertice->info.avioes[av2]->getMatricula();
						if (matricula1==matricula2) {
							label << matricula1 << ";";break;
						}
					}
				}
				if (vertice->adj[j].dest->info.local!=vertice->info.local) {
					gv->setEdgeLabel(a,label.str());gv->setEdgeThickness(a,2);
					if (avioes>0) {gv->setEdgeColor(a,"red");} // Voo com defice de avioes
				} // Voo
				else {gv->setEdgeThickness(a,1);} // Espera
				a++;
			}
		}
	}

	gv->rearrange();
}

template <class T>
void Graph<T>::showGantt(GraphViewer *gv) {
	// Vertices
	VertexInfo inicio;
	VertexInfo fim;
	for (size_t i=0;i<vertexSet.size();i++) {
		if (vertexSet[i]->getInfo().tipo==1) {inicio=vertexSet[i]->getInfo();} // inicio
		else if (vertexSet[i]->getInfo().tipo==2) {fim=vertexSet[i]->getInfo();} // fim
	}

	int incrementoX=2;
	int incrementoY=50;
	vector <T> v=topologicalOrder();
	vector <int> indices;
	bool started=false;
	int linha=1;
	for (size_t i=0;i<v.size();i++) {
		if (v[i].tipo==1) {
			gv->addNode(i,0,0);
			gv->setVertexLabel(i,printVertexInfo(v[i]));
			gv->setVertexColor(i,"green");
			indices.push_back(i);
			started=true;}
		if (v[i].tipo==2) {
			gv->addNode(i,(v[i].data-inicio.data)*incrementoX,linha*incrementoY);
			gv->setVertexLabel(i,printVertexInfo(v[i]));
			gv->setVertexColor(i,"red");
			indices.push_back(i);
		}
		if (started && v[i].utc<INT_INFINITY && v[i].tipo==3) {
			gv->addNode(i,(v[i].data-inicio.data)*incrementoX,linha*incrementoY);
			gv->setVertexLabel(i,printVertexInfo(v[i]));
			indices.push_back(i);
			vector< Edge<T> > adj=getVertex(v[i])->adj;
			for (size_t j=0;j<adj.size();j++) {
				if (adj[j].dest->info.tipo==4) {
					for (size_t k=0;k<v.size();k++) {
						if (adj[j].dest->info==v[k]) {
							gv->addNode(k,(v[k].data-inicio.data)*incrementoX,linha*incrementoY);
							gv->setVertexLabel(k,printVertexInfo(v[k]));
							indices.push_back(k);
							break;
						}
					}
				}
			}
			linha++;
		}
	}
	int a=0;
	for (size_t i=0;i<indices.size();i++) {
		Vertex<VertexInfo>* vertice = getVertex(v[indices[i]]);
		for (size_t j=0;j<vertice->adj.size();j++) {
			int dest=-1;
			for (size_t x=0;x<indices.size();x++) {
				if (v[indices[x]]==vertice->adj[j].dest->info) {dest=indices[x];}
			}
			if (dest>=0) {
				gv->addEdge(a,indices[i],dest,EdgeType::DIRECTED);
				stringstream label;
				double peso=vertice->adj[j].weight;
				double gap=vertice->adj[j].dest->info.utc-vertice->info.mtc-peso;
				if (vertice->adj[j].dest->info.utc<INT_INFINITY) {
					label << "Dur: "<< peso << "m / Folga:" << gap << "m";
				}
				else {label << "Dur: " << peso << "m";}
				if (vertice->adj[j].dest->info.local!=vertice->info.local) {
					gv->setEdgeLabel(a,label.str());gv->setEdgeThickness(a,2);
				} // Voo
				else {gv->setEdgeThickness(a,1);} // Espera
				a++;
			}
		}
	}

	gv->rearrange();
}

template <class T>
void Graph<T>::calculateMinTC(const T &s) {
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->info.mintc =0;
		vertexSet[i]->processing = false;
	}
	Vertex<T>* v = getVertex(s);
	v->info.mintc = 0;

	vector< Vertex<T>* > pq;
	pq.push_back(v);

	make_heap(pq.begin(), pq.end());

	while( !pq.empty() ) {
		v = pq.front();
		pop_heap(pq.begin(), pq.end());
		pq.pop_back();
		for(unsigned int i = 0; i < v->adj.size(); i++) {
			Vertex<T>* w = v->adj[i].dest;
			if(v->info.mintc + v->adj[i].weight >= w->info.mintc ) {
				double peso=0;
				if (v->info.local!=v->adj[i].dest->info.local) {peso=v->adj[i].weight;}
				w->info.mintc = v->info.mintc + peso;
				//se ja estiver na lista, apenas a actualiza
				if(!w->processing)
				{
					w->processing = true;
					pq.push_back(w);
				}
				make_heap (pq.begin(),pq.end(),vertex_greater_than<VertexInfo>());
			}
		}
	}
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->processing = false;
	}
}

template <class T>
void Graph<T>::calculateMTC(const T &s) {
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->info.mtc =0;
		vertexSet[i]->processing = false;
	}
	Vertex<T>* v = getVertex(s);
	v->info.mtc = 0;

	vector< Vertex<T>* > pq;
	pq.push_back(v);

	make_heap(pq.begin(), pq.end());

	while( !pq.empty() ) {
		v = pq.front();
		pop_heap(pq.begin(), pq.end());
		pq.pop_back();
		for(unsigned int i = 0; i < v->adj.size(); i++) {
			Vertex<T>* w = v->adj[i].dest;
			if(v->info.mtc + v->adj[i].weight >= w->info.mtc ) {
				w->info.mtc = v->info.mtc + v->adj[i].weight;
				//se ja estiver na lista, apenas a actualiza
				if(!w->processing)
				{
					w->processing = true;
					pq.push_back(w);
				}
				make_heap (pq.begin(),pq.end(),vertex_greater_than<VertexInfo>());
			}
		}
	}
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->processing = false;
	}
}

template <class T>
void Graph<T>::calculateUTC(const T &e) {
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->info.utc = INT_INFINITY;
		vertexSet[i]->processing = false;
	}
	Vertex<T>* v = getVertex(e);
	v->info.utc = v->info.mtc;

	vector<T> vertices=topologicalOrder();
	for (int n=vertices.size()-2;n>=0;n--) {
		v = getVertex(vertices[n]);
		for(unsigned int i = 0; i < v->adj.size(); i++) {
			Vertex<T>* w = v->adj[i].dest;
			double peso=0;
			if (v->info.local!=v->adj[i].dest->info.local) {peso=v->adj[i].weight;}
			if(w->info.mtc - peso < v->info.utc ) {
				v->info.utc = w->info.mtc - peso;
			}
		}
	}
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->processing = false;
	}
}

template <class T>
void Graph<T>::calculateMinAvioes(const T &s,const T &e) {


	// Percorrer pela ordem topologica inversa
	// Para cada vertice com 0 avioes, fazer pathfinding do inicio ate la
	// A cada vertice do path, adicionar 1 aviao

	dijkstraShortestPath(s);
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->info.minAvioes = 0;
		vertexSet[i]->processing = false;
	}
	Vertex<T>* inicio = getVertex(s);
	Vertex<T>* v = getVertex(e);
	v->info.minAvioes=0;

	vector<T> vertices=topologicalOrder();
	for (int n=vertices.size()-1;n>=0;n--) {
		v = getVertex(vertices[n]);
		if (v->info.minAvioes==0) {
			 while (v!=inicio) {
				v->info.minAvioes++;
				v=v->path;
				if (v==NULL) {break;}
			}
			inicio->info.minAvioes++;
		}
	}
	for(unsigned int i = 0; i < vertexSet.size(); i++) {
		vertexSet[i]->processing = false;
	}
}

template <class T>
void Graph<T>::associaAvioes(Vertex<T> *v, vector <Aviao *> avioesDisponiveis) {
	sort(avioesDisponiveis.begin(),avioesDisponiveis.end(),compareAvioesPtr);

	for (size_t i=0;i<avioesDisponiveis.size() && v->info.minAvioes>0;i++) {
		v->info.avioes.push_back(avioesDisponiveis[i]);
		v->info.minAvioes--;
	}
	if (v->info.minAvioes<=0) {v->visited = true;}
	else {v->visited = false;} // Vai ter de ser visitado mais vezes

	typename vector<Edge<T> >::iterator it= (v->adj).begin();
	typename vector<Edge<T> >::iterator ite= (v->adj).end();
	for (; it !=ite; it++) {
	    if ( it->dest->visited == false ){
	    	associaAvioes(it->dest,v->info.avioes);
	    }
	}
}

#endif /* GRAPH_H_ */
