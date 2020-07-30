package main;

public class BinHeapMin {

	private int n;               /* Numero de elementos no heap */   
	private int tam;             /* Tamanho do heap */
    private	No vetor[];          /* vetor com elementos */
    private int pos[];           /* vetor com a posição no array onde se encontra o elemento com determinado custo 
    							ex: pos[1] = 8  significa que o nó 1 está na posição 8 do array */
    
    public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

	public No[] getVetor() {
		return vetor;
	}

	public void setVetor(No[] vetor) {
		this.vetor = vetor;
	}
	
	public int[] getPos() {
		return pos;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
	}

    
	/* Constroi heap vazio */
    public BinHeapMin(int tamanho){
	  /* aumenta o tamanho do vetor em uma unidade para iniciar do indice 1 */
	  setTam(tamanho);
	  setVetor(new No[tamanho+1]);
	  setN(0); 
	  setPos(new int[tamanho]);
	}
	  
	/* Constroi heap a partir de vetor v */
    public BinHeapMin(int tamanho, No v[]){
	  setTam(tamanho); setVetor(new No[tamanho+1]); setN(tamanho); setPos(new int[tamanho]);
	
	  for( int i = 0; i < tamanho; i++ ){
	        vetor[ i + 1 ] = v[ i ];
	        pos[v[i].getNum()] = i + 1;
	        //System.out.println("vetor[i+1]="+ i + " + 1 <- v[i] " + i + " Num Nó " + v[i].getNum());
	        //System.out.println("pos["+v[i].getNum()+"] <- i+1 " + i+"+1");
	        //System.out.println("\n");
	  }
	  constroiHeap( );              
	}
	 

	/* Busca o menor item na fila de prioridades.
	   Retorna o menor item em itemMin e true, ou falso se heap vazio */
	public No min (){
	  if ( vazia() ){
	     //System.out.println("Fila de prioridades vazia!\n");
	     return null;
	  }   
	  return vetor[1];
	  
	}

	/* Insere item x na fila de prioridade, mantendo a propriedade do heap. 
	   Eh permitido duplicadas. */
	public void insere(No x) {
	  int dir;
	  
	  if ( tam == n ){
	    //System.out.println("Fila de prioridades cheia!\n");
	    return;
	  }
	  
	  /* inicializa sentinela */  
	  vetor[ 0 ] = x;   
	  dir = ++n;
	  
	  /* Refaz heap (sobe elemento) */
	  for( ; x.getEstimativa() < vetor[dir/2].getEstimativa(); dir /= 2 )
	    vetor[dir] = vetor[ dir/2 ];
	  vetor[dir] = x ;
	  pos[x.getNum()]=dir;
	} 
	
	public void bubbleUp(int i, No x){
		
		int posAtual = pos[x.getNum()];
		
		/* Refaz heap (sobe elemento) */
		int pai = i/2;
		int filho = i;
		for(int j = i ; j>1&&vetor[filho].getEstimativa() < vetor[pai].getEstimativa(); j /= 2 ){
			  //System.out.println("Bubble up  nó " + vetor[j/2].getNum() +" pos "+ pos[vetor[j/2].getNum()] + " para  nó " + vetor[j].getNum()+ " pos " +pos[vetor[j].getNum()] );
			  
			  No temp = vetor[pai];
			  // troca -> pai desce pro lugar do filho
			  vetor[pai] = vetor[ filho ];
			  vetor[filho] = temp;
		      // posição do pai agora é a posição do filho
			  int posTemp = pos[vetor[pai].getNum()];
			  pos[vetor[pai].getNum()] = pos[vetor[filho].getNum()]; 
			  pos[vetor[filho].getNum()] = posTemp; 
		      // testa novo pai
			  filho = pai;
		      pai = pai/2;
		      
		  }
	}

