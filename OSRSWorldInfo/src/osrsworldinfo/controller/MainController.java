package osrsworldinfo.controller;

import osrsworldinfo.model.OSRSWorldInfo;
import osrsworldinfo.model.World;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

	private OSRSWorldInfo worldInfo;
	private ObservableList<World> data;

	@FXML TableView<World> worldTable;
	@FXML TableColumn<World, Integer> worldNumberColumn;
	@FXML TableColumn<World, String> worldTypeColumn;
	@FXML TableColumn<World, Integer> worldPlayerCountColumn;
	@FXML TableColumn<World, String> worldActivityColumn;
	@FXML TableColumn<World, String> worldLocationColumn;
	@FXML TableColumn<World, Integer> worldPingColumn;
	
	@FXML Button setOSBuddyDefaultButton;
	@FXML Button refreshButton;
	@FXML ProgressBar refreshProgressBar;

	@FXML
	private void initialize() {
		worldNumberColumn.setCellValueFactory(new PropertyValueFactory<World, Integer>("number"));
		worldTypeColumn.setCellValueFactory(new PropertyValueFactory<World, String>("type"));
		worldPlayerCountColumn.setCellValueFactory(new PropertyValueFactory<World, Integer>("playerCount"));
		worldActivityColumn.setCellValueFactory(new PropertyValueFactory<World, String>("activity"));
		worldLocationColumn.setCellValueFactory(new PropertyValueFactory<World, String>("location"));
		worldPingColumn.setCellValueFactory(new PropertyValueFactory<World, Integer>("ping"));

		worldInfo = new OSRSWorldInfo();
	}

	@FXML
	private void onRefreshButtonPress(ActionEvent e) {
		worldTable.setDisable(true);
		refreshButton.setDisable(true);
		setOSBuddyDefaultButton.setDisable(true);
		
		worldInfo.refresh();
		data = FXCollections.observableArrayList(worldInfo.getWorlds());
		
		worldTable.setItems(data);
		
		worldTable.setDisable(false);
		refreshButton.setDisable(false);
		setOSBuddyDefaultButton.setDisable(false);
	}
	
	@FXML
	private void onSetOSBuddyDefaultButtonPress(ActionEvent e) {
		int worldNumber = worldTable.getSelectionModel().getSelectedItem().getNumber() + 300;
		
		String configPath = System.getProperty("user.home") + "\\OSBuddy\\orion.ini";
		Properties osbuddySettings = new Properties();
		
		try {
			osbuddySettings.load(new FileInputStream(configPath));
			osbuddySettings.setProperty("default_world", Integer.toString(worldNumber));
			osbuddySettings.store(new FileOutputStream(configPath), null);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Alert alert = new Alert(AlertType.INFORMATION, 
				"Your OSBuddy default world is now World " + worldNumber + ".");
		alert.setTitle("OSRS World Info");
		alert.setHeaderText(null);
		alert.show();	
	}
}
