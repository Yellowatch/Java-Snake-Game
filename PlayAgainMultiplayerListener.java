package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PlayAgainMultiplayerListener implements ActionListener {

	JFrame frame;
	
	public PlayAgainMultiplayerListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		((MyFrame) frame).playAgainMultiplayer();
	}

}
