package es.ies.puerto.model;

import java.util.Random;

/**
 * Good hunters
 * @author Jose Maximiliano Boada Martin <maxibapl@gmail.com>
 */
public class Hunter extends Thread{
    /**
     * Properties
     */
    private String hunterName;
    private String position;
    private GameMap gameMap;
    
    /**
     * Default constructor
     */
    public Hunter() {}

    /**
     * Constructor with name parameter
     * @param name of hunter
     */
    public Hunter(String name) {
        this.hunterName = name;
        position = "0,0";
        this.gameMap = new GameMap();
    }

    /**
     * Constructor with name and map parameters
     * @param name of hunter
     * @param gameMap shared resource
     */
    public Hunter(String name, GameMap gameMap) {
        this.hunterName = name;
        position = "0,0";
        this.gameMap = gameMap;
    }

    /**
     * Getters and setters
     */

    public String getHunterName() {
        return hunterName;
    }

    public void setHunterName(String name) {
        this.hunterName = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * For running hunter thread
     */
    @Override
    public void run() {
        long initialTime = System.currentTimeMillis();
        long timePassed = 0;

        int monsterCaught = 0;
        boolean isOver = false;
        int movement = 0;

        gameMap.addHunter(this);

        while (!isOver && !gameMap.getMonsters().isEmpty() && timePassed < GameMap.TIME_REMAINING && movement != -1) {
            gameMap.showMap();
            Random random = new Random();
            int randomTime = random.nextInt(1500) + 501;
            movement = gameMap.moveHunter(this);
            if (movement == 1) {
                System.out.println(hunterName + " has failed a hunt");
            }



            long endTime = System.currentTimeMillis();
            timePassed = (endTime - initialTime);
            
            if (gameMap.getMonsters().isEmpty()) {
                isOver = true;
            }

            if (movement == -1) {
                System.out.println(hunterName + " died in a trap!");
                break;
            }

            for (Monster monster : gameMap.getMonsters()) {
                if (monster.getPosition().equals(this.getPosition()) && !monster.isHunted()) {
                    monster.setHunted(true);
                    System.out.println(this.getHunterName() + " caught " + monster.getMonsterName());
                    gameMap.removeMonster(monster);
                    gameMap.getMonsters().remove(monster);
                    monsterCaught++;
                    break;
                }
            }

            try {
                Thread.sleep(randomTime);
            } catch (InterruptedException e) {
                System.out.println(hunterName + " interrupted");
            }
        }
        if (timePassed >= GameMap.TIME_REMAINING || gameMap.getMonsters().isEmpty()){
            System.out.println(hunterName + " caught " + monsterCaught + " monsters");
        }
    }

    @Override
    public String toString() {
        return "Hunter [name=" + hunterName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hunterName == null) ? 0 : hunterName.hashCode());
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
        Hunter other = (Hunter) obj;
        if (hunterName == null) {
            if (other.hunterName != null)
                return false;
        } else if (!hunterName.equals(other.hunterName))
            return false;
        return true;
    }
}
