package pl.hackyeah.colorando;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends Activity {

    private static final String[] COLORS = {
            "#00ff00", "#006bb3", "#ff0000", "#ff9900",
            "#d9d9d9", "#000000", "#99ffcc", "#6600cc", "#ff66ff"
    };

    private GridLayout gameBoard;
    private Button share;
    private Button play;
    private int triesLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameBoard = findViewById(R.id.gameBoard);
        share = findViewById(R.id.share);
        play = findViewById(R.id.play);

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
        share.setOnClickListener(v1 -> shareIt());
        play.setOnClickListener(v2 -> {
            if(triesLeft > 1) {
                triesLeft--;
                play.setText("Play (" + String.valueOf(triesLeft) + ")");
            } else {
                play.setVisibility(View.GONE);
                share.setVisibility(View.VISIBLE);
            }
        });
        triesLeft = 3;
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://www.lotto.pl/colorando/if-you-enjoy-our-app/you-know-what-to-do:)";
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

    @Override
    protected void onResume() {
        super.onResume();

    }
}
