package es.ies.puerto.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import es.ies.puerto.model.GameMap;
import es.ies.puerto.model.Hunter;
import es.ies.puerto.model.Monster;

public class RunHunt {
    GameMap gameMap;
    List<Monster> monsterList;
    List<Hunter> hunterList;

    public RunHunt() {
        gameMap = new GameMap();
        monsterList = new CopyOnWriteArrayList<>();
        hunterList = new CopyOnWriteArrayList<>();
    }


    /**
     * Getters/setters
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(List<Monster> monsterList) {
        this.monsterList = monsterList;
    }

    public List<Hunter> getHunterList() {
        return hunterList;
    }

    public void setHunterList(List<Hunter> hunterList) {
        this.hunterList = hunterList;
    }


    public static void main(String[] args) {
        

        GameMap gameMap = new GameMap(5);
        Hunter hunter1 = new Hunter("Max", gameMap);
        Hunter hunter2 = new Hunter("Pepe", gameMap);
        Monster monster1 = new Monster(1, "Rathalos", gameMap);
        Monster monster2 = new Monster(2, "Rathian", gameMap);

        List<Monster> monsterList = new ArrayList<>(Arrays.asList(monster1, monster2));
        List<Hunter> hunterList = new ArrayList<>(Arrays.asList(hunter1, hunter2));

        RunHunt monsterHunterGame = new RunHunt();
        monsterHunterGame.setGameMap(gameMap);
        monsterHunterGame.setHunterList(hunterList);
        monsterHunterGame.setMonsterList(monsterList);

        createLocations(monsterHunterGame);

        for (Hunter hunter : hunterList) {
            monsterHunterGame.getGameMap().addHunter(hunter);
        }
        for (Monster monster : monsterList) {
            monsterHunterGame.getGameMap().addMonster(monster);
        }

        Thread hunter1Thread = new Thread(hunter1);
        Thread hunter2Thread = new Thread(hunter2);

        Thread monster1Thread = new Thread(monster1);
        Thread monster2Thread = new Thread(monster2);

        hunter1Thread.start();
        hunter2Thread.start();

        monster1Thread.start();
        monster2Thread.start();

        try {
            hunter1Thread.join();
            hunter2Thread.join();

            monster1Thread.join();
            monster2Thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public static void createLocations(RunHunt monsterHunter) {
        for (Hunter hunter : monsterHunter.getHunterList()) {
            String location;
            do {
                location = monsterHunter.getGameMap().generateLocations();
            } while (overlap(location, monsterHunter.getHunterList(), monsterHunter.getMonsterList()));
            hunter.setPosition(location);
        }

        for (Monster monster : monsterHunter.getMonsterList()) {
            String location;
            do {
                location = monsterHunter.getGameMap().generateLocations();
            } while (overlap(location, monsterHunter.getHunterList(), monsterHunter.getMonsterList()));
            monster.setPosition(location);
        }

    }

    public static boolean overlap(String position, List<Hunter> hunters, List<Monster> monsters){
        for (Hunter hunter : hunters) {
            if (hunter.getPosition() != null && hunter.getPosition().equals(position)) {
                return true;
            }
        }
        for (Monster monster : monsters) {
            if (monster.getPosition() != null && monster.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }
}
