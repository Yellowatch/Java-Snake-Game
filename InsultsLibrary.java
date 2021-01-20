package Game;

public class InsultsLibrary {
	private String insults[] = new String[7];
	private String MInsults[] = new String[7];
	
	public InsultsLibrary() {
		insults[0] = "Wow! That was so bad. Did you really think you can play good enough, I mean, after all, it's you we're talking about.";
		insults[1] = "You’re like the grey sprinkle on a rainbow cupcake. Absolutely trash.";
		insults[2] = "Is that all you can do? You are more disappointing than an unsalted pretzel.";
		insults[3] = "It’s literally impossible to underestimate you";
		insults[4] = "You bloody loser. I’m not insulting you, I’m describing you";
		insults[5] = "Keep rolling your eyes, you might eventually find a brain";
		insults[6] = "I thought of you today. It reminded me to take out the trash. Seeing you play this bad had the same effect.";
		MInsults[0] = "You both suck!";
		MInsults[1] = "You bloody losers!";
		MInsults[2] = "Alright wtf was that garbage?";
		MInsults[3] = "HA you guys are so bad!";
	}
	
	public String getInsult(int i) {
		return insults[i];
	}
	
	public String getMultiplayerInsults(int i) {
		return MInsults[i];
	}
}
