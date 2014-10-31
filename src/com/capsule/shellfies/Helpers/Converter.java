package com.capsule.shellfies.Helpers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.capsule.shellfies.Objects.BeanImage;

public class Converter {

	@SuppressWarnings("unused")
	private Context	context;

	private Converter(Context context) {
		this.context = context;
	}

	public static Converter getInstance(Context context) {
		Converter converter = new Converter(context);
		return converter;
	}

	public ArrayList<BeanImage> toBeanImageArr(JSONArray jArr) {
		ArrayList<BeanImage> alImages = new ArrayList<BeanImage>();
		try {
			for (int i = 0; i < jArr.length(); i++) {
				BeanImage o = this.toBeanImage(jArr.getJSONObject(i));

				if (o != null) {
					alImages.add(o);
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return alImages;
	}

	public BeanImage toBeanImage(JSONObject jObj) {
		try {
			BeanImage obj = new BeanImage();
			obj.setImageId(jObj.getString(Keys.ID));

			JSONArray jArr = jObj.getJSONArray(Keys.IMAGES);
			if (jArr.length() > 0){
				// Get optimum image for details image
				obj.setUrl(jArr.getJSONObject(0).getString(Keys.SOURCE));
				
				// Get smallest image for thumbnails
				obj.setUrlSmall(jArr.getJSONObject(jArr.length() - 1).getString(Keys.SOURCE));
			}

			return obj;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
