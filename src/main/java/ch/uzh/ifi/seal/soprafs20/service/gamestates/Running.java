package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import org.hibernate.cfg.NotYetImplementedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Running implements GameState {
    @Override
    public void addPlayer(Game game, User user) {
        throw new GameBadRequestException("You can't add new players to a running game.");
    }
    @Override
    public void removePlayer(Game game, User user) {
        // TODO: Surrender.
        throw new NotYetImplementedException("Will be implemented in Sprint 3");
    }
    @Override
    public void startGame(Game game, Integer npc) {
        throw new GameBadRequestException("Game already started");
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

    @Override
    public void nextTurn(Game game) {
        distributeCards(game);

        if(isFinished(game)){
            game.setState(GameStateEnum.FINISHED);
            return;
        }


        setNewTurnPlayer(game);
        game.resetCategory();
        game.resetWinners();

        Long buffer=7L;
        //set start time for new turn
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        game.setStartTime(pattern.format(now.plusSeconds(buffer)));
    }

    //helper to return winners of gamestate
    public ArrayList<Player> getWinner(Game game){

        Integer maxValue=-1;
        ArrayList<Player> winner=new ArrayList<>();
        for(Player player:game.getPlayers()){
            if(!player.getDeck().isEmpty()){

                Integer categoryValue=player.getDeck().peekCard().getCategories().get(game.getCategory());
                if(categoryValue>maxValue){
                    maxValue=categoryValue;
                    winner=new ArrayList<>();
                    winner.add(player);
                }
                else if(categoryValue.equals(maxValue)){
                    maxValue=categoryValue;
                    winner.add(player);
                }
            }

        }
        return winner;
    }

    public void distributeCards(Game game){
        List<Player> winners=game.getWinners();

        if (winners.size()==1){
            Player winner=winners.get(0);
            for(Player player:game.getPlayers()){
                if (!player.getDeck().isEmpty()) {
                    Card temp = new Card(player.getDeck().removeCard());
                    winner.getDeck().addCard(temp);
                }
            }
        }
        else{
            for (Player player : game.getPlayers()){
                if (!player.getDeck().isEmpty()) {
                    Card temp = new Card(player.getDeck().removeCard());
                    player.getDeck().addCard(temp);
                }
            }
        }
    }
    public void setNewTurnPlayer(Game game){
        List<Player> winners=game.getWinners();

        if (winners.size()==1){
            game.setTurnPlayer(winners.get(0));
        }
        //TODO: different draw mechanics?
    }

    public boolean isFinished(Game game){
        for (Player player : game.getPlayers()){
            if(!player.getDeck().isEmpty() && !isWinner(game,player)){
                return false;
            }
        }
        return true;
    }

    public boolean isWinner(Game game, Player player){
        Long playerId= player.getId();
        List<Long> winnerIds = new ArrayList<Long>();
        for (Player winner : game.getWinners()){
            winnerIds.add(winner.getId());
        }
        return winnerIds.contains(playerId);
    }
}
