package pl.hackyeah.colorando.server.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final int MAX_SHARES = 20;
    private Map<String, Game> gamesById = new HashMap<>();
    private Map<String, List<String>> usersBannedForSharedGame = new HashMap<>();
    private Map<String, Game> gamesBySharingId = new HashMap<>();

    public GameService() {
        generateNew("Krakow");
        generateNew("Wroclaw");
    }

    public synchronized String generateNew(String location) {
        Game newGame = new Game(location);
        String gameId = newGame.getGameId();
        gamesById.put(gameId, newGame);
        gamesBySharingId.put(newGame.getSharingId(), newGame);
        System.out.println(gameId + " " + location + " " + newGame.getSolution());
        return gameId;
    }

    public synchronized String generateNewGameWithNoSahring() {
        Game newGame = new Game();
        String gameId = newGame.getGameId();
        gamesById.put(gameId, newGame);
        System.out.println("From sharing: " + gameId + " " + newGame.getSolution());
        return gameId;
    }

    public boolean validateGameStart(String gameId, String deviceLocation) {
        Game game = gamesById.get(gameId);
        return game != null && validateLocation(game.getLocation(), deviceLocation);
    }

    private boolean validateLocation(String location, String deviceLocation) {
        return location.equals(deviceLocation); // we may want to implement some margin allowance logic here
    }

    public boolean validateGuess(String gameId, String guessAttempt) {
        Game game = gamesById.get(gameId);
        return game != null && guessAttempt.equals(game.getSolution());
    }

    public String getSharingId(String gameId) {
        Game game = gamesById.get(gameId);
        return game == null ? null : game.getSharingId();
    }

    public void banGameOrginatorForSharedGame(String gameId, String userId) {
        List<String> oneUserList = new ArrayList<>();
        oneUserList.add(userId);
        usersBannedForSharedGame.put(gamesById.get(gameId).getSharingId(), oneUserList);
    }

    public boolean isUserAllowedToPlaySharedGame(String sharingId, String userId) {
        List<String> list = usersBannedForSharedGame.get(sharingId);
        if (list.contains(userId) || limitReached(list) || sharingIsTooOld(gamesBySharingId.get(sharingId).getCreatedOn())) {
            return false;
        }
        list.add(userId);
        return true;
    }

    private boolean limitReached(List<String> list) {
        return list.size() >= MAX_SHARES;
    }

    private boolean sharingIsTooOld(Date date) {
        return new Date().getTime() - date.getTime() > 120 * 1000; //2 MINUTES
    }
}
