package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {

	private GameAction[] keys = new GameAction[65535];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (keys[key] != null) {
			if (action != GLFW.GLFW_RELEASE) {
				keys[key].press();
			}else{
				keys[key].release();
			}
		}
	}
	
	public void setGameAction(GameAction gameAction, int key){
		keys[key] = gameAction;
	}

}