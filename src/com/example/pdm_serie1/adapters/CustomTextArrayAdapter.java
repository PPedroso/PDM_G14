package com.example.pdm_serie1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pdm_serie1.model.IModelItem;

public class CustomTextArrayAdapter<T extends IModelItem> extends ArrayAdapter<T> {

	private T[] data;
	private LayoutInflater inflater;
	private int resource;
	
	public CustomTextArrayAdapter(Context context, int resource, T[] data) {
		super(context, resource, data);
		this.resource = resource;
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			view = inflater.inflate(resource, null);
		}
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setText(data[position].toListItemString());
		return view;
	}
}
