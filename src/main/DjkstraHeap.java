package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class DjkstraHeap {
	
	// deixando preparado para ler os dados.
	private int MAXNODES = 5;
	private int STARTNODE = 4;
	private int ARCS = 0;
	
	// listaAdj para guardar as conexões
	private Elemento listaAdj [][]; 

	private int S[]; // conjunto de nós explorados
	private BinHeapMin Q; // Heap Binário Mínimo
	private int a[]; // vetor de custo
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DjkstraHeap dj = new DjkstraHeap();
	}
	DjkstraHeap(){
		// leitura do arquivo.dat
		leArquivo();
		
		int vertices[] = new int [MAXNODES]; 
		boolean conhecidos[] = new boolean [MAXNODES];
		
		int estimativas[] = new int [MAXNODES]; //d
		int precedentes[] = new int[MAXNODES]; // PI
		No [] nos = new No[MAXNODES];
		// supondo início em 0 e 9999999 = Infinito(estimativa desconhecida)
		// 9999999 = precedente desconhecido
		for (int i=0; i<MAXNODES; i++){
			vertices[i]=i;
			estimativas[i]=9999999;
			precedentes[i]=9999999;
			nos[i] = new No(i,9999999);
		}
		
		estimativas [STARTNODE]=0; precedentes [STARTNODE]=0;
		nos[STARTNODE].setEstimativa(0); 
		Q = new BinHeapMin(MAXNODES,nos); // inicializa o heap
		//Q.imprime();
		//Q.posicoes();
		
		// inicialização da contagem de tempo
		Instant inicio = Instant.now();
		
		// no máximo vamos calcular MAXNODES
		int count = MAXNODES;
		
		// iniciando no STARTNODE K = nó atual
		int k = STARTNODE;
		
		// enquanto existiremm nós abertos ou não processados (elementos em Q)
		// Q aqui é representado por vertices[] e vertice[i] está aberto se conhecidos[i] = false
		// cada vez que um elemento for fechado count--;
		
		// atualiza nós fechados
		conhecidos[k] = true;
		
		// remove do heap 
		Q.removeMin();
		//Q.imprime();
		//Q.posicoes();
		
		while (count > 0) {
			
			// contador da listaAdj vai sempre de 0 até quando listaAdj [k][i] = null 
			int i = 0;
			//int min = estimativas[listaAdj[k][i].getVertice()];
			int verticeMin = 0;
			int min = 9999999;
			// relaxando o nó k
			//System.out.println (" nó da vez = "+k);
			
			while (listaAdj[k][i]!=null){ // vamos percorrer a lista de adjacências de k até o fim 
			
				
				// elemento da lista de adjacencia em teste
				Elemento aux = listaAdj[k][i];
				int j = aux.getVertice(); // vertice em teste é o j
				// verificando a aresta k,j
				//System.out.println("verificando aresta "+ k+","+j);
				
				int soma = aux.getCusto() + estimativas[k];
				
				if (!conhecidos[j]&&soma < min){ // temos que saber qem será o próximo não aberto (não visitado) que começará na próxima iteração
					verticeMin = j;
					min = soma;
				}
				if (soma < estimativas[j]&&!conhecidos[j]){ // Se achei estimativa melhor que a anterior atualizo ela e atualizo o pai.
					estimativas[j] = soma;
					precedentes[j] = k;
					Q.changeKey(new No(j,soma));
					//Q.imprime();
					//Q.posicoes();
					/*if (soma < min) { // Se j for a menor estimativa dentro todos os nós que podem ser alcançado a partir de k 
						verticeMin = j;
						min = soma;
					}*/
				}
				// testa o próximo elemento da lista de adjacencias
				i++;
				
			}
			
			// acha o menor vértice O(n)
			int menor = verticeMin;
			
			// remove do heap 
			// pega o elemento mínimo
			No itemMin = Q.min();
			
			
			if (itemMin!=null){
				k = itemMin.getNum();
				//System.out.println("item ínimo = "+ k);	
				// menor vértice aberto com estimativa mínima é fechado 
				conhecidos[k] = true;
				Q.removeMin();
				//Q.imprime();
				//Q.posicoes();
				}
			else {
				//System.out.println(" fila vazia ");
				break;
			}
			if (itemMin.getEstimativa()==9999999){
				break; // nós isolados
			}
			// menos um nó a processar
			count --;
			
		}
		
		Instant fim = Instant.now();
		
		for (int i = 0; i < MAXNODES; i++){
			System.out.println("Estimativas");
			System.out.println("["+i+"] = "+estimativas[i] );
		}
		for (int i = 0; i < MAXNODES; i++){
			System.out.println("Precedentes");
			System.out.println("["+i+"] = "+precedentes[i] );
		}
		System.out.println("Inicio:"+inicio);
	    System.out.println("Final: "+fim);
	    Duration duracao = Duration.between(inicio, fim);
	    long duracaoNanos = duracao.toNanos();
	    System.out.println("tempo: "+ duracaoNanos);
		
	}
	
	private void leArquivo(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
		// arquivo Default é o de teste
		File selectedFile = new File("/Users/fabiomarcosdeabreusantos/Downloads/check_v5_s1.dat" );
		if (result == JFileChooser.APPROVE_OPTION) {
		    selectedFile = fileChooser.getSelectedFile();
		    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		}
		InputStream is = null;
		try {
			is = new FileInputStream(selectedFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    // ALt para ler o formato dat ou stp
	    // Se o arquivo for formato .dat
	    if (selectedFile.getName().endsWith("dat")){
		    String s, token = null;
		    StringTokenizer stk = null;
		    int origem, vertice, custo; //vertice = destino
			try {
				s = br.readLine(); // primeira linha do header
				stk = new StringTokenizer(s);
				token = stk.nextToken();
				if (token.equalsIgnoreCase("NB_NODES")){
						MAXNODES = Integer.parseInt(stk.nextToken());
						System.out.println("NB_NODES:"+MAXNODES);
						// criando a lista de adjacência
						listaAdj = new Elemento [MAXNODES][MAXNODES];
				} else { 
					System.out.println("Erro no header, falta o NB_NODES");
				}
				if (s!=null){
					s = br.readLine(); // segunda linha do header
					stk = new StringTokenizer(s);
					token = stk.nextToken();
					if (token.equalsIgnoreCase("NB_ARCS")){
							ARCS = Integer.parseInt(stk.nextToken());
							System.out.println("NB_ARCS:"+ARCS);
					} else { 
						System.out.println("Erro no header, falta o NB_ARCS");
					}
				}
				if (s!=null){
					s = br.readLine(); // terceira linha do header
					stk = new StringTokenizer(s);
					token = stk.nextToken();
					if (token.equalsIgnoreCase("LIST_OF_ARCS")){
							System.out.println(token);
					} else { 
						System.out.println("Erro no header, falta o LIST_OF_ARCS");
					}
					token = stk.nextToken();
					if (token.equalsIgnoreCase("COSTS")){
							System.out.println(token);
					} else { 
						System.out.println("Erro no header, falta o COSTS");
					}
				}
				 	 
				while (s != null && !s.equalsIgnoreCase("END")) {
					
					s = br.readLine();
					System.out.println(s);
					stk = new StringTokenizer(s);
					if (s.equalsIgnoreCase("END")){
						System.out.println("Fim do Arquivo");
					} else {
						origem = Integer.parseInt(stk.nextToken());
						vertice = Integer.parseInt(stk.nextToken());
						custo = Integer.parseInt(stk.nextToken());
						Elemento e = new Elemento(origem, vertice, custo, true);
						insereLista(e);
					}
				}
			
				br.close();
				STARTNODE = Integer.parseInt(JOptionPane.showInputDialog(
	                    "Entre o nó inicial","0"));
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    } else {
	    	// se o arquivo for no formato .stp
	    	if (selectedFile.getName().endsWith("stp")){
			    String s, token = null;
			    StringTokenizer stk = null;
			    int origem, vertice, custo; //vertice = destino
				try {
					s = br.readLine(); // primeira linha do header
					stk = new StringTokenizer(s);
					token = stk.nextToken();
					while (!token.equalsIgnoreCase("Remark")&&s!=null) {
						s = br.readLine(); // ler ateh achar Remark
						if (!s.equals("")){
							stk = new StringTokenizer(s);
							token = stk.nextToken();
						}
					}
					while (!token.equalsIgnoreCase("Section")&&s!=null) {
						s = br.readLine(); // ler ateh achar Section 
						if (!s.equals("")){
							stk = new StringTokenizer(s);
							token = stk.nextToken();
						}
					}
					// ler linha Nodes
					s = br.readLine();
					stk = new StringTokenizer (s);
					token = stk.nextToken();
					if (token.equalsIgnoreCase("Nodes")){
						int nodes = Integer.parseInt(stk.nextToken());
						MAXNODES = nodes + 1;
						System.out.println("Nodes:"+MAXNODES);
						// criando a lista de adjacência (MAXNODES +1 pq não tem nó 0)
						listaAdj = new Elemento [MAXNODES][MAXNODES];
					} else { 
						System.out.println("Erro no header, falta o Nodes");
					}
					if (s!=null){
						s = br.readLine(); // ler linha Edges
						stk = new StringTokenizer(s);
						token = stk.nextToken();
						if (token.equalsIgnoreCase("Edges")){
								ARCS = Integer.parseInt(stk.nextToken());
								System.out.println("NB_ARCS:"+ARCS);
						} else { 
							System.out.println("Erro no header, falta o Edges");
						}
					}
					while (s != null && !s.equalsIgnoreCase("END")) {
						
						s = br.readLine();
						System.out.println(s);
						stk = new StringTokenizer(s);
						if (s.equalsIgnoreCase("End")){
							System.out.println("Fim do Arquivo");
						} else {
							String letraE = stk.nextToken();
							origem = Integer.parseInt(stk.nextToken());
							vertice = Integer.parseInt(stk.nextToken());
							custo = Integer.parseInt(stk.nextToken());
							Elemento e = new Elemento(origem, vertice, custo, true);
							insereLista(e);
							// insere na outra direção
							e = new Elemento(vertice, origem, custo, true);
							insereLista(e);

						}
					}
				
					br.close();
					STARTNODE = Integer.parseInt(JOptionPane.showInputDialog(
		                    "Entre o nó inicial","1"));
		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	    		
	    	} else {
	    		System.out.println("arquivo tem que ser .dat ou .stp!");
	    	}
	    }
	}

	private void insereLista(Elemento e) {
		// TODO Auto-generated method stub
		
		// busca posição vazia na lista de adjacências para inserir elemento
		int i = 0;
		
		
		while (listaAdj[e.getOrigem()][i]!=null){
			
			listaAdj[e.getOrigem()][i].setHasNext(true);
			i++;
		}
		listaAdj[e.getOrigem()][i] = e;
		listaAdj[e.getOrigem()][i].setHasNext(false);
		System.out.println("Inseri elemento na posição ["+e.getOrigem()+"]["+i+"]" );
		
	}

}
