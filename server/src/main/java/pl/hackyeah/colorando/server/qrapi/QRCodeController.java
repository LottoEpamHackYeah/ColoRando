package pl.hackyeah.colorando.server.qrapi;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.hackyeah.colorando.server.game.GameDTO;
import pl.hackyeah.colorando.server.game.GameService;
import pl.hackyeah.colorando.server.user.UserService;

@RestController
class QRCodeController {

    Logger LOG = LogManager.getLogger(QRCodeController.class);

    @Autowired
    GameService gameService;

    @Autowired
    UserService userService;

    // Operation for Zabka :)
    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getQrCode(String locationId) {
        LOG.trace("getQrCode (/qrcode) is called with parameters locationId: {}", locationId);
        synchronized (locationId) {
            byte[] currentPngForLocation = gameService.getCachedPngForLocation(locationId);
            if (currentPngForLocation != null) {
                return currentPngForLocation;
            }
            String newGameId = gameService.generateNew(locationId);
            LOG.info("New game {} created, new QR code will be sent in response", newGameId);
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
    @Deprecated
    public Boolean postScannedQrCode(String gameId, String locationId) {
        LOG.info("postScannedQrCode (/qrcode) is called with parameters gameId: {}, locationId: {}", gameId, locationId);
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

    @GetMapping("/getGameMethadata")
    public GameDTO getGameMethadata(String gameId, String locationId) {
        LOG.info("getGameMethadata (/getGameMethadata) is called with parameters gameId: {}, locationId: {}", gameId, locationId);
        synchronized (locationId) {
            userService.authenticateUser(/* some attributes */);
            boolean validationResult = gameService.validateGameStart(gameId, locationId);
            if (validationResult) {
                gameService.consumeCachedPngForLocation(locationId);
                userService.withdrawMoney(/* some attributes */);
                gameService.banGameOrginatorForSharedGame(gameId, "MainUser" /* TODO: pass real user id */);
                return GameDTO.fromGame(gameService.getGameById(gameId));
            } else {
                LOG.info("Location is not valid for this game. Game can't be started.");
                return GameDTO.invalidGame();
            }
        }
    }

    @GetMapping("/sharedGame")
    public String getSharedGame(String sharingId, String userId) {
        LOG.info("getSharedGame (/sharedGame) is called with parameters sharingId: {}, userId: {}", sharingId, userId);
        if (userId == null) {
            userId = "UnknownUser";
        }
        userService.authenticateUser(/* some attributes */);
        if (!gameService.isUserAllowedToPlaySharedGame(sharingId, userId)) {
            LOG.info("User is not allowed to use this sharing reference. Sharing has either reached the limit or expired.");
            return "!!! Not allowed to play this shared game !!!";
        }
        String newShareGameId = gameService.generateNewGameWithNoSahring();
        userService.withdrawMoney(/* some attributes of different user */);
        return newShareGameId;
    }
}
