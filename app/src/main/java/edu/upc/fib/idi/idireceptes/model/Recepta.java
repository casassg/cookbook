package edu.upc.fib.idi.idireceptes.model;

import java.util.List;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class Recepta  implements Entity{
    private String description;
    private List<Integer> ingredients;
    private String title;
    private long id;

    public Recepta(String description, List<Integer> ingredients, String title, int id) {
        this.description = description;
        this.ingredients = ingredients;
        this.title = title;
        this.id = id;
    }

    public Recepta() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
