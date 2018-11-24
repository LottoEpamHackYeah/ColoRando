package pl.hackyeah.colorando.server.qrapi;

import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/qrcode")
    public String getQrCode(String location) {
        // TODO: return png
        return gameService.generateNew(location);
    }

    @PostMapping("/qrcode")
    public Boolean postScannedQrCode(String gameId, String location) {
        userService.authenticateUser(/*some attributes*/);
        boolean validationResult = gameService.validateGameStart(gameId, location);
        if (validationResult) {
            userService.withdrawMoney(/*some attributes*/);
            gameService.banGameOrginatorForSharedGame(gameId, "123" /*TODO: pass user id*/);
        }
        return validationResult;
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
        return newShareGameId;
    }
}
