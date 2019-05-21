package model.data_structures;

import model.data_structures.Grafo.Arco;

public class KruskalMST
{
	private Queue<Arco> mst;
	public KruskalMST(Grafo G)
	{
		mst = new Queue<Arco>();
		MinPQ<Arco> pq = new MinPQ<Arco>(G.edges());
		UF uf = new UF(G.V());
		while (!pq.isEmpty() && mst.size() < G.V()-1)
		{
			Arco e = pq.delMin(); // Get min weight edge on pq
			int v = e.either(), w = e.other(v); // and its vertices.
			if (uf.connected(v, w)) continue; // Ignore ineligible edges.
			uf.union(v, w); // Merge components.
			mst.enqueue(e); // Add edge to mst.
		}
	}
	public Iterable<Arco> edges()
	{ return mst; }
	
	

public class UF {

	private int[] id; // access to component id (site indexed)
	private int count; // number of components

	public UF(int N)
	{ // Initialize component id array.
		count = N;
		id = new int[N];
		for (int i = 0; i < N; i++)
			id[i] = i;
	}

	public int count()
	{ return count; }

	public boolean connected(int p, int q)
	{ return find(p) == find(q); }

	private int find(int p)
	{ // Follow links to find a root.
		while (p != id[p]) p = id[p];
		return p;
	}

	public void union(int p, int q)
	{
		int i = find(p);
		int j = find(q);
		if (i == j) return;
		// Make smaller root point to larger one.
		if (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
		else { id[j] = i; sz[i] += sz[j]; }
		count--;
	}

}

}