	/* Remove o menor item da fila de prioridades. */
	public void removeMin( )
	{
	    if( vazia( ) ){
	     //System.out.println ("Fila de prioridades vazia!\n");
	     return;    
	    }    
	    //System.out.println("removendo nó... " + vetor[1].getNum());
	    pos[vetor[1].getNum()]=9999999; // 7 noves = elemento vazou 
	    vetor[ 1 ] = vetor[ n-- ];
	    pos[vetor[1].getNum()] = 1;
	    refaz( 1 , n );
	}


	/* Remove o menor item da lista de prioridades e coloca ele em itemMin. */
	public void removeMin( No itemMin )
	{
	    if( vazia( ) ){
	     //System.out.println("Fila de prioridades vazia!");
	     return;    
	    }         
	    itemMin = vetor[1];
	    vetor[ 1 ] = vetor[ n-- ];
	    pos[vetor[1].getNum()]=1;
	    refaz( 1 , n );
	}

	/* Metodo interno para refazer o heap.
	   esq eh o indice de onde inicia o processo para refazer */
	private void refaz( int esq, int dir )
	{
	    int filho;
	    int x = vetor[ esq ].getEstimativa(); // x e y são auxiliares pra guardar os dados do nó substituído or outro
	    int y = vetor[ esq ].getNum();		// que está migrando
	    //System.out.println("refazendo... se necessário");
	    for( ; esq * 2 <= dir; esq = filho )
	    {
	        filho = esq * 2;
	        if( filho != dir && vetor[ filho + 1 ].getEstimativa() < vetor[ filho ].getEstimativa() )
	            filho++;
	        if( vetor[ filho ].getEstimativa() < x ){
	            vetor[ esq ].setEstimativa( vetor[ filho ].getEstimativa() );
	            vetor[ esq ].setNum( vetor [filho ].getNum());
	            pos[vetor[esq].getNum()] = esq;
	        }
	        else
	           break;
	    }
	    vetor[ esq ].setEstimativa(x);
	    vetor[ esq ].setNum(y);
	    pos[vetor[esq].getNum()] = esq;
	}

	/* Estabelece a propriedade de ordem do heap a partir de um arranjo arbitrario dos itens. */
	private void constroiHeap( )
	{
	    for( int i = n / 2; i > 0; i-- )
	        refaz( i , n);
	}

	/* Testa se a fila de prioridade estah logicamente vazia.
	   Retorna true se vazia, false caso contrario. */
	public boolean vazia( )
	{
	    return n == 0;
	}

	/* Faz a lista de prioridades logicamente vazia. */
	public void fazVazia( )
	{
	    n = 0;
	}

	public BinHeapMin(){
	  vetor=null;
	}


	public void heapsort(int n){
	 System.out.println ("Implementar!\n"); 
	}

	public void imprime(){
	  for(int i = 1; i<= n; i++)
	   // System.out.println ("Num: "+ vetor[i].getNum() + " Est: " + vetor[i].getEstimativa() +"  " );
		  System.out.println ("vetor ["+i+"] = Num: "+ vetor[i].getNum() + " Est: " + vetor[i].getEstimativa() +"  " );
	  //System.out.println ( '\n'); 
	  
	}

	public void changeKey(No no) {
		// TODO Auto-generated method stub
		int index = pos[no.getNum()];
		//System.out.println("Change key nó "+ no.getNum() + " estimativa nova: "+ no.getEstimativa() + " está na pos: " + pos[no.getNum()]  );
		vetor[index] = no;
		bubbleUp(index, no);
		
	    
	}

	public void posicoes(){
		  for(int i = 0; i< pos.length; i++)
			//System.out.println ("pos["+ i + "]: " + pos[i] +"  " );
		  	if (pos[i]!=9999999)  
		  		System.out.println ("pos["+ i + "]: " + pos[i] +"  " );
		  	//System.out.println ("posição " + pos[i] +" -> Nó "+ i   );
		 // System.out.println ( '\n'); 
		  
		}
}
	