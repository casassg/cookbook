package edu.upc.fib.idi.idireceptes.model;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class Ingredient implements Entity{
    private String name;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
