
import br.uff.networks.domino_mania.client.DominoManiaClientConsole;

public class ClientLauncher {
    
    public static void main(String[] args) {
	try{
	    DominoManiaClientConsole client = new DominoManiaClientConsole();
	    client.start();
	}catch(Exception e){
	    System.out.println("servidor indisponível");
	}
    }

}
