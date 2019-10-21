package entities;

import java.util.Date;
import java.util.HashMap;

public class Vocations {
    private Integer id;
    private HashMap<Integer, Date> startVocation;
    private HashMap<Integer, Date> endVocation;

    public Vocations(){
        //Used by Hibernate
    }

    public Integer getId() {
        return id;
    }

    public HashMap<Integer, Date> getStartVocation() {
        return startVocation;
    }

    public HashMap<Integer, Date> getEndVocation() {
        return endVocation;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStartVocation(HashMap<Integer, Date> startVocation) {
        this.startVocation = startVocation;
    }

    public void setEndVocation(HashMap<Integer, Date> endVocation) {
        this.endVocation = endVocation;
    }
}
