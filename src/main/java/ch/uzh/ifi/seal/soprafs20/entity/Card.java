package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.List;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.Element;

@Entity
@Table(name = "CARD")
public class Card implements Serializable {

    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private Map<Category,Integer> categories;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String spriteURL;

    @Column(nullable = true)
    private String cryURL;

    @Column(nullable = false)
    private List<Element> element;

    @Column(nullable = false)
    private List<Integer> evolutionId;





    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }

    public Map<Category,Integer> getCategories() { return categories; }
    public void setCategories(Map<Category,Integer>) {
        this.categories = categories;
    }

    public String getName() { return name; }
    public void setName(String input) {
        this.name = input;
    }

    public String getSpriteURL() { return spriteURL; }
    public void setSpriteURL(String spriteURL) {
        this.spriteURL = spriteURL;
    }

    public String getCryURL() { return cryURL; }
    public void setCryURL(String cryURL) {
        this.cryURL = cryURL;
    }

    public List<Element> getElement() { return element; }
    public void setElement(List<Element> input) {
        this.element = element;
    }

    public List<Integer> getEvolutionId() { return evolutionId; }
    public void setEvolutionId(List<Integer> evolutionId) {
        this.evolutionId = evolutionId;
    }


}
