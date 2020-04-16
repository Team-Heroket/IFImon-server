package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.ArrayList;

public class Running implements GameState {
    @Override
    public void addPlayer(Game game, User user) {
        throw new GameBadRequestException("You can't add new players to a running game.");
    }
    @Override
    public void removePlayer(Game game, User user) {
        // TODO: Do this, when the user leaves.
        throw new NotYetImplementedException("Will be implemented in Sprint 3");
    }
    @Override
    public void selectCategory(Game game, Category category) {
        //code here
        game.setCategory(category);
        game.setWinners(getWinner(game));
    }

    @Override
    public void useBerries(Game game, Integer usedBerries, Player player) {
        // does player have this many berries?
        if(player.getBerries()<usedBerries){
            throw new GameBadRequestException("Player doesn't have this many berries left");
        }

        if(player.getDeck().isEmpty()){
            throw new GameBadRequestException("Player has no more cards to evolve");
        }

        //is player allowed to use this many berries?
        if(player.getDeck().peekCard().getEvolutionNames().size() < usedBerries){
            throw new GameBadRequestException("Pokemon can't evolve this many times");
        }


        player.getDeck().evolveCard(usedBerries);


        player.setBerries(player.getBerries()-usedBerries);

    }








    //helper to return winners of gamestate
    private ArrayList<Player> getWinner(Game game){

        Integer maxValue=-1;
        ArrayList<Player> winner=new ArrayList<>();
        for(Player player:game.getPlayers()){
            Integer categoryValue=player.getDeck().peekCard().getCategories().get(game.getCategory());
            if(categoryValue>maxValue){
                maxValue=categoryValue;
                winner=new ArrayList<>();
                winner.add(player);
            }
            else if(categoryValue==maxValue){
                maxValue=categoryValue;
                winner.add(player);
            }
        }
        return winner;
    }
}
