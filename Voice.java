package Game;

import java.io.IOException;
import java.util.Random;

import com.darkprograms.speech.synthesiser.SynthesiserV2;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class Voice {
	SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	Random random = new Random();
	public static final int MAX_VOICE = 3;
	public static int voiceNum = 0;

	public Voice() {
		
	}
	
	public void play() {
		voiceNum++;
		if (voiceNum <= MAX_VOICE) {
			speak(new InsultsLibrary().getInsult(random.nextInt(7)));
		}
	}

	private void speak(String text) {
		Thread thread = new Thread(() -> {
			try {
				AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
				player.play();

			} catch (IOException | JavaLayerException e) {

				e.printStackTrace(); 

			}
		});
		thread.setDaemon(false);
		thread.start();
	}

	public void playTwoPlayer() {
		voiceNum++;
		if (voiceNum <= MAX_VOICE) {
			speak(new InsultsLibrary().getMultiplayerInsults(random.nextInt(4)));
		}
	}
}
