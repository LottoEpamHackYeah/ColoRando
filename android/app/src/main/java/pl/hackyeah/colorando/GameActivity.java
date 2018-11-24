package pl.hackyeah.colorando;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
