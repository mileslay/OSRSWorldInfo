package osrspingchecker.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OSRSWorldInfo {
	
	private final String worldListURL = "http://oldschool.runescape.com/slu?order=WPMLA";
	private Document worldListPage;
	private ArrayList<World> worlds;
	
	private ThreadPoolExecutor executor;
	private final int PING_COUNT = 1;
	private final int THREAD_COUNT = 4;
	private final Pattern PING_AVG_PATTERN = Pattern.compile("Average = (\\d+)ms");

	public OSRSWorldInfo() {
		worlds = scrapeWorldData();
		pingAll();
	}
	
	public ArrayList<World> scrapeWorldData() {
		ArrayList<World> worlds = new ArrayList<World>();
		
		try {
			worldListPage = Jsoup.connect(worldListURL).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Elements rows = worldListPage.getElementsByClass("server-list__row");
		
		for(Element row: rows) {
			Elements columns = row.children();
			
			final String WORLD_NUMBER_TEXT = columns.get(0).text();
			int worldNumIndex = WORLD_NUMBER_TEXT.contains("OldSchool") ? 10 : 11;
			int worldNumber = Integer.parseInt(WORLD_NUMBER_TEXT.substring(worldNumIndex));

			final String PLAYER_COUNT_TEXT = columns.get(1).text();
			int playerNumIndex = PLAYER_COUNT_TEXT.indexOf("players");
			int playerCount = 0;
			if (playerNumIndex != -1)
				playerCount = Integer.parseInt(PLAYER_COUNT_TEXT.substring(0, playerNumIndex - 1));

			String region = columns.get(2).text();
			String type = columns.get(3).text();
			String activity = columns.get(4).text();
			
			if(activity.equals("-"))
				activity = "";
			
			worlds.add(new World(worldNumber, playerCount, region, type, activity));
		}
		
		return worlds;
	}

	public int ping(String ip) {
		Matcher matcher;
		
		try {
			Process process = Runtime.getRuntime().exec("ping " + ip + " -n " + PING_COUNT);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
			String output = "", s = "";
			while ((s = inputStream.readLine()) != null)
				output += s;
			
			matcher = PING_AVG_PATTERN.matcher(output);
		} catch(IOException e) {
			e.printStackTrace();
			return -1;
		}
		
		CopyOnWriteArrayList<String> matches = new CopyOnWriteArrayList<String>();
		while(matcher.find())
			matches.add(matcher.group(1));
		
		int ping = Integer.parseInt(matches.get(0));
		
		return ping;
	}
	
	private Future<Integer> pingFuture(String ip) {
		return executor.submit(() -> {
			return ping(ip);
		});
	}
	
	public void pingAll() {
		HashMap<World, Future<Integer>> pingMap = new HashMap<World, Future<Integer>>();
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
		
		for(World world: worlds) 
			pingMap.put(world, pingFuture(getWorldIP(world.getNumber())));
			
		for(World world: worlds) {
			try {
				world.setPing(pingMap.get(world).get(30, TimeUnit.SECONDS));
				System.out.println("World " + world.getNumber() + " ping: " + world.getPing());
			} catch (Exception e) {
				world.setPing(-1);
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
	}
	
	public void refresh() {
		worlds = scrapeWorldData();
		pingAll();
	}

	public String getWorldIP(int worldNumber) {
		return "oldschool" + worldNumber + ".runescape.com";
	}
	
	public ArrayList<World> getWorlds(){
		return worlds;
	}
}
