package pl.hackyeah.colorando;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity {

    private final int CELL_HEIGHT = 250;
    private int buttonCount = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TableLayout layout = new TableLayout(this);

        layout.setBackgroundColor(Color.parseColor("#00ffcc"));

        TableRow row1 = new TableRow(this);
        TableRow row2 = new TableRow(this);
        TableRow row3 = new TableRow(this);

        List<TextView> cells = getCells("#00ff00", "#006bb3", "#ff0000", "#ff9900",
                "#d9d9d9", "#000000", "#99ffcc", "#6600cc", "#ff66ff");

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        lp.leftMargin = 160;
        lp.topMargin = 350;


        layout.setLayoutParams(lp);
        row1.addView(cells.get(0));
        row1.addView(cells.get(1));
        row1.addView(cells.get(2));

        row2.addView(cells.get(3));
        row2.addView(cells.get(4));
        row2.addView(cells.get(5));

        row3.addView(cells.get(6));
        row3.addView(cells.get(7));
        row3.addView(cells.get(8));

        layout.addView(row1);
        layout.addView(row2);
        layout.addView(row3);


        Button tryItButton = getTryItButton();
        layout.addView(tryItButton);

        setContentView(layout);
    }

    private Button getTryItButton() {
        Button button = new Button(this);
        button.setId(R.id.tryItButtonId);
        button.setText(String.valueOf(buttonCount));
        button.setOnClickListener(v -> {
            buttonCount--;
            Button v1 = (Button) v;
            v1.setText(String.valueOf(buttonCount));
        });
        button.setGravity(Gravity.CENTER);
        return button;
    }

    private List<TextView> getCells(String... colors) {
        List<TextView> textViews = new ArrayList<>();
        for (String color : colors) {
            textViews.add(createCell(color));
        }
        return textViews;
    }

    private TextView createCell(String color) {
        TextView textView = new TextView(this);
        textView.setOnTouchListener(new ChoiceTouchListener());
        textView.setOnDragListener(new ChoiceDragListener());
        textView.setBackgroundColor(Color.parseColor(color));
        textView.setWidth(CELL_HEIGHT);
        textView.setHeight(CELL_HEIGHT);
        return textView;
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
