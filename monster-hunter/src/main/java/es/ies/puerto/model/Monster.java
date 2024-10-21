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

    /**
     * Default constructor
     */
    public Monster() {
        position = "";
        hunted = false;
        gameMap = new GameMap();
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

    @Override
    public void run() {
        long initialTime = System.currentTimeMillis();
        long timePassed = 0;
        gameMap.addMonster(this);

        while (!hunted && timePassed < GameMap.TIME_REMAINING) {
            Random random = new Random();
            int randomTime = random.nextInt(1500) + 501;
            
            if (!hunted) {
                gameMap.moveMonster(this);
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
