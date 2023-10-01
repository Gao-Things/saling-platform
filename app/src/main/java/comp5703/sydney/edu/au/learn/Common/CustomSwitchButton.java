package comp5703.sydney.edu.au.learn.Common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.kyleduo.switchbutton.SwitchButton;

public class CustomSwitchButton extends SwitchButton {

    private Paint textPaint;
    private float textSize = 20f;  // 设置您想要的文本大小
    private boolean textBold = true;  // 设置是否加粗

    public CustomSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(textBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String text = isChecked() ? "Open" : "Close";  // 依据当前开关状态设置文本
        float textWidth = textPaint.measureText(text);
        float x = (getWidth() - textWidth) / 2;
        float y = (getHeight() + textSize) / 2;
        canvas.drawText(text, x, y, textPaint);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
        textPaint.setTypeface(textBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        invalidate();
    }
}
