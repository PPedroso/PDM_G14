package com.example.pdm_serie1.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.pdm_serie1.model.IModelItem;

public class NormalListCustomTextArrayAdapter<T extends IModelItem<T>> extends CustomTextArrayAdapter<T>{

	public NormalListCustomTextArrayAdapter(Context context, int resource, T[] data) {
		super(context, resource, data);
	}

	@Override
	protected void changeText(int position, View view) {
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setText(data[position].toListItemString());
	}

}
