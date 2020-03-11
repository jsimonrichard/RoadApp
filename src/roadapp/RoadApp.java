package roadapp;

import java.awt.*;
import javax.swing.*;

import roadapp.window.WindowHandler;

public class RoadApp {
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
