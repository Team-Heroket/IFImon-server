package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class GameTokenPutDTO {


    private int npc;
    private int card;


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
}
