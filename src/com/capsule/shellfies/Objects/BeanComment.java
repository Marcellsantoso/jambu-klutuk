package com.capsule.shellfies.Objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanComment extends SimpleBean {
	private String	comment;

	public BeanComment(int id, String name, String comment) {
		super(id, name);
		setComment(comment);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
