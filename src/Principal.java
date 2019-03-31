import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Thread.sleep;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Principal extends JFrame implements Runnable, KeyListener{
	
	//recebe a operação
	int op = Integer.parseInt( JOptionPane.showInputDialog("1-Jogar/2-Resultados do Ultimo jogo"));
	
	//bloco de try/catch
	{
		//trata o Comando Invalido
	while(op != 1 && op != 2) {
		try {
			throw new ComandoInvalidoException();
		}catch(ComandoInvalidoException e) {
			JOptionPane.showMessageDialog(null, "Comando Invalido!");
			op = Integer.parseInt( JOptionPane.showInputDialog("1-Jogar/2-Resultados do Ultimo jogo"));
		}
	}
	}
	
	//cria o File
	File file = new File("ultimo_jogo.txt");
	
	// bloco try/catch
	{		
	//Lê o arquivo caso o usario queria e exista, se não existe lança uma exceção e a trata
try {
	if(op == 2) {
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new FileReader("ultimo_jogo.txt"));
	    String aLineFromFile = null;
	    
	    	
	    while ((aLineFromFile = br.readLine()) != null){
	            JOptionPane.showMessageDialog(null, aLineFromFile);
	    }        
	    br.close();
	    fis.close();
		}
	} catch (HeadlessException | IOException e1) {
		 JOptionPane.showMessageDialog(null, "Nenhum Jogo Registrado Anteriormente!");
		 
	}

	}
	
	
	// Recebe um valor para o tamanho do trajeto
	// Como a tela é fixa, o tamanho do trajeto impacta na quantidade de pixel que os carros movem-se
	int chegada = Integer.parseInt( JOptionPane.showInputDialog("Tamanho do trajeto(Min: 200)"));
	// Bloco Try/Catch
	{
	// Trata a excessao do tamanho invalido
	while(chegada < 200) {
		try {
			throw new TamanhoInvalidoException();
		} catch (TamanhoInvalidoException e) {
			JOptionPane.showMessageDialog(null, "Tamanho Invalido!");
			chegada = Integer.parseInt( JOptionPane.showInputDialog("Tamanho do trajeto(Min: 200)"));
			
		}
	}
	
	}
	
	
	int taxa = 400/(chegada/10);
	int aux = 0;
	int aux2 = 0;
	
	// Cria os objetos usados no jogo	
	Desenho background = new Desenho("background.gif", 960, 640);
	JLabel labelBackground = new JLabel(background.getImg());
	
	DesenhoMovel jogador1 = new DesenhoMovel("cop.png", 0, 490);
	DesenhoMovel jogador2 = new DesenhoMovel("bandit.png",200,490);
	
		
	JLabel labelJogador1 = new JLabel(jogador1.getImg());
	JLabel labelJogador2 = new JLabel(jogador2.getImg());
	
	
		
	public Principal() {
		
		//Cofigura a Tela
		addKeyListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(960, 640);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);
        setResizable(false);
	        
	    //Adciona os objetos	        
		this.add(labelJogador1);
		this.add(labelJogador2);
		this.add(labelBackground);
		
		//Adciona os limetes nos objetos   
		labelJogador1.setBounds(jogador1.getX(),jogador1.getY(),150,77);
		labelJogador2.setBounds(jogador2.getX(),jogador2.getY(),230,66);
		labelBackground.setBounds(0, 0, background.getX(), background.getY());
		
		//Instacia a Thread e começa ela
		Thread t1 = new Thread(this);// Ele fica enviando ele mesmo
	    t1.start();     
	}
	//Metodo Main que arremessa 2 exceções
	public static void main(String[] args)  throws TamanhoInvalidoException, ComandoInvalidoException {
		Principal app = new Principal();
		
		
		
	}
	
	public void run() {
		//Bloco do jogo		
		while(true) {
			try {
				//A cada 0.1 segundos faz a checagem do jogo
				sleep(300);
				//Checagem do Jogador1(Fugitivo)
				if(aux2 > 0) {
					for(int i=0;i<aux2*taxa;i++) {
					jogador2.moverDireita(1);
					if(jogador2.getX() >= 640) {
						JOptionPane.showMessageDialog(null, "Você escapou com sucesso!");
						try {
							FileOutputStream fos = new FileOutputStream(file);
							String s = "Vencedor: Fugitivo \n Tamanho do trajeto: "+chegada+"\nPolicial chegou aos: "+jogador1.getX();
							byte data[] = s.getBytes();
							fos.write(data);
							fos.close();
						
							}catch (FileNotFoundException e) {
								
						    } catch (IOException error) {
								
							}

						System.exit(0);
					}
					}
					aux2 = 0;
					
				}
				
				//Checagem do Jogador2(Policial)
				if(aux > 0) {
					for(int i=0;i<aux*taxa;i++) {
						jogador1.moverDireita(1);
						if((jogador2.getX()-130) - jogador1.getX()<= 0) {
							JOptionPane.showMessageDialog(null, "Vc foi pego");
							try {
								FileOutputStream fos = new FileOutputStream(file);
								String s = "Vencedor: Policial \n Tamanho do trajeto: "+chegada+"\n Captura aos: "+jogador2.getX()+"m";
								byte data[] = s.getBytes();
								fos.write(data);
								fos.close();
							
								}catch (FileNotFoundException e) {
									
							    } catch (IOException error) {
									
								}
							System.exit(0);
						}
						}
						aux = 0;
				}
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			
			}
			
		}
		
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override //Eventos de teclado para servir de controles para os jogadores
	public void keyReleased(KeyEvent e) {
	
		if(e.getKeyCode() == 39) {
			
			aux2++;
			labelJogador2.setBounds(jogador2.getX(),jogador2.getY(),230,66);
		}
		if(e.getKeyCode() == 68 ) {
			
			aux++;
			labelJogador1.setBounds(jogador1.getX(),jogador1.getY(),150,77);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
}