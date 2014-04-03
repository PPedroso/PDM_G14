package com.example.pdm_serie1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.pdm_serie1.model.IModelItem;

public abstract class CustomTextArrayAdapter<T extends IModelItem<T>> extends ArrayAdapter<T> {

	protected T[] data;
	private final LayoutInflater inflater;
	private final int resource;
	
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
			view = inflater.inflate(resource, parent, false);
		}
		changeText(position, view);
		return view;
	}
	
	protected abstract void changeText(int position, View view);
	
}
