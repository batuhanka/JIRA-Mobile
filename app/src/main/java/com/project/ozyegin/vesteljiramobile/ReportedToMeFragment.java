package com.project.ozyegin.vesteljiramobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.project.ozyegin.vesteljiramobile.model.IssueModel;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class ReportedToMeFragment extends Fragment {

    public static List<IssueModel> results;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View rootView                           = inflater.inflate(R.layout.fragment_reported, container, false);
        ExpandableListView elv                  = (ExpandableListView) rootView.findViewById(R.id.mylist);
        HashMap<String, List<String>> results   = getReportedIssues();
        List<String> headers                    = new ArrayList<String>();
        for(String str : results.keySet()){     headers.add(str);   }
        elv.setAdapter(new SavedTabsListAdapter(getActivity().getApplicationContext(), headers, results ));
        return rootView;
    }

    private HashMap<String, List<String>> getReportedIssues() {

        String mUsername                        = DashboardActivity.getmUsername();
        String mPassword                        = DashboardActivity.getmPassword();
        HashMap<String, List<String>> issues    = new HashMap<String, List<String>>();

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

                String reportedToMeStr  = "http://10.108.95.25/jira/rest/api/2/search?jql=reporter="+mUsername+"+and+status+in+(%22Open%22%2C%22In%20Progress%22)";
                HttpGet get 			= new HttpGet(reportedToMeStr);

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
                    String key      = jsonArray.getJSONObject(i).get("key").toString();
                    //String id       = jsonArray.getJSONObject(i).get("id").toString();
                    String priority = jsonArray.getJSONObject(i).getJSONObject("fields").getJSONObject("priority").get("name").toString();

                    if(issues.keySet().contains(priority)){
                        issues.get(priority).add(key);
                    }else{
                        List<String> templist = new ArrayList<String>();
                        templist.add(key);
                        issues.put(priority, templist);
                    }

                }

                return issues;
            }
            else{
                Log.e("BATU", "Login Failed");
            }

        }catch(Exception ignored){	Log.e("BATU", ignored.toString()); }
        return null;
    }

    public class SavedTabsListAdapter extends BaseExpandableListAdapter {


        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public SavedTabsListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }


}