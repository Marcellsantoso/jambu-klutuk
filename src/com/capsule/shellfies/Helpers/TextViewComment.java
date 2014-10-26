package com.capsule.shellfies.Helpers;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.capsule.shellfies.R;
import com.capsule.shellfies.Objects.BeanComment;

public class TextViewComment extends TextView {

	private BeanComment	comment;

	public TextViewComment(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextViewComment(Context context, BeanComment beanComment) {
		super(context, null);
		setComment(beanComment);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		SpannableString span = null;
		if (comment != null) {
			span = new SpannableString(text);
			span.setSpan(new TextAppearanceSpan(getContext(), R.style.textName), 0,
					comment.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		if (span == null)
			super.setText(text, type);
		else
			super.setText(span, type);
	}

	public BeanComment getComment() {
		return comment;
	}

	public void setComment(BeanComment comment) {
		this.comment = comment;
		this.setText(comment.getName() + " " + comment.getComment());
	}

	public void setListenerSpan(ClickableSpan listenerSpan) {
		SpannableString span = new SpannableString(this.getText());
		span.setSpan(listenerSpan, 0, comment.getName().length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		this.setText(span);
		this.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
