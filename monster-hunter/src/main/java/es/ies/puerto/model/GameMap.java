package es.ies.puerto.model;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that represents game map
 * @author Jose Maximiliano Boada Martin <maxibapl@gmail.com>
 */
public class GameMap {
    /**
     * Properties
     */
    private int size;
    private ConcurrentHashMap<String, String> areas;
    private String[][] map;

    private List<Hunter> hunters;
    private List<Monster> monsters;

    /**
     * Default constructor
     */
    public GameMap() {
        this.size = 10;
        this.areas = new ConcurrentHashMap<>();
        this.map = new String[size][size];
        this.hunters = new CopyOnWriteArrayList<>();
        this.monsters = new CopyOnWriteArrayList<>();
        generateMap();
    }

    /**
     * Constructor with size
     * @param size of map
     */
    public GameMap(int size) {
        this.size = size;
        this.areas = new ConcurrentHashMap<>();
        this.map = new String[size][size];
        this.hunters = new CopyOnWriteArrayList<>();
        this.monsters = new CopyOnWriteArrayList<>();
        generateMap();
    }

    /**
     * Getters and setters
     */

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ConcurrentHashMap<String, String> getAreas() {
        return areas;
    }

    public void setAreas(ConcurrentHashMap<String, String> locations) {
        this.areas = locations;
    }

    public String[][] getMap() {
        return map;
    }

    public void setMap(String[][] map) {
        this.map = map;
    }

    public List<Hunter> getHunters() {
        return hunters;
    }

    public void setHunters(List<Hunter> hunters) {
        this.hunters = hunters;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }



    public String generateLocations() {
        Random random = new Random();
        int x = random.nextInt(size);
        int y = random.nextInt(size);
        return x + "," + y;
    }

    /**
     * Generates base map
     */
    public void generateMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = "*";
            }
        }
    }

    public synchronized void addHunter(Hunter hunter) {
        String location = hunter.getPosition();
        if(overlap(location)) {
            hunters.add(hunter);
            String[] positions = location.split(",");
            int row = Integer.parseInt(positions[0]);
            int col = Integer.parseInt(positions[1]);

            map[row][col] = "H";

            areas.put(hunter.getHunterName(), location);
        }
    }

    public boolean overlap(String position) {
        return !areas.containsValue(position);
    }

    public synchronized void moveHunter(Hunter hunter) {
        Random random = new Random();
        int x = random.nextInt(size);
        int y = random.nextInt(size);

        String[] position = hunter.getPosition().split(",");
        switch (map[x][y]) {
            case "*":
                map[x][y] = "H";
                map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
                hunter.setPosition(x + "," + y);
                break;
        
            case "M":
                huntMonster(monsters, hunter);
                map[x][y] = "H";
                map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
                hunter.setPosition(x + ","+ y);
                break;
            
            case "H":
                moveHunter(hunter);
                break;
        }
    }

    public synchronized void addMonster(Monster monster) {
        String location = monster.getPosition();
        if(overlap(location)) {
            monsters.add(monster);
            String[] positions = location.split(",");
            int row = Integer.parseInt(positions[0]);
            int col = Integer.parseInt(positions[1]);

            map[row][col] = "M";

            areas.put(String.valueOf(monster.getId()) + ": " + monster.getMonsterName(), location);
        }
    }

    public synchronized void removeMonster(Monster monster) {
        String location = monster.getPosition();
        areas.remove(String.valueOf(monster.getId()) + ": " + monster.getMonsterName());
        monsters.remove(monster);
    }

    public synchronized void huntMonster(List<Monster> monsters, Hunter hunter) {
        for (Monster monster : monsters) {
            if (hunter.getPosition().equals(monster.getPosition())) {
                monster.setCaptured(true);
                removeMonster(monster);
                monsters.remove(monster);
                return;
            }
        }
    }



    @Override
    public String toString() {
        String drawnMap = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                switch (map[i][j]) {
                    case "H":
                        drawnMap += "H";
                        break;
                    case "M":
                        drawnMap += "M";
                        break;
                    default:
                        drawnMap += "*";
                        break;
                }
            }
            drawnMap += "\n";
        }
        return drawnMap;
    }

    
}
