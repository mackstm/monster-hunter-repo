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
    private int successChance;
    public static final long TIME_REMAINING = 15000;
    

    /**
     * Default constructor
     */
    public GameMap() {
        this.size = 10;
        this.areas = new ConcurrentHashMap<>();
        this.map = new String[size][size];
        this.hunters = new CopyOnWriteArrayList<>();
        this.monsters = new CopyOnWriteArrayList<>();
        this.successChance = 7;
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
        this.successChance = 7;
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

    public void setAreas(ConcurrentHashMap<String, String> areas) {
        this.areas = areas;
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

    public void generateEvents(int amount) {
        String types[] = {"T", "S"};
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            map[x][y] = types[0];
        }
    }

    public void showMap(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(map[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public synchronized void addHunter(Hunter hunter) {
        String area = hunter.getPosition();
        if(overlap(area)) {
            hunters.add(hunter);
            String[] positions = area.split(",");
            int row = Integer.parseInt(positions[0]);
            int col = Integer.parseInt(positions[1]);

            map[row][col] = "H";

            areas.put(hunter.getHunterName(), area);
        }
    }

    public boolean overlap(String position) {
        return !areas.containsValue(position);
    }

    public synchronized int moveHunter(Hunter hunter) {
        Random random = new Random();
        int x = random.nextInt(size);
        int y = random.nextInt(size);

        int hunted = random.nextInt(10);

        String[] position = hunter.getPosition().split(",");
        switch (map[x][y]) {
            case "*" -> {
                map[x][y] = "H";
                map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
                hunter.setPosition(x + "," + y);
            }
        
            case "M" -> {
                if (hunted <= this.successChance) {
                    huntMonster(hunter);
                    map[x][y] = "H";
                    map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
                    hunter.setPosition(x + ","+ y);
                } else {
                    return 1;
                }
            }
            
            case "H" -> moveHunter(hunter);

            case "T" -> {
                hunters.remove(hunter);
                map[x][y] = "*";
                map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
                return -1;
            }

            case "S" -> {
                successChance++;
            }
        }
        return 0;
    }

    public synchronized void addMonster(Monster monster) {
        String area = monster.getPosition();
        if(overlap(area)) {
            monsters.add(monster);
            String[] positions = area.split(",");
            int row = Integer.parseInt(positions[0]);
            int col = Integer.parseInt(positions[1]);

            map[row][col] = "M";

            areas.put(String.valueOf(monster.getIdMonster()) + ": " + monster.getMonsterName(), area);
        }
    }

    public synchronized void removeMonster(Monster monster) {
        areas.remove(String.valueOf(monster.getIdMonster()) + ": " + monster.getMonsterName());
        monsters.remove(monster);
    }

    public synchronized int moveMonster(Monster monster) {
        Random random = new Random();
        int x = random.nextInt(size);
        int y = random.nextInt(size);

        String[] position = monster.getPosition().split(",");
        switch (map[x][y]) {
            case "*" -> {
                map[x][y] = "M";
                map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
                monster.setPosition(x + "," + y);
            }
        
            case "M" -> moveMonster(monster);
            
            case "H" -> {
                moveMonster(monster);
                return 1;
            }

            case "T" -> {
                monsters.remove(monster);
                map[x][y] = "*";
                map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
                return -1;
            }

            default -> moveMonster(monster);
        }
        return 0;
    }

    public synchronized boolean huntMonster(Hunter hunter) {
        for (Monster monster : monsters) {
            if (hunter.getPosition().equals(monster.getPosition())) {
                monster.setHunted(true);
                removeMonster(monster);
                monsters.remove(monster);
                return true;
            }
        }
        return false;
    }

}
