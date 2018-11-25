package pl.hackyeah.colorando.server.qrapi;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hackyeah.colorando.server.game.GameService;
import pl.hackyeah.colorando.server.user.UserService;

@RestController
class QRCodeController {

    @Autowired
    GameService gameService;
    
    @Autowired
    UserService userService;

    // Operation for Zabka :)
    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getQrCode(String locationId) {
        synchronized (locationId) {
            byte[] currentPngForLocation = gameService.getCachedPngForLocation(locationId);
            if (currentPngForLocation != null) {
                return currentPngForLocation;
            }
            String newGameId = gameService.generateNew(locationId);
            try {
                byte[] byteArray = buildPng(newGameId);
                gameService.cachePngForLocation(locationId, byteArray);
                return byteArray;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private byte[] buildPng(String newGameId) throws MalformedURLException, IOException {
        URL url = new URL("https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + newGameId);
        BufferedImage img = ImageIO.read(url);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bao);
        byte[] byteArray = bao.toByteArray();
        return byteArray;
    }

    @GetMapping("/qrcode")
    public Boolean postScannedQrCode(String gameId, String locationId) {
        synchronized (locationId) {
            userService.authenticateUser(/* some attributes */);
            boolean validationResult = gameService.validateGameStart(gameId, locationId);
            if (validationResult) {
                gameService.consumeCachedPngForLocation(locationId);
                userService.withdrawMoney(/* some attributes */);
                gameService.banGameOrginatorForSharedGame(gameId, "123" /* TODO: pass user id */);
            }
            return validationResult;
        }
    }

    @PostMapping("/play")
    public Boolean postUserGuess(String gameId, String guessAttempt) {
        userService.authenticateUser(/*some attributes*/);
        boolean validationResult = gameService.validateGuess(gameId, guessAttempt);
        if (validationResult) {
            userService.payWinningMoney(/*some attributes*/);
        }
        return validationResult;
    }

    @GetMapping("/sharingId")
    public String getSharingId(String gameId) {
        userService.authenticateUser(/*some attributes*/);
        return gameService.getSharingId(gameId);
    }

    @PostMapping("/sharedGame")
    public String getSharedGame(String sharingId) {
        userService.authenticateUser(/*some attributes*/);
        if (!gameService.isUserAllowedToPlaySharedGame(sharingId, "234" /*TODO: pass user id*/)) {
            return "Not allowed to play this shared game"; // TODO: report the user is not allowed to play this shared game
        }
        String newShareGameId = gameService.generateNewGameWithNoSahring();
        userService.withdrawMoney(/*some attributes of different user*/);
        return newShareGameId;
    }
}
