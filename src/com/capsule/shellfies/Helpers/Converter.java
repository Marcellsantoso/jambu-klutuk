package com.capsule.shellfies.Helpers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.capsule.shellfies.Objects.BeanImage;

public class Converter {

	@SuppressWarnings("unused")
	private Context context;

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
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return alImages;
	}

	public BeanImage toBeanImage(JSONObject jObj) {
		try {
			BeanImage obj = new BeanImage();
			obj.setId(jObj.getInt(Constants.ID));
			obj.setTitle(jObj.getString(Constants.TITLE));
			obj.setDesc(jObj.getString(Constants.DESC));
			obj.setUrl(jObj.getString(Constants.IMAGE_URL));
			obj.setCommentCount(jObj.getInt(Constants.COMMENT_COUNT));
			obj.setCountLike(jObj.getInt(Constants.LIKE_COUNT));

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
