package pl.hackyeah.colorando;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;

public class MainActivity extends Activity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initControllers();
    }

    private void initComponents() {
        fab = findViewById(R.id.fab);
    }

    private void initControllers() {
        fab.setOnClickListener(view -> startActivity(GameActivity.class));
    }
}
