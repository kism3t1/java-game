import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Board extends JFrame{
// The new way to set up the main class using OOP,
// Splitting the main and level1 class into two
	
	public Board(){
		
        add(new Level1());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710,730);
        setLocationRelativeTo(null);
        setTitle("Java-Game V0.1 - Map Editor");
        setResizable(false);
        setVisible(true);
	}
	
	
	public static void main(String[] args){
		
		new Board();

	}

}
