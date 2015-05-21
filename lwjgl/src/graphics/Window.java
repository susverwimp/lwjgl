package graphics;

import input.GameAction;
import input.Input;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import entities.Camera;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private int width;
	private int height;
	private String title;
	private Input keyCallback;
	private long window;
	
	private GameAction left;
	private GameAction right;
	private GameAction up;
	private GameAction down;
	private GameAction forward;
	private GameAction backward;

	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public boolean init() {
		if (GLFW.glfwInit() != GL11.GL_TRUE) {
			System.out.println("Could not initialize glfw");
			return false;
		}

		// Configure our window
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);

		window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);

		if (window == NULL) {
			System.out.println("window is not created");
			return false;
		}

		GLFW.glfwSetKeyCallback(window, keyCallback = new Input());

		createGameActions();

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = GLFW
				.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2,
				(GLFWvidmode.height(vidmode) - height) / 2);

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwShowWindow(window);

		GLContext.createFromCurrent();

		return true;
	}

	public void update(Camera camera) {
		GLFW.glfwPollEvents();
		if (left.isPressed()) {
			camera.getPosition().x -= 0.5f;
		}
		if (right.isPressed()) {
			camera.getPosition().x += 0.5f;
		}
		if (up.isPressed()) {
			camera.getPosition().y += 0.5f;
		}
		if (down.isPressed()) {
			camera.getPosition().y -= 0.5f;
		}
		if (forward.isPressed()) {
			camera.getPosition().z -= 0.5f;
		}
		if (backward.isPressed()) {
			camera.getPosition().z += 0.5f;
		}
	}

	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}

	public boolean WindowShouldBeClosed() {
		return GLFW.glfwWindowShouldClose(window) == GL11.GL_TRUE;
	}

	public void cleanUp() {
		keyCallback.release();
		GLFW.glfwTerminate();
	}

	public long getWindow() {
		return window;
	}

	private void createGameActions() {
		left = new GameAction("left");
		right = new GameAction("right");
		up = new GameAction("up");
		down = new GameAction("down");
		forward = new GameAction("forward");
		backward = new GameAction("backward");

		keyCallback.setGameAction(left, GLFW.GLFW_KEY_A);
		keyCallback.setGameAction(right, GLFW.GLFW_KEY_D);
		keyCallback.setGameAction(up, GLFW.GLFW_KEY_W);
		keyCallback.setGameAction(down, GLFW.GLFW_KEY_S);
		keyCallback.setGameAction(forward, GLFW.GLFW_KEY_Z);
		keyCallback.setGameAction(backward, GLFW.GLFW_KEY_X);
	}
}
