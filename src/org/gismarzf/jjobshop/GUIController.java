package org.gismarzf.jjobshop;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.gismarzf.util.Observer;

public class GUIController implements Hookable, Observer {

	@FXML
	private Button empezarButton;
	@FXML
	private Button pausaButton;
	@FXML
	private Button stopButton;
	@FXML
	private AreaChart<Number, Number> areaChart;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private ComboBox<Metaheuristica> metaheuristicaComboBox;
	@FXML
	private TextField maxListaTabuTextField;
	@FXML
	private Label maxListaTabuLabel;

	@FXML
	private Label temperaturaLabel;
	@FXML
	private Label factorLabel;
	@FXML
	private TextField factorField;
	@FXML
	private TextField temperaturaField;

	private final Optimizer opt = new Optimizer(0);
	private final Excel er = new Excel(GUI.MAX_OPERATIONS);

	private transient Boolean isStopped;

	private XYChart.Series<Number, Number> data;
	private MyLogger mlogger;

	public enum Metaheuristica {
		RECOCIDO, TABU
	}

	@Override
	public void setHook(Object o) {
	}

	@FXML
	public void initialize() {

		yAxis.setLabel("Funcional");
		xAxis.setLabel("Tiempo (minutos)");

		ObservableList<Metaheuristica> items =
			FXCollections.observableArrayList();

		items.add(Metaheuristica.RECOCIDO);
		items.add(Metaheuristica.TABU);
		metaheuristicaComboBox.setItems(items);
		metaheuristicaComboBox.getSelectionModel().select(
			Metaheuristica.TABU);

		opt
			.setCreateNeighbourhood(new CreateNeighbourhoodAsCritical());

	}

	@FXML
	public void changeMetaheuristica() {

		if (metaheuristicaComboBox.getSelectionModel()
			.getSelectedItem() == Metaheuristica.RECOCIDO) {
			maxListaTabuLabel.setManaged(false);
			maxListaTabuTextField.setManaged(false);
			maxListaTabuLabel.setVisible(false);
			maxListaTabuTextField.setVisible(false);

			temperaturaField.setManaged(true);
			temperaturaLabel.setManaged(true);
			temperaturaField.setVisible(true);
			temperaturaLabel.setVisible(true);
			factorField.setManaged(true);
			factorLabel.setManaged(true);
			factorField.setVisible(true);
			factorLabel.setVisible(true);

		} else if (metaheuristicaComboBox.getSelectionModel()
			.getSelectedItem() == Metaheuristica.TABU) {
			maxListaTabuLabel.setManaged(true);
			maxListaTabuTextField.setManaged(true);
			maxListaTabuLabel.setVisible(true);
			maxListaTabuTextField.setVisible(true);

			temperaturaField.setManaged(false);
			temperaturaLabel.setManaged(false);
			temperaturaField.setVisible(false);
			temperaturaLabel.setVisible(false);
			factorField.setManaged(false);
			factorLabel.setManaged(false);
			factorField.setVisible(false);
			factorLabel.setVisible(false);
		}
		;

	}

	@FXML
	public void start() {
		isStopped = false;

		data = new Series<Number, Number>();
		areaChart.getData().add(data);

		opt.setInitialSolution(GUI.MAX_JOBS, er.getOperations());

		if (metaheuristicaComboBox.getSelectionModel()
			.getSelectedItem() == Metaheuristica.RECOCIDO) {

			Integer temperatura =
				Integer.parseInt(temperaturaField.getText());
			Double factor = Double.parseDouble(factorField.getText());

			opt.setChooseSolution(new ChooseNextSolutionWithSA(
				temperatura, factor));

		} else if (metaheuristicaComboBox.getSelectionModel()
			.getSelectedItem() == Metaheuristica.TABU) {

			Integer maxTabu =
				Integer.parseInt(maxListaTabuTextField.getText());

			opt.setChooseSolution(new ChooseNextSolutionWithTabuList(
				maxTabu));
		}

		Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {

				mlogger = new MyLogger(opt);

				opt.getTimer().setStart();

				while (!isStopped) {
					opt.calculateNeighbourhood();
					opt.calculateNextSolution();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							update(opt);
						}
					});
				}
			}
		});

		if (opt.getChooseSolution() != null) {
			tr.start();
		}

	}

	@FXML
	public void stop() {
		isStopped = true;
	}

	@Override
	public void update(Object o) {
		Optimizer opt = (Optimizer) o;

		data.getData().add(
			new Data<Number, Number>(opt.getTimer()
				.getElapsedMinutes(), opt.getThisSolution()
				.getFunctional()));

	}

	@FXML
	public void reset() {
		areaChart.getData().clear();
	}

	@FXML
	public void exportar() {
		er.export(opt, mlogger);
	}

	@FXML
	public void close() {
		Platform.exit();
	}
}
