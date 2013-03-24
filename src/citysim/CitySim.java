package citysim;
import citysim.controller.meta.GameController;
import citysim.model.meta.Game;

public final class CitySim {
	private final static int STEP_INTERVAL = 1000;
	
	public static void main(String[] args) {
		System.out.println("Welcome to CitySim");
		
		System.out.println("Generating Game...");
		Game game = Game.NewGame();
		System.out.println("Game ready.");
		System.out.println();
		
		System.out.println("Running...");
		run(game);
		
		System.out.println("Good Bye!");
	}
	
	private static void run(Game game) {
		try {
			GameController gameCtrl = new GameController(game);
			gameCtrl.start();
			while(true) {
				gameCtrl.step();
				
				System.out.println("Game status:");
				game.printStatus();
				
				if(game.everyoneDead()) {
					return;
				}
				
				Thread.sleep(STEP_INTERVAL);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
