package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class AStarListener implements ActionListener {

	JFrame frame;
	
	public AStarListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Thread thread = new Thread(){
		    public void run(){
				((MyFrame) frame).aStarAI();
		    }
		  };

		  thread.start();
	}

}
