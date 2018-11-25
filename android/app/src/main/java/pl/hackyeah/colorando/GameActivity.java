package pl.hackyeah.colorando;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import pl.hackyeah.colorando.repository.dto.CodeResult;

public class GameActivity extends Activity {

    private static final String[] COLORS = {
            "#00ff00", "#006bb3", "#ff0000", "#ff9900",
            "#d9d9d9", "#000000", "#99ffcc", "#6600cc", "#ff66ff"
    };

    private static final boolean[] GAMES = { false, false, false, false, false, true };

    private GridLayout gameBoard;
    private TextView header;
    private Button share;
    private Button play;
    private int triesLeft;
    private int gamesLeft;
    private int credit;
    private static int gameNumber, count = 0;
    private CodeResult codeResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameBoard = findViewById(R.id.gameBoard);
        share = findViewById(R.id.share);
        play = findViewById(R.id.play);
        header = findViewById(R.id.header);

        final int childCount = gameBoard.getChildCount();
        View v;
        String color;

        for (int i = 0; i < childCount; i++) {
            v = gameBoard.getChildAt(i);
            color = COLORS[i];

            v.setOnTouchListener(new ChoiceTouchListener());
            v.setOnDragListener(new ChoiceDragListener());
            v.setBackgroundColor(Color.parseColor(color));
        }

        codeResult = (CodeResult) getIntent().getSerializableExtra("results");

        share.setOnClickListener(v1 -> shareIt());
        play.setOnClickListener(v2 -> play(childCount));
        triesLeft = 2;
        gamesLeft = GAMES.length - 1;
        credit = 12;
        gameNumber++;
        header.setText(String.format("Games Left: %s - Credit: %s PLN ", String.valueOf(gamesLeft), String.valueOf(credit)));
    }

    private void play(int childCount) {
        if((count + 1) % 3 != 0) {
            play.setText(String.format("Play (%s)", triesLeft));
            Toast.makeText(GameActivity.this, "Unfortunately no :(", Toast.LENGTH_SHORT).show();
        } else if (GAMES[count]) {
            for (int i = 0; i < childCount; i++) {
                View vv = gameBoard.getChildAt(i);
                ((TextView) vv).setText("OK");
            }

            play.setVisibility(View.GONE);
            share.setVisibility(View.VISIBLE);
            Toast.makeText(GameActivity.this, "GOOD NEWS TODAY \\o/", Toast.LENGTH_SHORT).show();
        } else if (triesLeft == 0) {
            play.setText("Ow no! :(");
            play.setEnabled(false);
            triesLeft = 2;
        }
        count++;
        triesLeft--;
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://www.lotto.pl/colorando/if-you-enjoy-our-app/this-URL-is-waiting-for-ColoRando:)";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello, I send you invitation to the ColoRando game!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private class ChoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            }
            return false;
        }
    }

    private class ChoiceDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    System.out.println("Action drag started");
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    System.out.println("Action drag ended");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    System.out.println("Action drag exited");
                    break;
                case DragEvent.ACTION_DROP:
                    System.out.println("Action drop");
                    TextView fromCell = (TextView) event.getLocalState();
                    Drawable targetCellDrawable = v.getBackground();
                    Drawable fromCellDrawable = fromCell.getBackground();
                    v.setBackground(fromCellDrawable);
                    fromCell.setBackground(targetCellDrawable);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    System.out.println("Action drag entered");
                    break;
            }
            return true;
        }
    }
}
