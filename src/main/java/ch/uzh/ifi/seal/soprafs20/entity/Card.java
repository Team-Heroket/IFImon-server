package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "CARD")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public String getName() { return name; }

    public void setName(String input) {
        this.name = input;
    }

}
