import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3023027088533526718L;
	int width = 800, height = 600;
	
	public static void main(String[] args) {
		new Client();
	}
	
	Client () {
		this.setSize(width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		
		// Initialize entities
		
		JPanel panel = new JPanel();
		this.add(panel);
	}	
}
