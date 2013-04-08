import java.util.Arrays;

public class SAP {
	
	private Digraph g = null;
	private boolean[] marked = null;
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G){
		g = new Digraph(G);
		marked = new boolean[g.V()];
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w){
		if(v < 0 || v >= g.V()|| w < 0 || w >= g.V()){
			throw new IndexOutOfBoundsException();
		}
		Arrays.fill(marked, false);
		BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(g, v);
		BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(g, w);
		
		int length = Integer.MAX_VALUE;
		Queue<Integer> ancestorsV = new Queue<Integer>();
		ancestorsV.enqueue(v);
		while(!ancestorsV.isEmpty()){
			Integer ancestor = ancestorsV.dequeue();
			if(pathW.hasPathTo(ancestor)){
				int tempLength = pathV.distTo(ancestor) + pathW.distTo(ancestor);
				if(tempLength < length){
					length = tempLength;
				}
			}
			for(Integer adj:g.adj(ancestor)){
				if(!marked[adj]){
					ancestorsV.enqueue(adj);
					marked[adj] = true;
				}
			}
		}
		if(length < Integer.MAX_VALUE)
			return length;
		else
			return -1;
	}
	
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w)
	{
		Arrays.fill(marked, false);
		if(v < 0 || v >= g.V()|| w < 0 || w >= g.V()){
			throw new IndexOutOfBoundsException();
		}
		BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(g, v);
		BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(g, w);
		
		int length = Integer.MAX_VALUE;
		int commonAncestor = -1;
		Queue<Integer> ancestorsV = new Queue<Integer>();
		ancestorsV.enqueue(v);
		while(!ancestorsV.isEmpty()){
			Integer ancestor = ancestorsV.dequeue();
			if(pathW.hasPathTo(ancestor)){
				int tempLength = pathV.distTo(ancestor) + pathW.distTo(ancestor);
				if(tempLength < length){
					length = tempLength;
					commonAncestor = ancestor;
				}
			}
			for(Integer adj:g.adj(ancestor)){
				if(!marked[adj]){
					ancestorsV.enqueue(adj);
					marked[adj] = true;
				}
			}
		}
		return 	commonAncestor;
	}
	
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{
		int length = Integer.MAX_VALUE;
		for(Integer vInt:v){
			for(Integer wInt:w){
				int tempLength = length(vInt, wInt);
				if(tempLength!= -1 && tempLength < length){
					length = tempLength;
				}
			}
		}
		if(length < Integer.MAX_VALUE)
			return length;
		else
			return -1;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		int length = Integer.MAX_VALUE;
		int pathV = -1;
		int pathW = -1;
		for(Integer vInt:v){
			for(Integer wInt:w){
				int tempLength = length(vInt, wInt);
				if(tempLength!= -1 && tempLength < length){
					length = tempLength;
					pathV = vInt;
					pathW = wInt;
				}
			}
		}
		if(pathV != -1){
			return ancestor(pathV, pathW);
		}
		else{
			return -1;
		}
	}

	// for unit testing of this class (such as the one below)
	public static void main(String[] args)
	{
		In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
}
