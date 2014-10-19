package com.capsule.shellfies.Fragments;

import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Helpers.ArtbokFetch;
import com.capsule.shellfies.Helpers.ArtbookAdapter;
import com.capsule.shellfies.Helpers.ArtbookFeed;
import com.capsule.shellfies.Helpers.ArtbookLayout;
import com.capsule.shellfies.Helpers.SwipeHorizontalTouchMotion;
import com.comcast.freeflow.core.AbsLayoutContainer;
import com.comcast.freeflow.core.AbsLayoutContainer.OnItemClickListener;
import com.comcast.freeflow.core.FreeFlowContainer;
import com.comcast.freeflow.core.FreeFlowContainer.OnScrollListener;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.layouts.FreeFlowLayout;
import com.comcast.freeflow.layouts.VGridLayout;
import com.comcast.freeflow.layouts.VLayout;

public class FragmentGrid extends BaseFragmentShellfies {
	@InjectView(R.id.ffContainer)
	private FreeFlowContainer container;

	private ArtbookLayout custom;
	private VGridLayout grid;
	private ArtbokFetch fetch;
	private ArtbookAdapter adapter;

	private int itemsPerPage = 25;
	private int pageIndex = 1;
	private int currLayoutIndex = 0;
	private float scrollY = 0;

	private FreeFlowLayout[] layouts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_grid, container, false);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		DisplayMetrics display = getActivity().getResources()
				.getDisplayMetrics();

		// Our new layout
		custom = new ArtbookLayout();

		// Grid Layout
		grid = new VGridLayout();
		VGridLayout.LayoutParams params = new VGridLayout.LayoutParams(
				display.widthPixels / 2, display.widthPixels / 2);
		grid.setLayoutParams(params);

		// Vertical Layout
		VLayout vlayout = new VLayout();
		VLayout.LayoutParams params2 = new VLayout.LayoutParams(
				display.widthPixels);
		vlayout.setLayoutParams(params2);

		// HLayout
		// HLayout hlayout = new HLayout();
		// hlayout.setLayoutParams(new
		// HLayout.LayoutParams(display.widthPixels));

		layouts = new FreeFlowLayout[] { custom, grid, vlayout };

		adapter = new ArtbookAdapter(getActivity());

		container.setLayout(layouts[currLayoutIndex]);
		container.setAdapter(adapter);

		fetch = new ArtbokFetch();

		fetch.load(this, itemsPerPage, pageIndex);

	}

	@SuppressLint("ClickableViewAccessibility")
	@SuppressWarnings("deprecation")
	@Override
	public void onDataLoaded(ArtbookFeed feed) {
		adapter.update(feed);
		container.dataInvalidated();

		// Add listeners to this container
		container.setOnItemClickListener(ListenerItemClick);
		container.addScrollListener(ListenerScroll);
		container.setOnTouchListener(ListenerSwipe);
	}

	/**
	 * Change layout display
	 * 
	 * @param layoutIndex
	 */
	public void changeLayout(int layoutIndex) {
		if (layoutIndex == layouts.length) {
			layoutIndex = 0;
		}

		if (layoutIndex < 0) {
			layoutIndex = layouts.length - 1;
		}

		this.currLayoutIndex = layoutIndex;
		container.setLayout(layouts[layoutIndex]);
	}

	// ================================================================================
	// Listeners
	// ================================================================================
	public OnScrollListener ListenerScroll = new OnScrollListener() {

		@Override
		public void onScroll(FreeFlowContainer container) {
			// Get current position
			float percentY = container.getScrollPercentY();

			// Decide whether to display or show
			displayActionBar(percentY, scrollY);
			
			// Update value
			scrollY = percentY;
		}
	};

	public OnTouchListener ListenerSwipe = new SwipeHorizontalTouchMotion(
			getActivity()) {

		@Override
		public void onSwipeRight() {
			changeLayout(currLayoutIndex + 1);
		}

		@Override
		public void onSwipeLeft() {
			changeLayout(currLayoutIndex - 1);
		}
	};

	public OnItemClickListener ListenerItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AbsLayoutContainer parent, FreeFlowItem proxy) {
			// Empty function for now
		}
	};
}
