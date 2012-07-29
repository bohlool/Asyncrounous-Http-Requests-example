package com.asynchronous.download;

import java.util.Collection;
import java.util.HashSet;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import opens.components.http.HttpObjectRequest;
import opens.components.http.core.HttpRequest;
import opens.components.http.core.RequestQueue;

/**
 * Multiple downloads asynchronous
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 * @since 2012
 */
public final class Download {
	
	/**
	 * This sub-class willbe handle the requests
	 * @author Leonardo Rossetto <leonardoxh@gmail.com>
	 * @since 2012
	 */
	private class BitmapRequest extends HttpObjectRequest<Bitmap> {
		
		/**
		 * The file associate with the request
		 */
		private String associateFile;
		
		/**
		 * Construct to associate the file to actual request
		 * @param file File to associate (URL)
		 */
		private BitmapRequest(String file) {
			this.associateFile = file;
		}
		
		/**
		 * Construct number 2
		 */
		private BitmapRequest() {
			
		}
		
		/**
		 * And here you can manipulate the inputstream and construct a Bitmap object
		 * @param response the response from url
		 */
		@Override
		protected void onHttpResponseReceived(HttpResponse response) throws Exception {
			 Log.d("Download","File Downloaded: " + this.associateFile);
			 //Bitmap bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
		}
		
		/**
		 * This is a new callback handle the errors
		 * @param message the error message or null
		 */
		@Override
		protected void onErrorCallBack(String message) {
			super.onErrorCallBack(message);
			Toast.makeText(context, "Error ocurred at file: " + this.associateFile + 
					" and the reason is: " + message, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * Context to startup activitys and handle the toast messages
	 */
	private Context context;
	
	/**
	 * Construct 
	 * @param ctx Valid context to startup the activity's and toast messages
	 */
	public Download(Context ctx) {
		this.context = ctx;
	}
	
	/**
	 * Enable the callback of all downloads finished
	 */
	public void enableCallBack() {
		RequestQueue.instance().setOnFinishAllDownloadsCallBack(this, "allDownloadsFinished");
	}
	
	/**
	 * Set the number of requests
	 * @param requests Number of requests
	 */
	public void setRequestsNumber(int requests) {
		RequestQueue.instance().setMaxConcurrent(requests);
	}
	
	/**
	 * This will be invoked at all downloads are finished
	 */
	public void allDownloadsFinished() {
		Toast.makeText(this.context, "All downloads done!", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Do the download from the url's
	 * @param _files Files to download
	 */
	public void download(String[] _files) {
		HashSet<BitmapRequest> files = new HashSet<BitmapRequest>();
		for(String file : _files) {
			files.add(new BitmapRequest(file));
		}
		Download.pushRequests(files);
	}
	
	/**
	 * Do the download from the url's
	 * @param _files Files to download
	 */
	public void download(Collection<String> _files) {
		HashSet<BitmapRequest> files = new HashSet<BitmapRequest>();
		for(String file : _files) {
			files.add(new BitmapRequest(file));
		}
		Download.pushRequests(files);
	}
	
	/**
	 * This class will be push the requests
	 * @param requests Files to be pushed
	 */
	private static void pushRequests(HashSet<BitmapRequest> requests) {
		for(BitmapRequest request : requests) {
			HttpRequest require = request.get(request.associateFile);
			RequestQueue.instance().push(require);
		}
	}
}