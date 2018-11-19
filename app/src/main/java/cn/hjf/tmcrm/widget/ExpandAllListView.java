package cn.hjf.tmcrm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ExpandAllListView extends ListView {

	public ExpandAllListView(Context context) {
		super(context);
	}

	public ExpandAllListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandAllListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}

}
