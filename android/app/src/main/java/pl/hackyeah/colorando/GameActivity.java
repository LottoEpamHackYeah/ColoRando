package pl.hackyeah.colorando;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity {

    private final int CELL_HEIGHT = 250;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TableLayout layout = new TableLayout(this);
        TableRow row1 = new TableRow(this);
        TableRow row2 = new TableRow(this);
        TableRow row3 = new TableRow(this);

        List<TextView> cells = getCells("#00ff00", "#006bb3", "#ff0000", "#ff9900",
                "#d9d9d9", "#000000", "#99ffcc", "#6600cc", "#ff66ff");

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
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

        setContentView(layout);
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
        textView.setOnClickListener(v -> {
         v.setBackgroundColor(Color.parseColor("#996600"));
        });
//        textView.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                System.out.println("elo");
//                return true;
//            }
//        });
        textView.setBackgroundColor(Color.parseColor(color));
        textView.setWidth(CELL_HEIGHT);
        textView.setHeight(CELL_HEIGHT);
        return textView;
    }

}
