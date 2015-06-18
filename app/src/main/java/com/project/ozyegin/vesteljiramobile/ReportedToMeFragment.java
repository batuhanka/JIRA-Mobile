package com.project.ozyegin.vesteljiramobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ReportedToMeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView           = inflater.inflate(R.layout.fragment_reported, container, false);
        ExpandableListView elv  = (ExpandableListView) rootView.findViewById(R.id.mylist);
        elv.setAdapter(new SavedTabsListAdapter());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getReportedIssues();
        return rootView;
    }

    private void getReportedIssues() {

        String mUsername = DashboardActivity.getmUsername();
        String mPassword = DashboardActivity.getmPassword();

        String json = "";
        InputStream is;

        try{
            HttpClient client 			= new DefaultHttpClient();
            CookieStore cookieStore 	= new BasicCookieStore();
            HttpContext httpContext 	= new BasicHttpContext();
            HttpPost post 				= new HttpPost("http://10.108.95.25/jira/rest/auth/1/session");
            httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            post.setHeader("Content-type", "application/json");

            JSONObject obj = new JSONObject();
            obj.put("username", mUsername);
            obj.put("password", mPassword);

            post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
            HttpResponse response    = client.execute(post, httpContext);
            is                      = response.getEntity().getContent();

            try {
                BufferedReader reader   = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb        = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.get("session") != null){

                HttpGet get 			= new HttpGet("http://10.108.95.25/jira/rest/api/2/search?jql=assignee=batuhanka+and+status+in+(%22Open%22)");
                //HttpGet get 			= new HttpGet("http://10.108.95.25/jira/rest/api/2/project");

                get.setHeader("Content-type", "application/json");
                get.addHeader(response.getFirstHeader("Set-Cookie"));

                HttpResponse res 		= client.execute(get, httpContext);
                InputStream inputStream	= res.getEntity().getContent();
                BufferedReader reader 	= new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sb 		= new StringBuilder();
                String line 			= null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                String json2 = sb.toString();

                JSONObject jsonObject2  = new JSONObject(json2);
                JSONArray jsonArray     = jsonObject2.getJSONArray("issues");
                for(int i=0; i<jsonArray.length(); i++){
                    Log.e("BATU",jsonArray.getJSONObject(i).get("key").toString());

                }
                //Log.e("BATU", "inner result "+jsonArray.length());

            }
            else{
                Log.e("BATU", "Login Failed");
            }

        }catch(Exception ignored){	Log.e("BATU", ignored.toString()); }

    }

    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };

        private String[][] children = {
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Goldy", "Bubbles" }
        };

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return children[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return children[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {

            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ReportedToMeFragment.this.getActivity().getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ReportedToMeFragment.this.getActivity().getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }


}