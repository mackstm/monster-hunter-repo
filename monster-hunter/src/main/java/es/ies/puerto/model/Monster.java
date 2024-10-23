package es.ies.puerto.model;

import java.util.Random;

/**
 * Class that represents monster
 * @author Jose Maximiliano Boada Martin <maxibapl@gmail.com>
 */
public class Monster extends Thread {

    /**
     * Properties
     */
    private int idMonster;
    private String monsterName;
    private String position;
    private boolean hunted;
    GameMap gameMap;
    private Cave cave;

    /**
     * Default constructor
     */
    public Monster() {
        position = "";
        hunted = false;
        gameMap = new GameMap();
        cave = new Cave();
    }

    /**
     * Constructor with id and name
     * @param idMonster
     * @param monsterName
     */
    public Monster(int idMonster, String monsterName, GameMap gameMap) {
        this.idMonster = idMonster;
        this.monsterName = monsterName;
        position = "";
        hunted = false;
        this.gameMap = gameMap;
        cave = new Cave();
    }

    /**
     * Getters and setters
     */
    public int getIdMonster() {
        return idMonster;
    }

    public void setIdMonster(int id) {
        this.idMonster = id;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isHunted() {
        return hunted;
    }

    public void setHunted(boolean captured) {
        this.hunted = captured;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Cave getCave() {
        return cave;
    }

    public void setCave(Cave cave) {
        this.cave = cave;
    }

    @Override
    public void run() {
        long initialTime = System.currentTimeMillis();
        long timePassed = 0;
        int movement = 0;
        gameMap.addMonster(this);

        while (!hunted && timePassed < GameMap.TIME_REMAINING && movement != -1) {
            Random random = new Random();
            int randomTime = random.nextInt(1500) + 501;
        
            if (!hunted) {
                movement = gameMap.moveMonster(this);
            }

            if (movement == -1) {
                System.out.println(monsterName + " died in a trap!");
                break;
            }

            if (!cave.isOccupied()) {
                try {
                    cave.enterCave(this, gameMap);

                    Thread.sleep(3000);

                    cave.exitCave(this, gameMap);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            long endTime = System.currentTimeMillis();
            timePassed = (endTime - initialTime);

            try {
                Thread.sleep(randomTime);
            } catch (InterruptedException e) {
                System.out.println(monsterName + " interrupted");
            }
        }
    }

    @Override
    public String toString() {
        return "Monster [id=" + idMonster + ", monsterName=" + monsterName + ", position=" + position + ", captured="
                + hunted + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idMonster;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Monster other = (Monster) obj;
        if (idMonster != other.idMonster)
            return false;
        return true;
    }

    

}
