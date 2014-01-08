package org.secure.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class MessageSender {
	
	private final String HOST = "http://localhost/test/ws/get_sms.php";
	
	private static MessageSender instance = null;
	
	public static MessageSender newInstance()
	{
		if(instance == null)
			 instance = new MessageSender();
		return instance;
	}
	
	public int postMessage(String messageId,String sender,String rawMessage)
	{
		
		//private void sendPostRequest(String givenUsername, String givenPassword) {

		    class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

		        @Override
		        protected String doInBackground(String... params) {

		            String paramMessageId = params[0];
		            String paramSender = params[1];
		            String paramRawMessage = params[2];

		            System.out.println("*** doInBackground ** paramMessageId: " + paramMessageId + " paramSender: " + paramSender+ " paramRawMessage: "+paramRawMessage);

		            HttpClient httpClient = new DefaultHttpClient();

		            // In a POST request, we don't pass the values in the URL.
		            //Therefore we use only the web page URL as the parameter of the HttpPost argument
		            HttpPost httpPost = new HttpPost(HOST);

		            // Because we are not passing values over the URL, we should have a mechanism to pass the values that can be
		            //uniquely separate by the other end.
		            //To achieve that we use BasicNameValuePair             
		            //Things we need to pass with the POST request
		            BasicNameValuePair messageIdBasicNameValuePair = new BasicNameValuePair("msgid", paramMessageId);
		            BasicNameValuePair senderBasicNameValuePair = new BasicNameValuePair("sender", paramSender);
		            BasicNameValuePair rawMessageBasicNameValuePAir = new BasicNameValuePair("msg", paramRawMessage);

		            // We add the content that we want to pass with the POST request to as name-value pairs
		            //Now we put those sending details to an ArrayList with type safe of NameValuePair
		            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		            nameValuePairList.add(messageIdBasicNameValuePair);
		            nameValuePairList.add(senderBasicNameValuePair);
		            nameValuePairList.add(rawMessageBasicNameValuePAir);

		            try {
		                // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs. 
		                //This is typically useful while sending an HTTP POST request. 
		                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

		                // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
		                httpPost.setEntity(urlEncodedFormEntity);

		                try {
		                    // HttpResponse is an interface just like HttpPost.
		                    //Therefore we can't initialize them
		                    HttpResponse httpResponse = httpClient.execute(httpPost);

		                    // According to the JAVA API, InputStream constructor do nothing. 
		                    //So we can't initialize InputStream although it is not an interface
		                    InputStream inputStream = httpResponse.getEntity().getContent();

		                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

		                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		                    StringBuilder stringBuilder = new StringBuilder();

		                    String bufferedStrChunk = null;

		                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
		                        stringBuilder.append(bufferedStrChunk);
		                    }

		                    return stringBuilder.toString();

		                } catch (ClientProtocolException cpe) {
		                    System.out.println("First Exception caz of HttpResponese :" + cpe);
		                    cpe.printStackTrace();
		                } catch (IOException ioe) {
		                    System.out.println("Second Exception caz of HttpResponse :" + ioe);
		                    ioe.printStackTrace();
		                }

		            } catch (UnsupportedEncodingException uee) {
		                System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
		                uee.printStackTrace();
		            }

		            return null;
		        }

		        @Override
		        protected void onPostExecute(String result) {
		            super.onPostExecute(result);

		            if(result.equals("working")){
		            	System.out.println("HTTP POST is working...");
		            	// Toast.makeText(getApplicationContext(), "HTTP POST is working...", Toast.LENGTH_LONG).show();
		            }else{
		            	System.out.println("Invalid POST...");
		               // Toast.makeText(getApplicationContext(), "Invalid POST req...", Toast.LENGTH_LONG).show();
		            }
		        }           
		    }

		    SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		    sendPostReqAsyncTask.execute(messageId,sender,rawMessage);     
		    
		
	
			return 0;
	}

}
