package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TABLES")
public class Tables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private Long timer;

    public long getTimer() { return timer; }

    public void setTimer(long input) {
        this.timer = input;
    }

}
