package model.vo;

import model.data_structures.Grafo.Vertice;
import model.data_structures.Queue;

public class VOCC implements Comparable<VOCC>{
	private Queue<Vertice> cola;
	public VOCC(Queue<Vertice> q)
	{
		cola=q;
	}
	@Override
	public int compareTo(VOCC o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
