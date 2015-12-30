package edu.upc.fib.idi.idireceptes.model;

import java.util.List;

/**
 * Created by casassg on 25/12/15.
 *
 * @author casassg
 */
public class Recepta  implements Entity{
    private String description;
    private List<String> ingredients;
    private String title;
    private Long id;

    public Recepta(String description, List<String> ingredients, String title, long id) {
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

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
