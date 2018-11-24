package pl.hackyeah.colorando.server.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {
    private final String gameId;
    private final Date createdOn;
    private final String location;
    private final int dimensionX;
    private final int dimensionY;
    private final String solution; // "123456789"
    private final String sharingId;

    public Game(String location, int dimensionX, int dimensionY) {
        this.createdOn = new Date();
        this.gameId = UUID.randomUUID().toString();
        this.sharingId = UUID.randomUUID().toString();
        this.location = location;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        List<String> solutionAsList = Arrays.asList(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }); 
        Collections.shuffle(solutionAsList);
        this.solution = solutionAsList.stream().collect(Collectors.joining());
    }
    
    public Game() {
        this.createdOn = new Date();
        this.gameId = UUID.randomUUID().toString();
        this.sharingId = "N/A";
        this.location = "ALL";
        this.dimensionX = 3;
        this.dimensionY = 3;
        List<String> solutionAsList = Arrays.asList(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }); 
        Collections.shuffle(solutionAsList);
        this.solution = solutionAsList.stream().collect(Collectors.joining());
    }

    public Game(String location) {
        this(location, 3, 3);
    }

    public String getGameId() {
        return gameId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getLocation() {
        return location;
    }

    public int getDimensionX() {
        return dimensionX;
    }

    public int getDimensionY() {
        return dimensionY;
    }

    public String getSolution() {
        return solution;
    }

    public String getSharingId() {
        return sharingId;
    }
}
