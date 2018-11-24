package pl.hackyeah.colorando.server.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {
    private final String uuid;
    private final Date createdOn;
    private final String location;
    private final int dimensionX;
    private final int dimensionY;
    private final String solution; // "123456789"

    public Game(String location, int dimensionX, int dimensionY) {
        this.createdOn = new Date();
        this.uuid = UUID.randomUUID().toString();
        this.location = location;
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        List<String> solutionAsList = Arrays.asList(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }); // TOFIX:
                                                                                                                   // take
                                                                                                                   // dimensions
                                                                                                                   // into
                                                                                                                   // account
        Collections.shuffle(solutionAsList);
        this.solution = solutionAsList.stream().collect(Collectors.joining());
    }

    public Game(String location) {
        this(location, 3, 3);
    }

    public String getUuid() {
        return uuid;
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
}
