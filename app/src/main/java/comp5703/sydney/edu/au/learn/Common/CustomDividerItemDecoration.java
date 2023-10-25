package comp5703.sydney.edu.au.learn.Common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpacing;
    private final Paint paint;

    public CustomDividerItemDecoration(int spacing) {
        mSpacing = spacing;
        paint = new Paint();
        paint.setColor(Color.parseColor("#D6D6D6"));  // Set the desired color for the divider line
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // 设置左右边距
        outRect.left = mSpacing;
        outRect.right = mSpacing;

        // 如果想要设置上下边距，可以设置outRect.top和outRect.bottom

        // 为每个子项的底部添加分隔符，但跳过最后一个子项
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()) {
            outRect.bottom = 1; // 这里的1表示1像素的高度，可以根据需要调整
        }
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

        int left = parent.getPaddingLeft() + mSpacing;
        int right = parent.getWidth() - parent.getPaddingRight() - mSpacing;

        for (int i = 0; i < parent.getChildCount(); i++) { // -1是为了跳过最后一个子项
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + 1; // 1是分隔符的高度

            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}
