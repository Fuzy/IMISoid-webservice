package test;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class Tests {
  private static final String URI = "http://localhost:8081/Imisoid_WS/events";

  @Ignore
  @Test
  public void httpDelete() {
    HttpResponse resp = null;
    try {
      HttpClient httpClient = new DefaultHttpClient();
      final HttpDelete delete = new HttpDelete(URI + "/AAAC4zAAMAAARwPABh");
      resp = httpClient.execute(delete);
    }
    catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Assert.assertEquals(HttpStatus.SC_OK, resp.getStatusLine().getStatusCode());

  }

  @Ignore
  @Test
  public void httpGet() {
    HttpResponse resp = null;
    try {
      HttpClient httpClient = new DefaultHttpClient();
      final HttpGet get = new HttpGet(URI + "/xxx?from=30.7.2004&to=30.7.2004");// "/0000001?from=30.7.2004&to=30.7.2004"
      resp = httpClient.execute(get);
    }
    catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Assert.assertEquals(HttpStatus.SC_NO_CONTENT, resp.getStatusLine().getStatusCode());
  }

  @Ignore
  @Test
  public void httpGetExisting() {
    HttpResponse resp = null;
    try {
      HttpClient httpClient = new DefaultHttpClient();
      final HttpGet get = new HttpGet(URI + "/0000001?from=30.7.2004&to=30.7.2004");// "/0000001?from=30.7.2004&to=30.7.2004"
      resp = httpClient.execute(get);
    }
    catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Assert.assertEquals(HttpStatus.SC_OK, resp.getStatusLine().getStatusCode());
  }

  @Ignore
  @Test
  public void httpPostAsForm() {
    HttpResponse resp = null;
    String event = "{\"cas\":\"28800000\",\"datum\":\"30.07.2004\",\"datum_zmeny\":\"30.07.2004\",\"druh\":\"P\",\"ic_obs\":\"KDA\",\"icp\":\"0000001\",\"kod_po\":\"00\",\"server_id\":\"AAAC4zAAMAAARwPABN\",\"typ\":\"O\"}";

    try {
      HttpClient httpClient = new DefaultHttpClient();
      final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("event", event));
      HttpEntity entity = new UrlEncodedFormEntity(params);
      final HttpPost post = new HttpPost(URI);
      post.addHeader(entity.getContentType());
      post.setEntity(entity);
      resp = httpClient.execute(post);
    }
    catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Assert.assertEquals(HttpStatus.SC_NO_CONTENT, resp.getStatusLine().getStatusCode());
  }

  @Ignore
  @Test
  public void httpPostAsJson() {
    HttpResponse resp = null;
    // String event =
    // "{\"cas\":\"28800000\",\"datum\":\"30.07.2004\",\"datum_zmeny\":\"30.07.2004\",\"druh\":\"P\",\"ic_obs\":\"KDA\",\"icp\":\"0000001\",\"kod_po\":\"00\",\"server_id\":\"AAAC4zAAMAAARwPABN\",\"typ\":\"O\"}";
    String event = "{\"cas\":\"28800000\",\"datum\":\"28800000\",\"datum_zmeny\":\"28800000\",\"druh\":\"P\",\"ic_obs\":\"KDA\",\"icp\":\"0000001\",\"kod_po\":\"00\",\"server_id\":null,\"typ\":\"O\"}";

    try {
      HttpClient httpClient = new DefaultHttpClient();
      final HttpPost post = new HttpPost(URI);
      StringEntity se = new StringEntity(event);
      post.setEntity(se);
      post.setHeader("Accept", "application/json");
      post.setHeader("Content-type", "application/json");
      resp = httpClient.execute(post);
    }
    catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Assert.assertEquals(HttpStatus.SC_NO_CONTENT, resp.getStatusLine().getStatusCode());
  }
}
