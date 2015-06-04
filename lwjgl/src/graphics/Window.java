package graphics;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private int width;
	private int height;
	private String title;
	private long window;
	
	
	private static final int MAX_KEYS = 1024;
	private static final int MAX_MOUSE_BUTTONS = 32; 
	
	private boolean[] keys = new boolean[MAX_KEYS];
	private boolean[] mouseButtons = new boolean[MAX_MOUSE_BUTTONS];
	
	private double mouseX;
	private double mouseY;
	private double mouseDX;
	private double mouseDY;
	
	private double scrollDX;
	private double scrollDY;
	
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWScrollCallback scrollCallback;
	
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
			GLFW.glfwTerminate();
			return false;
		}
		
		GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = action!=GLFW.GLFW_RELEASE;
			}
		});
		GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				mouseButtons[button] = action != GLFW.GLFW_RELEASE;
			}
		});
		GLFW.glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				double oldMouseX = mouseX;
				double oldMouseY = mouseY;
				mouseX = xpos;
				mouseY = ypos;
				mouseDX = mouseX - oldMouseX;
				mouseDY = mouseY - oldMouseY;
			}
		});
		GLFW.glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				scrollDX = xoffset;
				scrollDY = yoffset;
			}
		});

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = GLFW
				.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2,
				(GLFWvidmode.height(vidmode) - height) / 2);

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(window);
		
		GLFW.glfwSwapInterval(1);
		
		GLFW.glfwShowWindow(window);
		
		GLContext.createFromCurrent();

		return true;
	}

	public void update() {
		mouseDX = 0;
		mouseDY = 0;
		scrollDX = 0;
		scrollDY = 0;
		GLFW.glfwPollEvents();
	}

	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}

	public boolean WindowShouldBeClosed() {
		return GLFW.glfwWindowShouldClose(window) == GL11.GL_TRUE;
	}

	public void cleanUp() {
		keyCallback.release();
		mouseButtonCallback.release();
		cursorPosCallback.release();
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public long getWindow() {
		return window;
	}
	
	public boolean isKeyPressed(int key){
		return keys[key];
	}
	
	public boolean isMouseButtonPressed(int mouseButton){
		return mouseButtons[mouseButton];
	}
	
	public float getMouseX(){
		return (float) mouseX;
	}
	
	public float getMouseY(){
		return (float) mouseY;
	}
	
	public float getMouseDX(){
		return (float) mouseDX;
	}
	
	public float getMouseDY(){
		return (float) mouseDY;
	}
	
	public float getScrollDX(){
		return (float) scrollDX;
	}
	
	public float getScrollDY(){
		return (float) scrollDY;
	}
	
	public float getTime(){
		return (float) GLFW.glfwGetTime();
	}
}
