package osrspingchecker.controller;

import osrspingchecker.model.OSRSWorldInfo;
import osrspingchecker.model.World;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

	private OSRSWorldInfo worldInfo;

	@FXML Button refreshButton;
	@FXML TableView<World> worldTable;
	@FXML TableColumn<World, Integer> worldNumberColumn;
	@FXML TableColumn<World, Integer> worldPlayerCountColumn;
	@FXML TableColumn<World, String> worldTypeColumn;
	@FXML TableColumn<World, String> worldRegionColumn;
	@FXML TableColumn<World, String> worldActivityColumn;
	@FXML TableColumn<World, Integer> worldPingColumn;

	@FXML
	private void initialize() {
		ObservableList<World> data = FXCollections.observableArrayList();

		worldNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		worldPlayerCountColumn.setCellValueFactory(new PropertyValueFactory<>("playerCount"));
		worldTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		worldRegionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
		worldActivityColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));
		worldPingColumn.setCellValueFactory(new PropertyValueFactory<>("ping"));

		worldInfo = new OSRSWorldInfo();
		ArrayList<World> worlds = worldInfo.getWorlds();

		data.addAll(worlds);
		worldTable.setItems(data);
	}

	@FXML
	private void onRefreshButtonPress(ActionEvent e) {
		worldInfo.refresh();
		worldTable.refresh();
	}
}
