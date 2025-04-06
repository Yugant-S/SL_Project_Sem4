package com.example.sl_project.stats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.expensetracker.R;

import java.util.ArrayList;
import java.util.List;

public class DonutChartView extends View {
    private List<Integer> amounts = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();
    private Paint paint;
    private RectF rect;
    private float startAngle = 0;

    public DonutChartView(Context context) {
        super(context);
        init();
    }

    public DonutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DonutChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        rect = new RectF();
    }

    public void setData(List<Integer> amounts, List<Integer> colors) {
        if (amounts.size() != colors.size())
            throw new IllegalArgumentException("Amounts and colors must have the same size");
        this.amounts = amounts;
        this.colors = colors;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (amounts.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2 * 0.8f;
        float centerX = width / 2;
        float centerY = height / 2;

        float innerRadius = radius * 0.6f;
        rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        int total = 0;
        for (int amount : amounts) {
            total += amount;
        }

        startAngle = 0;
        for (int i = 0; i < amounts.size(); i++) {
            paint.setColor(colors.get(i));
            float sweepAngle = 360f * amounts.get(i) / total;
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }

        paint.setColor(getResources().getColor(R.color.white));
        canvas.drawCircle(centerX, centerY, innerRadius, paint);
    }
}