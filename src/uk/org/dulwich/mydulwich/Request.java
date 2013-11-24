package uk.org.dulwich.mydulwich;

import java.util.List;
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
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.widget.Toast;
import uk.org.dulwich.mydulwich.ntlm.NTLMSchemeFactory;

public class Request {
	private String url;
	private Activity parent;
	
	public Request(Activity activity, String url)
	{
		this.url = url;
		this.parent = activity;
	}
	
	public HttpEntity fetch() throws ClientProtocolException, IOException, BadLogin
	{
		HttpClient httpClient = new DefaultHttpClient();   
        ((AbstractHttpClient) httpClient).getAuthSchemes().register("ntlm", new NTLMSchemeFactory());

        NTCredentials creds;
        try {
        	creds = new NTCredentials(Account.getUsername(parent), Account.getPassword(parent), "", ApiList.domain);
        } catch(IllegalArgumentException e) { throw new BadLogin(); }

        ((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000); 

        HttpGet httpget = new HttpGet(url);
        httpget.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

        HttpResponse response;
        try {
        	response = httpClient.execute(httpget);
        } catch(Exception e) { 
        	Toast.makeText(parent, e.toString(), Toast.LENGTH_SHORT).show();
        	Toast.makeText(parent, "Bad/No Internet Connection!", Toast.LENGTH_LONG).show();
        	throw new BadLogin();
        }
        int rc = response.getStatusLine().getStatusCode();
        if (rc >= 300) throw new BadLogin("Response = "+rc);
        
        return response.getEntity();
	}

	public String get() throws ClientProtocolException, IOException, BadLogin
	{
        return EntityUtils.toString(fetch());
	}
	
	public List<Notice> notices() throws ClientProtocolException, IOException, BadLogin, IllegalStateException, XmlPullParserException
	{
		return (new NoticeRSSParser()).parse(fetch().getContent());
	}
}
