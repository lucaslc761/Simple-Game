import javax.swing.JLabel;

public class DesenhoMovel extends Desenho {


	
    public DesenhoMovel(String path, int x, int y) {
        super(path, x, y);
    }
    
    public void moverDireita(int distancia) {
    	this.setX(this.getX() + distancia);
    }
    
    

	
	
	

	
    
}
