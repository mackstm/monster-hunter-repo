package es.ies.puerto.model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Cave {
    private final Semaphore semaphore;
    int maximum;
    String position;
    boolean occupied;
    Set<Monster> monsters;

    /**
     * Default constructor of the class
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Cave() {
        this.maximum = 1;
        this.position = "0,0";
        this.semaphore = new Semaphore(maximum);
        this.occupied = false;
        this.monsters = new ConcurrentHashMap().keySet();
    }

    /**
     * Constructor with capacity and positions
     * @param maximum
     * @param position
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Cave(int maximum, String position) {
        this.maximum = maximum;
        this.position = position;
        this.semaphore = new Semaphore(maximum);
        this.occupied = false;
        this.monsters = new ConcurrentHashMap().keySet();
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int capacity) {
        this.maximum = capacity;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Set<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(Set<Monster> monsters) {
        this.monsters = monsters;
    }

    /**
     * Function to enter a cave
     */

    public synchronized void enterCave(Monster monster, GameMap mapGame) throws InterruptedException {
        semaphore.acquire();
        mapGame.addMonsterToCave(monster);
        String[] position = monster.getPosition().split(",");
        int x = Integer.parseInt(position[0]);
        int y = Integer.parseInt(position[1]);
        mapGame.getMap()[x][y] = "*";
        setOccupied(true);
        System.out.println(monster.getMonsterName() + " has entered the cave.");
    }

    /**
     * Function to exit a cave
     */
    public synchronized void exitCave(Monster monster, GameMap mapGame) {
        semaphore.release();
        mapGame.removeMonsterFromCave(monster);
        String[] position = monster.getPosition().split(",");
        int x = Integer.parseInt(position[0]);
        int y = Integer.parseInt(position[1]);
        mapGame.getMap()[x][y] = "M";
        setOccupied(false);
        System.out.println(monster.getMonsterName() + " has exited the cave.");
    }

    /**
     * Getters and setters
     */
   
}
