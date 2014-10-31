package com.capsule.shellfies.Fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Adapters.AdapterGrid;
import com.capsule.shellfies.Helpers.ArtbookLayout;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.CustomSwipeRefreshLayout;
import com.capsule.shellfies.Helpers.Helper;
import com.capsule.shellfies.Helpers.Keys;
import com.capsule.shellfies.Helpers.SwipeHorizontalTouchMotion;
import com.capsule.shellfies.Objects.BeanImage;
import com.comcast.freeflow.core.AbsLayoutContainer;
import com.comcast.freeflow.core.AbsLayoutContainer.OnItemClickListener;
import com.comcast.freeflow.core.FreeFlowContainer;
import com.comcast.freeflow.core.FreeFlowContainer.OnScrollListener;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.layouts.FreeFlowLayout;
import com.comcast.freeflow.layouts.VGridLayout;
import com.comcast.freeflow.layouts.VLayout;
import com.iapps.libs.helpers.HTTPAsyncTask;
import com.iapps.libs.objects.Response;
import com.iapps.libs.views.LoadingCompound;

public class FragmentGrid extends BaseFragmentShellfies implements OnRefreshListener {
	@InjectView(R.id.ffContainer)
	private FreeFlowContainer			freeFlowContainer;
	@InjectView(R.id.ld)
	private LoadingCompound				ld;
	@InjectView(R.id.swipe_container)
	private CustomSwipeRefreshLayout	swipeLayout;

	private final int					API_LOAD_IMAGES_TIMELINE	= 1;

	private ArrayList<BeanImage>		alImages					= new ArrayList<BeanImage>();
	private ArtbookLayout				custom;
	private VGridLayout					grid;
	private AdapterGrid					adapter;

	private int							mLimit						= 25;
	private int							mPage						= 1;
	private int							mLayoutIndex				= 0;
	private float						mScrollY					= 0;
	private boolean						isHideTop					= true,
																	isHideBot = true;

	private FreeFlowLayout[]			layouts;

	public FragmentGrid(boolean hideTopBar, boolean hideBotBar) {
		this.isHideTop = hideTopBar;
		this.isHideBot = hideBotBar;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_grid, container, false);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// TODO : remove this dummy id
		getPref().setFacebookAlbumId("1115556724456");
		initializeSwipeLayout();

		if (alImages.isEmpty())
			loadImages();
	}

	private void initializeSwipeLayout() {
		swipeLayout.setFreeFlow(freeFlowContainer);
		swipeLayout.setOnRefreshListener(this);
		int actionbarHeight = (int) getActivity().getTheme().obtainStyledAttributes(
				new int[] {
					android.R.attr.actionBarSize
				}).getDimension(0, 0);
		Log.d(Constants.LOG, "tinggi : " + Integer.toString(actionbarHeight));
		// swipeLayout.setTopMargin(actionbarHeight + 100);

		// Set four colors used in progress animation
		swipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_blue_light,
				android.R.color.holo_red_light);
	}

	public void loadImages() {
		DisplayMetrics display = getActivity().getResources().getDisplayMetrics();

		// Our new layout
		custom = new ArtbookLayout();

		// Grid Layout
		grid = new VGridLayout();
		VGridLayout.LayoutParams params = new VGridLayout.LayoutParams(display.widthPixels / 2,
				display.widthPixels / 2);
		grid.setLayoutParams(params);

		// Vertical Layout
		VLayout vlayout = new VLayout();
		VLayout.LayoutParams params2 = new VLayout.LayoutParams(display.widthPixels);
		vlayout.setLayoutParams(params2);

		// HLayout
		// HLayout hlayout = new HLayout();
		// hlayout.setLayoutParams(new
		// HLayout.LayoutParams(display.widthPixels));

		layouts = new FreeFlowLayout[] {
				custom, grid, vlayout
		};
		adapter = new AdapterGrid(getActivity(), alImages);

		freeFlowContainer.setLayout(layouts[mLayoutIndex]);
		freeFlowContainer.setAdapter(adapter);

		// Add listeners to this container
		freeFlowContainer.setOnItemClickListener(new ListenerClick());
		freeFlowContainer.addScrollListener(new ListenerScroll());
		freeFlowContainer.setOnTouchListener(new ListenerSwipe(getActivity()));

		callApi(API_LOAD_IMAGES_TIMELINE);
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

		this.mLayoutIndex = layoutIndex;
		freeFlowContainer.setLayout(layouts[layoutIndex]);
	}

	@Override
	public void onRefresh() {
		callApi(API_LOAD_IMAGES_TIMELINE);
	}

	// ================================================================================
	// AsyncTask Methods
	// ================================================================================
	public void callApi(int apiTag) {
		switch (apiTag) {
			case API_LOAD_IMAGES_TIMELINE: {
				GetImageAsync gImage = new GetImageAsync();
				// gImage.setUrl(api.getGridAll());
				String url = "https://graph.facebook.com/" + getPref().getFacebookAlbumId() +
						"/photos?access_token=" + getPref().getFacebookSession();
				Log.d(Constants.LOG, "url : " + url);
				// Session session = Session.getActiveSession();
				// Log.d(Constants.LOG, "access token : " + session.getAccessToken());
				gImage.setUrl(url);
				gImage.setGetParams(Keys.PAGE, mPage);
				gImage.setGetParams(Keys.PER_PAGE, mLimit);
				gImage.execute();
			}
		}
	}

	// ================================================================================
	// Listeners
	// ================================================================================
	public class ListenerScroll implements OnScrollListener {

		@Override
		public void onScroll(FreeFlowContainer container) {
			// Get current position
			float percentY = container.getScrollPercentY();

			// Decide whether to display or show
			displayActionBar(percentY, mScrollY, isHideTop, isHideBot);

			// Update value
			mScrollY = percentY;
		}
	}

	public class ListenerSwipe extends SwipeHorizontalTouchMotion {

		public ListenerSwipe(Context ctx) {
			super(ctx);
		}

		@Override
		public void onSwipeRight() {
			changeLayout(mLayoutIndex + 1);
		}

		@Override
		public void onSwipeLeft() {
			changeLayout(mLayoutIndex - 1);
		}

	}

	public class ListenerClick implements OnItemClickListener {

		@Override
		public void onItemClick(AbsLayoutContainer parent, FreeFlowItem proxy) {
			FragmentGrid.this.showImageDetails(alImages, proxy.itemIndex);
		}

	}

	// ================================================================================
	// AsyncTask Class
	// ================================================================================
	public class GetImageAsync extends HTTPAsyncTask {

		@Override
		protected void onPreExecute() {
			if (!ld.isShown()) {
				ld.showLoading();
			}
		}

		@Override
		protected void onPostExecute(Response response) {
			if (!Helper.isValidResponse(response, FragmentGrid.this))
				return;

			ld.hide();
			swipeLayout.setRefreshing(false);

			JSONObject json = Helper.handleResponse(response, ld);
			if (json != null) {
				try {
					if (mPage == 1) {
						alImages.clear();
					}

					alImages.addAll(converter.toBeanImageArr(json.getJSONArray(Keys.DATA)));

					adapter.updateData(alImages);
					freeFlowContainer.notifyDataSetChanged();
					freeFlowContainer.dataInvalidated(true);
				}
				catch (JSONException e) {
					ld.showError("", e.getMessage());
					e.printStackTrace();
				}
			} else {
				// Failed to parse JSON
				ld.showUnknownResponse();
			}
		}
	}
}
