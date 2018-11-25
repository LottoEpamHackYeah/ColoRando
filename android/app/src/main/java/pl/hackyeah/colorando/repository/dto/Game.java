package pl.hackyeah.colorando.repository.dto;

import java.util.Objects;

public class Game {

    private String id;
    private Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) &&
                Objects.equals(location, game.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", location=" + location +
                '}';
    }
}
