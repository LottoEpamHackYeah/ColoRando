package pl.hackyeah.colorando;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

public class Activity extends AppCompatActivity {

    protected <T extends Activity> void startActivity(Class<T> _class, Serializable results) {
        Intent i = new Intent(this, _class);
        i.putExtra("results", results);
        super.startActivity(i);
    }


}
