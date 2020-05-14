package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;

import java.util.ArrayList;

public interface GameState {
    public void addPlayer(Game game, User user);
    public void putEmote(Player player, Integer emote);
    public void removePlayer(Game game, User user);
    public void selectCategory(Game game, Category category);
    public void useBerries(Game game, Integer usedBerries, Player player);
    public void nextTurn(Game game);
    public void startGame(Game game, Integer npc, int deckSize, long buffer, int generation, GameRepository gameRepository);
    public ArrayList<Player> getWinner(Game game);
}
