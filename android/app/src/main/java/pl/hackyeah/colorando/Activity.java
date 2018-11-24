package pl.hackyeah.colorando;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Activity extends AppCompatActivity {

    protected <T extends Activity> void startActivity(Class<T> _class) {
        super.startActivity(new Intent(this, _class));
    }
}
