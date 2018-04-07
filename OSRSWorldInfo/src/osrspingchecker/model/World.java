package osrspingchecker.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class World {
	
	private SimpleIntegerProperty number, playerCount, ping;
	private SimpleStringProperty type, region, activity;
	
	public World(int number, int playerCount, String type, String region, String activity) {
		this.number = new SimpleIntegerProperty(number);
		this.playerCount = new SimpleIntegerProperty(playerCount);
		this.type = new SimpleStringProperty(type);
		this.region = new SimpleStringProperty(region);
		this.activity = new SimpleStringProperty(activity);
	}
	
	public Integer getNumber() {
		return number.get();
	}
	
	public Integer getPlayerCount() {
		return playerCount.get();
	}
	
	public String getType() {
		return type.get();
	}
	
	public String getRegion() {
		return region.get();
	}
	
	public String getActivity() {
		return activity.get();
	}
	
	public Integer getPing() {
		return ping.get();
	}
	
	public void setPing(int ping) {
		this.ping = new SimpleIntegerProperty(ping);
	}
}
