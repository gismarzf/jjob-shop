package org.gismarzf.jjobshop;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application {

	private Stage stage;
	public static final double FUNCIONAL_OPTIMO = 1784.0;
	public static final int MAX_OPERATIONS = 300;
	public static final int MAX_JOBS = 30;

	// private final Optimizer opt = new Optimizer(60000);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		// start with the login screen
		final StageWrapper loginStage =
			new StageWrapper("MainGUI.fxml",
				"Trabajo Practico JOB SHOP SCHEDULING");
		this.stage = loginStage.getStage();
		this.stage.show();

		// so the controller knows the optimizer
		// ((Hookable) loginStage.getController()).setHook(opt);

	}
}