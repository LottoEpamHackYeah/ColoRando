package pl.hackyeah.colorando.server.game;

import java.util.Map;

public class GameService {
    Map<String, Game> gamesById;

    public synchronized String generateNew(String location, int dimensionX, int dimensionY) {
        Game newGame = new Game(location, dimensionX, dimensionY);
        gamesById.put(newGame.getUuid(), newGame);
        return newGame.getUuid();
    }

    public boolean validateGameStart(String uuid, String deviceLocation) {
        return validateLocation(gamesById.get(uuid).getLocation(), deviceLocation);
    }

    private boolean validateLocation(String location, String deviceLocation) {
        return location.equals(deviceLocation); // we may want to implement some margin allowance logic here
    }
}
