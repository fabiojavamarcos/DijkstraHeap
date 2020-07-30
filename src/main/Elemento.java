package main;

public class Elemento {
	private int origem; // u
	private int vertice; // v
	private int custo; // w
	private boolean hasNext; 
	public Elemento(int vertice, int custo, boolean hasNext) {
		super();
		this.vertice = vertice;
		this.custo = custo;
		this.hasNext = hasNext;
	}
	public int getVertice() {
		return vertice;
	}
	public void setVertice(int vertice) {
		this.vertice = vertice;
	}
	public int getCusto() {
		return custo;
	}
	public void setCusto(int custo) {
		this.custo = custo;
	}
	public boolean isHasNext() {
		return hasNext;
	}
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
	public Elemento(int origem, int vertice, int custo, boolean hasNext) {
		super();
		this.origem = origem;
		this.vertice = vertice;
		this.custo = custo;
		this.hasNext = hasNext;
	}
	public int getOrigem() {
		return origem;
	}
	public void setOrigem(int origem) {
		this.origem = origem;
	}
	
}
