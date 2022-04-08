package com.liansheng.carworld.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 
 * ViewPage可能不需要滑动
 * 
 *
 */
public class CWViewPager extends ViewPager {

	private boolean scroolEnable = true;

	public CWViewPager(Context context){
		super(context);
	}

	public CWViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setScrollEnable(boolean scrollEnable){
		this.scroolEnable = scrollEnable;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(!scroolEnable){
			return false;
		}
	    try {
	        return super.onTouchEvent(arg0);
        } catch (Exception e) {
            return true;
        }
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(!scroolEnable){
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}
	
	

}
