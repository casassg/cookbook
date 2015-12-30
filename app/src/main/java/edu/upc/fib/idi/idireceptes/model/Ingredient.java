package edu.upc.fib.idi.idireceptes.model;

import java.util.List;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class Ingredient implements Entity{
    private String name;
    private Long id;
    private List<Ingredient> substituts;

    public List<Ingredient> getSubstituts() {
        return substituts;
    }

    public void setSubstituts(List<Ingredient> substituts) {
        this.substituts = substituts;
    }

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
