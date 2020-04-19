package RoadApp;

import RoadApp.window.WindowHandler;

public class App {
	static WindowHandler handler = new WindowHandler();
	
	static boolean use_splash = false;

	public static void main(String[] args) throws InterruptedException {
		if(use_splash) {
			handler.splash();
		} else {
			handler.loadDirectly();
		}
	}
}
