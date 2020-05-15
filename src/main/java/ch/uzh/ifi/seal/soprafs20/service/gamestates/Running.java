package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameForbiddenException;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.service.StatisticsHelper;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
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

        log.debug(String.format("Player %s is leaving the game.", user.getUsername()));

        // chooses random category for left turnplayer
        if (game.getTurnPlayer().getUser().getId().equals(user.getId()) && null == game.getCategory()) {
            this.selectCategory(game, randomEnum(Category.class));
        }

        List<Player> players = game.getPlayers();

        // Change to a new creator, if the creator leaves
        if (game.getCreator().getUser().getId().equals(user.getId())) {
            log.debug(String.format("Player %s is creator, reassigning to new.", user.getUsername()));

            // To prevent setting the same creator back
            long creatorID = game.getCreator().getUser().getId();

            game.resetCreator(); // Just to be able to throw an error

            for (Player player: players) {
                if (!(player instanceof Npc) &&
                        !(player.getUser().getId().equals(creatorID)) &&
                        !(player.getDeck().isEmpty())) { // Add so spectators do not get chosen as creator TODO test it
                    game.setCreator(player);
                    break;
                }
            }

            // Creator is null if no "real" player which is not spectating exist
            if (null == game.getCreator()) {
                log.debug("No new creator got set.");
            } else {
                log.debug(String.format("New creator is %s.", game.getCreator().getUser().getUsername()));
            }
        }

        log.warn("The leaving player gets removed from list, there is no redistribution routine in place atm.");

        for (Player player: players) {
            if (player.getUser().getId().equals(user.getId())) {
                players.remove(player);
                log.debug("User removed.");
                return;
            }
        }

        // TODO: Surrender routine.

    }
    @Override
    public void startGame(Game game, Integer npc, int deckSize, long buffer, int generation, GameRepository gameRepository) {
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
            // evolveCard evolves the card a the top, so this method checks the top card
            StatisticsHelper.encounter(player, player.getDeck().peekCard());
        }
        else{
            throw new GameBadRequestException("Invalid Evolution");
        }

        game.setWinners(getWinner(game));

        log.debug("Berries set.");

    }

    @Override
    public void nextTurn(Game game) {

        log.debug("Next Turn got called.");

        distributeCards(game);

        if(isFinished(game)){
            log.debug("Changing to finished state");
            game.setState(GameStateEnum.FINISHED);
            StatisticsHelper.doPostStatistics(game);
            log.debug("Game finished.");
            npcSelectEmote(game);
            return;
        }

        log.debug("Not finished yet");

        setNewTurnPlayer(game);
        log.debug(String.format("Turn Player changed to: %s",game.getTurnPlayer().getUser().getUsername()));
        game.resetCategory();
        log.debug("Category resetted");
        game.setWinners(new ArrayList<Player>());
        log.debug("Winners resetted");

        Long buffer=7000L;
        game.setStartTime( String.valueOf(System.currentTimeMillis() + buffer) );
        log.debug("New start Time set");

        //if the turnPlayer is an npc he should chose a category
        if(game.getTurnPlayer() instanceof Npc){
            log.debug("NPC is turnplayer and chooses category");
            npcSelectCategory(game);
            log.debug("NPC has chosen category");
        }


        //all npc players decide if they should use berries here
        for (Player player : game.getPlayers()){
            if(player instanceof Npc){
                log.debug("NPC tries to use berry");
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
                    // The card the winner gets is most likely new from him, so he has to encounter it
                    StatisticsHelper.encounter(winner, temp);

                    if (player.getUser().getId().equals(game.getCreator().getUser().getId()) && player.getDeck().isEmpty()) {
                        log.debug("Current creator is spectator, reassigning...");
                        for (Player player2: game.getPlayers()) {
                            if (!(player2 instanceof Npc) &&
                                    !(player2.getUser().getId().equals(game.getCreator().getUser().getId())) &&
                                    !(player2.getDeck().isEmpty())) { // Add so spectators do not get chosen as creator TODO test it
                                game.setCreator(player2);
                                break;
                            }
                        }
                        log.debug(String.format("Reassigned to %s.", game.getCreator().getUser().getUsername()));
                    }
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

        log.debug("Setting new turn-player.");

        if (winners.size()==1){
            Player winner = winners.get(0);

            log.debug("Just one winner.");

            // Safety-check so that turn-player is never null
            for (Player player: game.getPlayers()) {
                if (winner.getUser().getId().equals(player.getUser().getId())) {
                    log.debug(String.format("Setting %s as turn-player", winner.getUser().getUsername()));
                    game.setTurnPlayer(winner);
                    return;
                }
            }

            // If winner left game, assign a new turn-player
            for (Player player: game.getPlayers()) {
                if (!(player instanceof Npc)) {
                    log.debug(String.format("Winner left, setting %s as turn-player", player.getUser().getUsername()));
                    game.setTurnPlayer(player);
                    return;
                }
            }

        } else {

            log.debug("Multiple winners (which means draw).");

            Player turnPlayer = game.getTurnPlayer();

            for (Player player: game.getPlayers()) {
                if (turnPlayer.getUser().getId().equals(player.getUser().getId())) {
                    log.debug(String.format("The turn-player %s stays.", turnPlayer.getUser().getUsername()));
                    return;
                }
            }

            // Chooses other winner from array, who don't left
            for (Player winner: winners) {
                for (Player player: game.getPlayers()) {
                    if (winner.getUser().getId().equals(player.getUser().getId())) {
                        if (!(winner instanceof Npc)) {
                            log.debug(String.format("Turn-player left, setting to %s.", winner.getUser().getUsername()));
                            game.setTurnPlayer(winner);
                            return;
                        }
                    }
                }
            }

            // If ALL winner left game, assign a new turn-player
            for (Player player: game.getPlayers()) {
                if (!(player instanceof Npc)) {
                    log.debug(String.format("All winners left, setting turn-player to %s.", player.getUser().getUsername()));
                    game.setTurnPlayer(player);
                    return;
                }
            }

        }
        //TODO: different draw mechanics?
    }

    private void npcUseBerry(Game game, Player npc){
        //10% chance to use 2 berries if possible
        if(validateBerry(2,npc)){
            if(decideBerry(10)){
                this.useBerries(game, 2, npc);
                log.debug("NPC used 2 berries");
            }
        }
        //30% chance to use 1 berry if possible
        else if(validateBerry(1,npc)){
            if(decideBerry(30)){
                this.useBerries(game, 1, npc);
                log.debug("NPC used a berry");
            }
        }

    }
    private void npcSelectCategory(Game game){
        //choose random category
        this.selectCategory(game, randomEnum(Category.class));
    }

    private void npcSelectEmote(Game game) {

        Random random = new Random();

        for (Player player: game.getPlayers()) {
            if (player instanceof Npc) {
                player.setEmote(random.nextInt(5)+1);
            }
        }
    }

    public boolean isFinished(Game game){

        boolean onlyBot = true;
        boolean onlyOnePlayerHasCards = true;

        for (Player player : game.getPlayers()){
            if((!player.getDeck().isEmpty() && !isWinner(game,player)) || game.getWinners().size()>1){
                onlyOnePlayerHasCards = false;
            }
            if (!(player instanceof Npc) && !player.getDeck().isEmpty()) {
                onlyBot = false;
            }
        }

        return onlyOnePlayerHasCards || onlyBot;

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

    @Override
    public void putEmote(Player player, Integer emote){throw new GameBadRequestException("Can't put Emotes during running Game");}


}
