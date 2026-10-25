import boundaries.ControlUI;
import control.GameConfig;
import control.TurnController;

public class Main {
    public static void main(String[] args){
        TurnController game = new TurnController(new GameConfig(42), new ControlUI());
        game.run();
    }
}
