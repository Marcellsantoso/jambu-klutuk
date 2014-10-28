package com.capsule.shellfies.Fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Adapters.AdapterGrid;
import com.capsule.shellfies.Helpers.ArtbookLayout;
import com.capsule.shellfies.Helpers.Constants;
import com.capsule.shellfies.Helpers.Helper;
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

public class FragmentGrid extends BaseFragmentShellfies {
	@InjectView(R.id.ffContainer)
	private FreeFlowContainer		container;
	@InjectView(R.id.ld)
	private LoadingCompound			ld;

	private final int				API_LOAD_IMAGES_TIMELINE	= 1;

	private ArrayList<BeanImage>	alImages					= new ArrayList<BeanImage>();
	private ArtbookLayout			custom;
	private VGridLayout				grid;
	private AdapterGrid				adapter;
	private MenuItem				menu;

	private int						mLimit						= 25;
	private int						mPage						= 1;
	private int						mLayoutIndex				= 0;
	private float					mScrollY					= 0;
	private boolean					isRefreshing,
									isHideTop = true,
									isHideBot = true;

	private FreeFlowLayout[]		layouts;

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
		setHasOptionsMenu(true);

		if (alImages.isEmpty())
			loadImages();
		// getHome().getSupportActionBar().startActionMode(new ListenerMenu());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case R.id.menu_refresh:
				menu = item;

				// getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
				isRefreshing = true;

				// Reset page counter, because user refresh the whole page
				mPage = 1;

				loadImages();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void setMenuIcon() {
		if (menu != null) {
			if (isRefreshing) {
				menu.setVisible(false);
			} else {
				menu.setVisible(true);
			}
		}
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

		container.setLayout(layouts[mLayoutIndex]);
		container.setAdapter(adapter);

		// Add listeners to this container
		container.setOnItemClickListener(new ListenerClick());
		container.addScrollListener(new ListenerScroll());
		container.setOnTouchListener(new ListenerSwipe(getActivity()));

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
		container.setLayout(layouts[layoutIndex]);
	}

	// ================================================================================
	// Menu functions
	// ================================================================================
	public class ListenerMenu implements ActionMode.Callback {

		@Override
		public boolean onActionItemClicked(ActionMode arg0, MenuItem item) {
			int itemId = item.getItemId();
			switch (itemId) {
				case R.id.menu_refresh:
					menu = item;

					// getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
					isRefreshing = true;

					// Reset page counter, because user refresh the whole page
					mPage = 1;

					loadImages();
					return true;

				default:
					return false;
			}
		}

		@Override
		public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
			// Inflate our menu from a resource file
			arg0.getMenuInflater().inflate(R.menu.menu_refresh, arg1);

			// Return true so that the action mode is shown
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {

		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			return false;
		}

	}

	// ================================================================================
	// AsyncTask Methods
	// ================================================================================
	public void callApi(int apiTag) {
		switch (apiTag) {
			case API_LOAD_IMAGES_TIMELINE: {
				GetImageAsync gImage = new GetImageAsync();
				gImage.setUrl(api.getGridAll());
				gImage.setGetParams(Constants.PAGE, mPage);
				gImage.setGetParams(Constants.PER_PAGE, mLimit);
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

			setMenuIcon();
		}

		@Override
		protected void onPostExecute(Response response) {
			if (!Helper.isValidResponse(response, FragmentGrid.this))
				return;

			ld.hide();

			isRefreshing = false;
			// getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
			setMenuIcon();

			JSONObject json = Helper.handleResponse(response, ld);
			if (json != null) {
				try {
					if (mPage == 1) {
						alImages.clear();
					}

					alImages.addAll(converter.toBeanImageArr(json.getJSONArray(Constants.SHOTS)));

					adapter.updateData(alImages);
					container.notifyDataSetChanged();
					container.dataInvalidated(true);
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
