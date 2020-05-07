package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class GameTokenPutDTO {


    private int npc;
    private int card;
    private int generation;


    public int getNpc() {
        return npc;
    }
    public void setNpc(int npc) {
        this.npc = npc;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }
}
