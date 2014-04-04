package com.example.pdm_serie1.asynctaskrelated;

import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.exceptions.UnexpectedStatusCodeException;

public abstract class JsonHttpRequestAsyncTask<T, K, Z> extends AsyncTask<T, K, IAsyncTaskResult<Z>>{

	private final String searchItemForException;
	protected final Context ctx;
	
	public JsonHttpRequestAsyncTask(Context ctx, String searchItemForException) {
		this.searchItemForException = searchItemForException;
		this.ctx = ctx;
	}

	@Override
	protected IAsyncTaskResult<Z> doInBackground(T...params) {
		try {
			return actualBackgroundWork(params);
		} catch (JSONException e) {
			Log.e("JSON", 
				  String.format("The parsing of GET request for all %s isn't being handled propertly", searchItemForException), 
			      e);
			return new BasicAsyncTaskResult<Z>(e);
		} catch (MyHttpException e) {
			if(e instanceof UnexpectedStatusCodeException) {
				Log.e("HTTP", 
					  String.format("The GET request to obtain the %s list is"
							  	    + " returning % status instead of 200",
							  	    searchItemForException,
							  	    ((UnexpectedStatusCodeException)e).getRecievedStatusCode()),
					  e);
			} else {
				Log.w("HTTP",
				 	  String.format("An error occured provoked by external causes occured while requesting"
					  + " the list of %s", searchItemForException),
					  e);
			}
			return new BasicAsyncTaskResult<Z>(e);
		} 
	}
	
	protected abstract IAsyncTaskResult<Z> actualBackgroundWork(T...params) throws JSONException, MyHttpException;

	@Override
	protected void onPostExecute(IAsyncTaskResult<Z> result) {
		if(result.getError() != null) {
			Toast.makeText(ctx, "An unexpected error occured", Toast.LENGTH_SHORT).show();
			return;
		}
		actualPostExecuteWork(result);
	}
	
	protected abstract void actualPostExecuteWork(IAsyncTaskResult<Z> result);

}