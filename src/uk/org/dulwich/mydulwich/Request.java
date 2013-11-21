package uk.org.dulwich.mydulwich;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.util.Log;
import uk.org.dulwich.mydulwich.ntlm.NTLMSchemeFactory;

public class Request {
	private String url;
	private Activity parent;
	
	public Request(Activity activity, String url)
	{
		this.url = url;
		this.parent = activity;
	}
	
	public String get() throws ClientProtocolException, IOException, BadLogin
	{
    	Log.v(this.getClass().getName(), "one");
		HttpClient httpClient = new DefaultHttpClient();        
        ((AbstractHttpClient) httpClient).getAuthSchemes().register("ntlm",new NTLMSchemeFactory());

    	Log.v(this.getClass().getName(), "two");
        NTCredentials creds;
        try {
        	creds = new NTCredentials(Account.getUsername(parent), Account.getPassword(parent), "", ApiList.domain);
        } catch(IllegalArgumentException e) { throw new BadLogin(); }

    	Log.v(this.getClass().getName(), "three");
        ((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000); 

    	Log.v(this.getClass().getName(), "four");
        HttpGet httpget = new HttpGet(url);
        httpget.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

    	Log.v(this.getClass().getName(), "five");
        HttpResponse response = httpClient.execute(httpget);
        int rc = response.getStatusLine().getStatusCode();
        if (rc >= 300) throw new BadLogin("Response = "+rc);
        

    	Log.v(this.getClass().getName(), "six");
        HttpEntity resEntity = response.getEntity();
        return EntityUtils.toString(resEntity);
	}
}