package es.ies.puerto.model;

import java.util.Objects;

/**
 * Class that represents monster
 * @author Jose Maximiliano Boada Martin <maxibapl@gmail.com>
 */
public class Monster {

    /**
     * Properties
     */
    private int id;
    private String monsterName;
    private String position;
    private boolean captured;

    /**
     * Default constructor
     */
    public Monster() {
        position = "";
        captured = false;
    }

    /**
     * Constructor with id and name
     * @param id
     * @param monsterName
     */
    public Monster(int id, String monsterName) {
        this.id = id;
        this.monsterName = monsterName;
        position = "";
        captured = false;
    }

    /**
     * Getters and setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    @Override
    public String toString() {
        return "Monster [id=" + id + ", monsterName=" + monsterName + ", position=" + position + ", captured="
                + captured + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        if (id != other.id)
            return false;
        return true;
    }

    

}
