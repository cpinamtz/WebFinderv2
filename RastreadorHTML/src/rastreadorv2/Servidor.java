package rastreadorv2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Servidor {
	
	
	String [] dominio  = {".com",".info",".net",".es",".biz",".us",".tv",".org",".gov"};
	String [] pagina = new String [dominio.length];
	
	ArrayList<String> existpage = new ArrayList<String>();
	ArrayList<String> nonexistpage = new ArrayList<String>();

	
	//CONSTRUCTORES
	/**
	 * Este constructor se utiliza para que se pueda
	 * introducir un servidor personalizado que no sea 
	 * generado aleatoriamente
	 * @param servidor
	 */
	public Servidor(String servidor){
		int i;
		for(i=0;i<dominio.length;i++)
			pagina[i]="http://www."+servidor+dominio[i];
	}
	
	/**
	 * Constructor vacio(los nombres se generan de manera random)
	 */
	public Servidor(){	
	}
	
	
	//METODOS
	/**
	 * M�todo que crea aleatoriamente nombres de p�ginas web
	 * @throws IOException
	 */
	public void crearDireccionWeb(int TAMHOST) throws IOException{
			
		String comienzo = "http://www.";
		String host = "";
			
		char letra;
		int i,alfabeto;
			
		//Vamos a crear una p�gina con 2 caracteres
		for(i=0;i<TAMHOST;i++){
			//Elige un numero entre 97 y 122 (letras minusculas)(ver tabla ASCII)
			double a = Math.random()*26+97;
			alfabeto = (int) a;
			letra = (char) alfabeto;
			host = host+letra;
		}
		
		for(i=0;i<dominio.length;i++)
			pagina [i] = comienzo + host + dominio[i];
		
		}
	
	
	/**
	 * M�todo que muestra las paginas web generadas aleatoriamente de un
	 * nombre de servidor (independientemente de si existen o no)
	 */
	public void mostrarTodasPagina() {
		
		int i; 
		for(i=0;i<pagina.length;i++)
			System.out.println(pagina[i]);
	}
	
	/**
	 * M�todo que comprueba, realizando una petici�n, si la pagina
	 * web existe o no.
	 * @throws IOException
	 */
	public void existePagina() throws IOException{
		
		int i;
		for(i=0;i<pagina.length;i++){
			
			URL url = new URL(pagina[i]);
		    System.out.println("\nEstableciendo conexion con "+ pagina[i] + " ...");
		    URLConnection uc = url.openConnection();
		    
		    if(uc.getContentLength() == -1){
		    	nonexistpage.add(pagina[i]);
		    	System.out.println("No existe la pagina " + pagina[i]);
		    }
		    	
		    else {
		    	existpage.add(pagina[i]);
		    	System.out.println("Existe la pagina " + pagina[i]);
		    }
	}
	}
	/**
	 * M�todo que muestra por pantalla todas las paginas 
	 * con el mismo nombre de servidor que S� existen
	 */
	public void mostrarExiste(){
		
		int i;
		System.out.println("\nMostrando paginas existentes: ");
		for(i=0;i<existpage.size();i++)
				System.out.println(existpage.get(i));	
	}
	
	/**
	 * M�todo que muestra por pantalla todas las paginas 
	 * con el mismo nombre de servidor que NO existen
	 */
	public void mostrarNOExiste(){
		
		int i;
		System.out.println("\nMostrando paginas NO existentes: ");
		for(i=0;i<nonexistpage.size();i++)
				System.out.println(nonexistpage.get(i));
	}
	
	/**
	 * Metodo que extrae el documento HTML de las paginas que si existen
	 * @throws IOException
	 */
	public void getHTML() throws IOException{
		
		int i;
		//Recorrido del ArrayList de las paginas que s� existen
		for(i=0;i<existpage.size();i++){
			
			URL url = new URL(existpage.get(i));
			URLConnection uc = url.openConnection();
			uc.connect();
		    System.out.println("\nLeyendo codigo HTML de la pagina "+ uc.getURL()+ " ...");
			    
		    //Creacion del fichero que contendra el codigo HTML
		    BufferedWriter f1 = new BufferedWriter(new FileWriter(existpage.get(i).substring(7)+ ".txt"));
		    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			    
		    String inputLine;
		    String contenido = "";
			
		    //Mientras no llegue al final del fin de fichero, contenido guardar� todo el texto
			while ((inputLine = in.readLine()) != null)
				contenido += inputLine + "\n";
				    
		    in.close();
		    f1.write(contenido);
			    
		    //IMPORTANTE CERRAR EL FICHERO
		    f1.close(); 
		    System.out.println("Codigo HTML de la pagina " + pagina [i] + " copiado\n");
		}
	}
	
	/**
	 * Este m�todo se encarga de buscar la etiqueta HEAD del documento HTML
	 * y guarda todo su contenido en un archivo de texto para su posterior
	 * proceso m�s sencillo y limpio (b�squeda de CSS y JS)
	 * @throws IOException
	 */
	public void getHeadTag() throws IOException{
		
		int i;
		String archivo;
		String cadena;
		int beginhead = 0;
		int endhead = 0;
		String contenido = "";
		
		for(i=0;i<existpage.size();i++){
			
			archivo = existpage.get(i).substring(7) + ".txt";
			
			FileReader f = new FileReader(archivo);
			BufferedReader b = new BufferedReader(f);
			
			while((cadena = b.readLine())!=null) {
				contenido += cadena + "\n";
	            }
			
			beginhead = contenido.indexOf("<head>");
			endhead = contenido.indexOf("</head>");
			
			if(beginhead!=-1){
				
				BufferedWriter f1 = new BufferedWriter(new FileWriter("head"+existpage.get(i).substring(7)+".txt"));
				System.out.println("Etiqueta <head> encontrada de "+ existpage.get(i) + " :D");
				
				//'contenido' es igual a lo que haya dentro de las etiquetas HEAD 
		        contenido = contenido.substring(beginhead, endhead+7);
		        f1.write(contenido);
		        f1.close();
			}
			
			else
				System.out.println("No tiene <head> " + existpage.get(i) + " :_(");
			
			b.close();
	        }
		}

	}

