package com.capsule.shellfies.Objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanImage extends SimpleBean {

	String	imageId, url, urlSmall;

	public BeanImage() {
		super(0, "");
	}

	public BeanImage(int id, String name) {
		super(id, name);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlSmall() {
		return urlSmall;
	}

	public void setUrlSmall(String urlSmall) {
		this.urlSmall = urlSmall;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}
