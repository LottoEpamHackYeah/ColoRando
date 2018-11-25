package pl.hackyeah.colorando.server.game;

public class GameDTO {
    private String solution; // "123456789"
    private String sharingId;
    private boolean valid;

    private GameDTO() {
    }

    public static GameDTO invalidGame() {
        GameDTO dto = new GameDTO();
        dto.solution = "N/A";
        dto.sharingId = "N/A";
        dto.valid = false;
        return dto;
    }

    public static GameDTO fromGame(Game game) {
        GameDTO dto = new GameDTO();
        dto.solution = game.getSolution();
        dto.sharingId = game.getSharingId();
        dto.valid = true;
        return dto;
    }

    public String getSharingId() {
        return sharingId;
    }

    public String getSolution() {
        return solution;
    }

    public boolean isValid() {
        return valid;
    }
}
