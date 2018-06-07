package com.lm.mutipletouch;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;

public class MultipleDragImageView extends AppCompatImageView{
    private SparseArray<PointF> points;
    private Matrix matrix=new Matrix();
    private PointF nowPointF;
    private RectF drawableRectF=new RectF();

    public MultipleDragImageView(Context context) {
        super(context);
    }

    public MultipleDragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);

        points=new SparseArray<>();
        drawableRectF.left=0;
        drawableRectF.top=0;
        drawableRectF.right=getDrawable().getIntrinsicWidth();
        drawableRectF.bottom=getDrawable().getIntrinsicHeight();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                points.put(event.getPointerId(event.getActionIndex()),
                        new PointF(event.getX(event.getActionIndex()),event.getY(event.getActionIndex())));
                nowPointF=points.get(0);
                break;
            case MotionEvent.ACTION_UP:
                points.clear();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //pointIndex会在复用
                points.put(event.getPointerId(event.getActionIndex()),new PointF(event.getX(event.getActionIndex()),
                        event.getY(event.getActionIndex())));
                nowPointF=points.get(event.getPointerId(0));
                logPoints();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //pointIndex在手指抬起时可能会更新
                points.remove(event.getPointerId(event.getActionIndex()));
                if(event.getPointerCount()>=2){
                    if(event.getActionIndex()==0){
                        nowPointF=points.get(event.getPointerId(1));
                    }
                }else{
                    nowPointF=null;
                }

                logPoints();
                break;
            case MotionEvent.ACTION_MOVE:
                //ACTION_MOVE的pointIndex始终为0
                matrix.postTranslate(event.getX()-nowPointF.x,event.getY()-nowPointF.y);

                drawableRectF.left=0;
                drawableRectF.top=0;
                drawableRectF.right=getDrawable().getIntrinsicWidth();
                drawableRectF.bottom=getDrawable().getIntrinsicHeight();

                //将matrix变换映射到drawableRectF
                matrix.mapRect(drawableRectF);

                setImageMatrix(matrix);

                int pointerId;
                PointF pointF;
                for(int i=0;i<event.getPointerCount();i++){
                    pointerId=event.getPointerId(i);
                    pointF=points.get(pointerId);
                    pointF.x=event.getX(i);
                    pointF.y=event.getY(i);
                }
                break;
        }
        return true;
    }


    private void logPoints(){
        for(int i=0;i<points.size();i++){
            System.out.println("key:"+points.keyAt(i)+",value:"+points.valueAt(i));
        }
    }
}
