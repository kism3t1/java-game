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
        setTitle("Kism3t Game V0.000000000000.2 - Level 1");
        setResizable(false);
        setVisible(true);
	}
	
	
	
// The original way of setting up the main JFrame - not used 06/09/11
	
		/*
		JFrame frame = new JFrame("Kism3t Game V0.000000000000.1 - Level 1");
		frame.add(new Level1());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(530, 555);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	*/
	
	public static void main(String[] args){
		
		new Board();

	}

}
