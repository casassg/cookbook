package edu.upc.fib.idi.idireceptes.model;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class Ingredient implements Entity{
    private String name;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
