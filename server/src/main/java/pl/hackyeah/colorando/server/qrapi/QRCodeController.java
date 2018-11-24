package pl.hackyeah.colorando.server.qrapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hackyeah.colorando.server.game.GameService;

@RestController
class QRCodeController {

    @Autowired
    GameService gameService;

    @GetMapping("/qrcode")
    public String getQrCode() {
        return "123456789";
    }

    @PostMapping("/qrcode")
    public Boolean postScannedQrCode(String uuid, String location) {
        return gameService.validateGameStart(uuid, location);
    }

    @PostMapping("/play")
    public Boolean postUserGuess(String uuid, String guessAttempt) {
        return true;
    }
}
