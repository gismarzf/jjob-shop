package org.gismarzf.jjobshop;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.gismarzf.util.Observer;

public class StageWrapper {

	private FXMLLoader loader;
	private Pane pane;
	private Stage stage;
	private Observer controller;

	public StageWrapper(String fxmlURI, String title) {

		this.loader = new FXMLLoader(Main.class.getResource(fxmlURI));

		try {
			this.pane = (Pane) loader.load();
		} catch (IOException e) {
			System.out.println("Couldn't load/initialize: " + fxmlURI);
			e.printStackTrace();
		}

		this.stage = new Stage();
		this.stage.setScene(new Scene(pane));
		this.stage.setTitle(title);

		this.controller = loader.getController();

	}

	public Stage getStage() {
		return stage;
	}

	public Observer getController() {
		return controller;
	}
}
