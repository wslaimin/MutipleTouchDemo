package com.lm.mutipletouch;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DragImageView extends AppCompatImageView{
    private Matrix matrix=new Matrix();
    private boolean canDrag;
    private PointF lastPoint=new PointF();
    private RectF mBitmapRectF=new RectF();
    public DragImageView(Context context) {
        super(context);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBitmapRectF.top=0;
        mBitmapRectF.left=0;
        mBitmapRectF.right=getDrawable().getIntrinsicWidth();
        mBitmapRectF.bottom=getDrawable().getIntrinsicHeight();

        setScaleType(ScaleType.MATRIX);
        setImageMatrix(matrix);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 判断按下位置是否包含在图片区域内
                if (mBitmapRectF.contains((int)event.getX(), (int)event.getY())){
                    canDrag = true;
                    lastPoint.set(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                canDrag = false;
            case MotionEvent.ACTION_MOVE:
                if (canDrag) {
                    // 移动图片
                    matrix.postTranslate(event.getX() - lastPoint.x, event.getY() - lastPoint.y);
                    // 更新上一次点位置
                    lastPoint.set(event.getX(), event.getY());

                    // 更新图片区域
                    mBitmapRectF.top=0;
                    mBitmapRectF.left=0;
                    mBitmapRectF.right=getDrawable().getIntrinsicWidth();
                    mBitmapRectF.bottom=getDrawable().getIntrinsicHeight();
                    matrix.mapRect(mBitmapRectF);

                    setImageMatrix(matrix);
                    invalidate();
                }
                break;
        }

        return true;
    }
}
