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

    @GetMapping("/qrcode")
    public String getQrCode(String location) {
        return gameService.generateNew(location);
    }

    @PostMapping("/qrcode")
    public Boolean postScannedQrCode(String uuid, String location) {
        userService.authenticateUser(/*some attributes*/);
        boolean validationResult = gameService.validateGameStart(uuid, location);
        if (validationResult) {
            userService.withdrawMoney(/*some attributes*/);
        }
        return validationResult;
    }

    @PostMapping("/play")
    public Boolean postUserGuess(String uuid, String guessAttempt) {
        userService.authenticateUser(/*some attributes*/);
        boolean validationResult = gameService.validateGuess(uuid, guessAttempt);
        if (validationResult) {
            userService.payWinningMoney(/*some attributes*/);
        }
        return validationResult;
    }
}
