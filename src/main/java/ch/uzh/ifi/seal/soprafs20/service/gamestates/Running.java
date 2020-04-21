package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameForbiddenException;
import ch.uzh.ifi.seal.soprafs20.service.StatisticsHelper;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Running implements GameState {

    private final Logger log = LoggerFactory.getLogger(Running.class);

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

        log.debug(String.format("Select category was called, with category: %s", category));

        // Doesn't allow setting category more than once
        if (null != game.getCategory()) {
            throw new GameForbiddenException("Category already set.");
        }
        game.setCategory(category);
        game.setWinners(getWinner(game));

        log.debug("Category set.");
    }

    @Override
    public void useBerries(Game game, Integer usedBerries, Player player) {

        log.debug(String.format("Use Berry got called by %s, amount %s, game %s.", player.getUser().getUsername(), usedBerries, game.getToken()));

        if(validateBerry(usedBerries, player)){
            player.getDeck().evolveCard(usedBerries);
            player.setBerries(player.getBerries()-usedBerries);
        }
        else{
            throw new GameBadRequestException("Invalid Evolution");
        }

        log.debug("Berries set.");

    }

    @Override
    public void nextTurn(Game game) {

        log.debug("Next Turn got called.");

        distributeCards(game);

        if(isFinished(game)){
            game.setState(GameStateEnum.FINISHED);
            StatisticsHelper.doPostStatistics(game);
            log.debug("Game finished.");
            return;
        }


        setNewTurnPlayer(game);
        game.resetCategory();
        game.resetWinners();

        Long buffer=7000L;
        //set start time for new turn
        //DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //LocalDateTime now = LocalDateTime.now();
        game.setStartTime( String.valueOf(System.currentTimeMillis() + buffer) );
        // TODO: update game entity accordingly

        //if the turnPlayer is an npc he should chose a category
        if(game.getTurnPlayer() instanceof Npc){
            npcSelectCategory(game);
        }

        //all npc players decide if they should use berries here
        for (Player player : game.getPlayers()){
            if(player instanceof Npc){
                npcUseBerry(game, player);
            }
        }

        log.debug("Next Turn set.");

    }

    //helper to return winners of gamestate
    @Override
    public ArrayList<Player> getWinner(Game game){

        if(game.getCategory()==null){
            return null;
        }

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

    private void npcUseBerry(Game game, Player npc){
        //10% chance to use 2 berries if possible
        if(validateBerry(2,npc)){
            if(decideBerry(10)){
                this.useBerries(game, 2, npc);
            }
        }
        //30% chance to use 1 berry if possible
        else if(validateBerry(1,npc)){
            if(decideBerry(30)){
                this.useBerries(game, 1, npc);
            }
        }

    }
    private void npcSelectCategory(Game game){
        //choose random category
        this.selectCategory(game, randomEnum(Category.class));
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

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        Random random = new Random();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private static boolean decideBerry(Integer percentChance){
        Random r = new Random();
        int result= r.nextInt(100)+1;
        if (result<=percentChance){
            return true;
        }
        return false;
    }

    private boolean validateBerry(Integer amount, Player player){
        // does player have this many berries?
        if(player.getBerries()<amount){
            return false;
        }

        //does player have a top card?
        if(player.getDeck().isEmpty()){
            return false;
        }

        //is player allowed to use this many berries on card?
        if(player.getDeck().peekCard().getEvolutionNames().size() < amount){
            return false;
        }

        return true;
    }


}
