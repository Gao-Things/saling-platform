package comp5703.sydney.edu.au.learn.Common;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 1000; // milliseconds
    private long lastClickTime = 0;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - lastClickTime;
        lastClickTime = currentClickTime;

        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;

        onSingleClick(v);
    }
}

