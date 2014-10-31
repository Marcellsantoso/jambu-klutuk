package com.capsule.shellfies.Helpers;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.comcast.freeflow.core.FreeFlowContainer;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

	private FreeFlowContainer	freeFlow;

	public CustomSwipeRefreshLayout(Context context) {
		super(context);
	}

	public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean canChildScrollUp() {
		if (freeFlow != null) {
			// In order to scroll a StickyListHeadersListView up:
			// Firstly, the wrapped ListView must have at least one item
			return (freeFlow.getAdapter().getSection(0).getData().size() > 0) &&
					// And then, the first visible item must not be the first item
					((freeFlow.getScrollPercentY() > 0));
		} else {
			// Fall back to default implementation
			return super.canChildScrollUp();
		}
	}

	public FreeFlowContainer getFreeFlow() {
		return freeFlow;
	}

	public void setFreeFlow(FreeFlowContainer freeFlow) {
		this.freeFlow = freeFlow;
	}

}
