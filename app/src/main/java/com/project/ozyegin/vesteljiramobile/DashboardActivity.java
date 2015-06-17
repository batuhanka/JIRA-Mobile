package com.project.ozyegin.vesteljiramobile;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

import com.project.ozyegin.vesteljiramobile.adapter.TabsPagerAdapter;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;

public class DashboardActivity extends FragmentActivity implements ActionBar.TabListener {

	private static String mUsername;
	private static String mPassword;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Assigned", "Reported", "Search" };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_dashboard);

        mUsername 	= this.getIntent().getExtras().get("username").toString();
		mPassword 	= this.getIntent().getExtras().get("password").toString();

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	private void getAssignedIssues(String username, String password) {

		try{
		HttpClient client 			= new DefaultHttpClient();
		CookieStore cookieStore 	= new BasicCookieStore();
		HttpContext httpContext 	= new BasicHttpContext();
		HttpPost post 				= new HttpPost("http://10.108.95.25/jira/rest/auth/1/session");
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		post.setHeader("Content-type", "application/json");

		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("password", password);

		post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
		HttpResponse response    = client.execute(post, httpContext);



			String json2 				= "";
			HttpClient client2 			= new DefaultHttpClient();
			HttpContext httpContext2 	= new BasicHttpContext();
			HttpGet get 				= new HttpGet("http://10.108.95.25/jira/rest/api/2/search?jql=assignee=yigitk+and+status+in+(%22In+Progress%22)");
			get.setHeader("Content-type", "application/json");
			get.addHeader(response.getFirstHeader("Set-Cookie"));

			HttpResponse res 		= client2.execute(get, httpContext2);
			InputStream is 			= res.getEntity().getContent();
			BufferedReader reader 	= new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb 		= new StringBuilder();
			String line 			= null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json2 = sb.toString();
			JSONObject jsonObject = new JSONObject(json2);
			Log.e("BATU", "inner result "+jsonObject);

		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public static String getmUsername() {
		return mUsername;
	}

	public static String getmPassword() {
		return mPassword;
	}


}
