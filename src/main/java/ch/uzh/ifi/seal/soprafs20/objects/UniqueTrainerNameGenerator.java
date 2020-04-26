package ch.uzh.ifi.seal.soprafs20.objects;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * "Generate" a unique PokemonName
 */
public class UniqueTrainerNameGenerator {

    /*
        NOTE:
        Is the autowire in GameClass called once or on every request?
        If called on every request, this will result in unexpected behavior!
        TODO: Check this. add different generators?
     */

    private LinkedList<String> trainers;

    /**
     * "Generates" an unique TrainerName
     */
    public UniqueTrainerNameGenerator() {
        this.refresh();
    }

    private void refresh() {
        this.trainers = new LinkedList<>(this.finalTrainers);
        Collections.shuffle(this.trainers);
    }

    /**
     * Get a unique pokemon name
     *
     * @return a unique pokemon name
     */
    public String get() {
        /*
            prevents cap of 809 games
            I assume here we wont have more than 809 games running at once
            If we get famous this should change...
        */
        if (this.trainers.isEmpty()) {
            this.refresh();
        }

        return this.trainers.pop();
    }

    /*
        Since we do everything in memory this shouldn't be a problem either
        Of course we could put into a file at some time.
        There is nothing interesting below this.
     */
    private final List<String> finalTrainers = Arrays.asList(
            "Brock",
            "Misty",
            "Lt.Surge",
            "Erika",
            "Koga",
            "Janine",
            "Sabrine",
            "Blaine",
            "Giovanni",
            "Blue"
    );



}
