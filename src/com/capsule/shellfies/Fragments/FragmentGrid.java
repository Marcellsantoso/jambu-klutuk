package com.capsule.shellfies.Fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Adapters.AdapterGrid;
import com.capsule.shellfies.Helpers.Api;
import com.capsule.shellfies.Helpers.ArtbookLayout;
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

	public static final int				API_LOAD_IMAGES_TIMELINE	= 1,
																	API_LOAD_IMAGES_PROFILE = 2;

	private ArrayList<BeanImage>		alImages					= new ArrayList<BeanImage>();
	private ArtbookLayout				custom;
	private VGridLayout					grid;
	private AdapterGrid					adapter;

	private int							mLimit						= 25;
	private int							mPage						= 1;
	private int							mLayoutIndex				= 0;
	private int							apiTag;
	private float						mScrollY					= 0;

	private FreeFlowLayout[]			layouts;

	public FragmentGrid() {}

	public FragmentGrid(int apiTag) {
		this.apiTag = apiTag;
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
		// getPref().setFacebookAlbumId("1115556724456");

		initializeSwipeLayout();
		loadImages();

		if (alImages.isEmpty() || getHome().isActionChanged()) {
			callApi();
		} else
			ld.hide();
	}

	private void initializeSwipeLayout() {
		swipeLayout.setFreeFlow(freeFlowContainer);
		swipeLayout.setOnRefreshListener(this);

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
		callApi();
	}

	// ================================================================================
	// AsyncTask Methods
	// ================================================================================
	public void callApi() {
		switch (apiTag) {
			case API_LOAD_IMAGES_TIMELINE:
				GetImageAsync gImage = new GetImageAsync();
				// gImage.setUrl(api.getGridAll());
				String urlTest = Api.FACEBOOK_BASE + getPref().getFacebookAlbumId() +
						"/photos?access_token=" + getPref().getFacebookSession();
				gImage.setUrl(urlTest);
				gImage.setGetParams(Keys.PAGE, mPage);
				gImage.setGetParams(Keys.PER_PAGE, mLimit);
				gImage.execute();
				break;

			case API_LOAD_IMAGES_PROFILE:
				GetImageAsync gProfile = new GetImageAsync();
				String url = Api.FACEBOOK_BASE + getPref().getFacebookAlbumId() +
						"/photos?access_token=" + getPref().getFacebookSession();
				gProfile.setUrl(url);
				gProfile.setGetParams(Keys.PAGE, mPage);
				gProfile.setGetParams(Keys.PER_PAGE, mLimit);
				gProfile.execute();
				break;
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
			displayActionBar(percentY, mScrollY);

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
					alImages.addAll(converter.toBeanImageArr(json.getJSONArray(Keys.DATA)));
					alImages.addAll(converter.toBeanImageArr(json.getJSONArray(Keys.DATA)));
					alImages.addAll(converter.toBeanImageArr(json.getJSONArray(Keys.DATA)));
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
