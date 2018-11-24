package pl.hackyeah.colorando.server.game;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {
    private Map<String, Game> gamesById = new HashMap<>();

    public GameService() {
        generateNew("Krakow");
        generateNew("Wroclaw");
    }

    public synchronized String generateNew(String location) {
        Game newGame = new Game(location);
        String uuid = newGame.getUuid();
        gamesById.put(uuid, newGame);
        System.out.println(uuid + " " + location + " " + newGame.getSolution());
        return uuid;
    }

    public boolean validateGameStart(String uuid, String deviceLocation) {
        Game game = gamesById.get(uuid);
        return game != null && validateLocation(game.getLocation(), deviceLocation);
    }

    private boolean validateLocation(String location, String deviceLocation) {
        return location.equals(deviceLocation); // we may want to implement some margin allowance logic here
    }

    public boolean validateGuess(String uuid, String guessAttempt) {
        Game game = gamesById.get(uuid);
        return game != null && guessAttempt.equals(game.getSolution());
    }
}
