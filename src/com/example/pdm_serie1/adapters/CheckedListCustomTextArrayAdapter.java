package com.example.pdm_serie1.adapters;

import android.content.Context;
import android.view.View;
import android.widget.CheckedTextView;

import com.example.pdm_serie1.model.IModelItem;

public class CheckedListCustomTextArrayAdapter<T extends IModelItem> extends CustomTextArrayAdapter<IModelItem>{

	public CheckedListCustomTextArrayAdapter(Context context, int resource, IModelItem[] data) {
		super(context, resource, data);
	}

	@Override
	protected void changeText(int position, View view) {
		CheckedTextView textView = (CheckedTextView) view.findViewById(android.R.id.text1);
		textView.setText(data[position].toListItemString());		
	}

}
