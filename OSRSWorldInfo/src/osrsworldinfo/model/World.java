package osrsworldinfo.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class World {
	
	private SimpleIntegerProperty number, playerCount, ping;
	private SimpleStringProperty type, activity, location;
	
	public World(int number, String type, int playerCount, String activity, String location) {
		this.number = new SimpleIntegerProperty(number);
		this.type = new SimpleStringProperty(type);
		this.playerCount = new SimpleIntegerProperty(playerCount);
		this.activity = new SimpleStringProperty(activity);
		this.location = new SimpleStringProperty(location);
	}
	
	public Integer getNumber() {
		return number.get();
	}
	
	public String getType() {
		return type.get();
	}
	
	public Integer getPlayerCount() {
		return playerCount.get();
	}
	
	public String getActivity() {
		return activity.get();
	}
	
	public String getLocation() {
		return location.get();
	}
	
	public Integer getPing() {
		return ping.get();
	}
	
	public void setPing(int ping) {
		this.ping = new SimpleIntegerProperty(ping);
	}
}
