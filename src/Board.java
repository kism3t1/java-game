import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Board extends JFrame {

	public Board() {

		add(new Level1());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(710, 730);
		setLocationRelativeTo(null);
		setTitle("Java-Game V0.1 - Map Editor");
		setResizable(true);
		setVisible(true);
	}

	public static void main(String[] args) {

		new Board();

	}

}
