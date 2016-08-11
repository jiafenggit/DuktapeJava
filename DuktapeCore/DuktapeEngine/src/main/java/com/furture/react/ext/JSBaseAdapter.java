package com.furture.react.ext;

import com.furture.react.JSRef;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * BaseAdapter 抽象类的实现。通过映射到具体的实例实现在JavaScript使用抽象类的功能。
 * */
public class JSBaseAdapter extends BaseAdapter{

	protected JSRef jsRef;

	public JSBaseAdapter(JSRef jsRef) {
		this.jsRef = jsRef;
	}


	@Override
	public int getCount() {
		Object count = jsRef.getEngine().call(jsRef, "getCount");
		if (count == null) {
			return 0;
		}
		return ((Number) count).intValue();
	}

	@Override
	public Object getItem(int position) {
		return jsRef.getEngine().call(jsRef, "getItem",  position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return (View) jsRef.getEngine().call(jsRef, "getView",  position, convertView, parent);
	}


	@Override
	public int getViewTypeCount() {
		Object count = jsRef.getEngine().call(jsRef, "getViewTypeCount");
		if (count == null) {
			return 1;
		}
		return ((Number) count).intValue();
	}

	@Override
	public int getItemViewType(int position) {
		Object count = jsRef.getEngine().call(jsRef, "getItemViewType", position);
		if (count == null) {
			return 0;
		}
		return ((Number) count).intValue();
	}
}
