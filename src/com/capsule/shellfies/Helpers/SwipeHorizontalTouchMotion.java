package com.capsule.shellfies.Helpers;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class SwipeHorizontalTouchMotion implements OnTouchListener {
	private final GestureDetector gestureDetector;

	public SwipeHorizontalTouchMotion(Context ctx) {
		gestureDetector = new GestureDetector(ctx, new GestureListener());
	}

	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD
							&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight();
						} else {
							onSwipeLeft();
						}
					}
					result = true;
				} else if (Math.abs(diffY) > SWIPE_THRESHOLD
						&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffY > 0) {
						onSwipeBottom();
					} else {
						onSwipeTop();
					}
				}
				result = true;

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	public abstract void onSwipeRight();
	public abstract void onSwipeLeft();
	public abstract void onSwipeTop();
	public abstract void onSwipeBottom();	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
//
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN: {
//			startClickTime = Calendar.getInstance().getTimeInMillis();
//			Log.d(Constants.LOG, "container action down (" + Long.toString(startClickTime) + ")");
//			break;
//		}
//
//		case MotionEvent.ACTION_UP: {
//			long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
//			Log.d(Constants.LOG, "container action up (" + Long.toString(clickDuration) + ")");
//			if (clickDuration < Constants.MAX_CLICK_DURATION) {
//				v.performClick();
//				return true;
//			}else{
//				return gestureDetector.onTouchEvent(event);
//			}
//		}
//		}
//		
//		return false;
	}
}
