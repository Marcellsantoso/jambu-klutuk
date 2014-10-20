/*******************************************************************************
 * Copyright 2013 Comcast Cable Communications Management, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.capsule.shellfies.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Objects.BeanImage;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.core.Section;
import com.comcast.freeflow.core.SectionedAdapter;
import com.iapps.libs.helpers.BaseUIHelper;

public class AdapterGrid implements SectionedAdapter {

	public static final String	TAG			= "DribbbleDataAdapter";

	private Context				context;
	private Section				section;

	private int[]				colors		= new int[] { 0xcc152431, 0xff264C58, 0xffF5C543,
											0xffE0952C, 0xff9A5325, 0xaaE0952C, 0xaa9A5325,
			0xaa152431,
			0xaa264C58, 0xaaF5C543, 0x44264C58, 0x44F5C543, 0x44152431 };

	private boolean				hideImages	= false;

	public AdapterGrid(Context context, ArrayList<BeanImage> alImages) {
		this.context = context;
		section = new Section();
		updateData(alImages);
	}

	public void updateData(ArrayList<BeanImage> alImages) {
		section.getData().clear();
		section.getData().addAll(alImages);
	}

	@Override
	public long getItemId(int section, int position) {
		return section * 1000 + position;
	}

	@Override
	public View getItemView(int sectionIndex, int position, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.pic_view, parent, false);
		}
		ImageView img = (ImageView) convertView.findViewById(R.id.pic);
		if (hideImages) {
			int idx = position % colors.length;
			img.setBackgroundColor(colors[idx]);

		} else {
			BeanImage bean = (BeanImage) section.getData().get(position);
			BaseUIHelper.loadImage(context, bean.getUrl(), img);
		}

		return convertView;
	}

	@Override
	public View getHeaderViewForSection(int section, View convertView,
			ViewGroup parent) {
		return null;
	}

	@Override
	public int getNumberOfSections() {
		if (section.getData().size() == 0)
			return 0;
		return 1;
	}

	@Override
	public Section getSection(int index) {
		return section;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getViewTypes() {
		return new Class[] { LinearLayout.class };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getViewType(FreeFlowItem proxy) {
		return LinearLayout.class;
	}

	@Override
	public boolean shouldDisplaySectionHeaders() {
		return false;
	}

}
