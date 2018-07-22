package view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.hy.maps.Detail;

public class Detail_View extends View {
    private LinearLayout l;
    public Detail_View(Context context) {
        super(context);
        l=new LinearLayout(context);
    }

    public Detail_View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        l=new LinearLayout(context);
    }

    public Detail_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        l=new LinearLayout(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
