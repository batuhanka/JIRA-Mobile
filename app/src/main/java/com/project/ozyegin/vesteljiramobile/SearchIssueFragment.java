package com.project.ozyegin.vesteljiramobile;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchIssueFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView 				= inflater.inflate(R.layout.fragment_searched, container, false);
		final Context context				= rootView.getContext();

		/*LinearLayout mainLayout		= (LinearLayout) rootView.findViewById(R.id.mainlayout);

		LinearLayout projectLayout 	= new LinearLayout(context);
		projectLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(	LinearLayout.LayoutParams.FILL_PARENT,
																				LinearLayout.LayoutParams.WRAP_CONTENT);
		projectLayout.setLayoutParams(layoutParams);

		TextView projectTextView					= new TextView(context);
		LinearLayout.LayoutParams textItemParams 	= new LinearLayout.LayoutParams(	LinearLayout.LayoutParams.FILL_PARENT,
																						LinearLayout.LayoutParams.FILL_PARENT);
		textItemParams.weight = new Float(0.75);
		projectLayout.setLayoutParams(textItemParams);
		projectTextView.setText("Project");
		projectLayout.addView(projectTextView);

		Spinner projectListView 					= new Spinner(context);
		LinearLayout.LayoutParams selectItemParams 	= new LinearLayout.LayoutParams(	LinearLayout.LayoutParams.MATCH_PARENT,
																						LinearLayout.LayoutParams.FILL_PARENT);
		selectItemParams.weight = new Float(0.25);
		ArrayList<String> projectItems				= new ArrayList<String>();
		projectItems.add("NEW1");
		projectItems.add("NEW2");
		projectItems.add("NEW3");
		projectItems.add("NEW4");

		ArrayAdapter<String> projectListAdapter		= new ArrayAdapter<String>(	context,
																				android.R.layout.simple_list_item_1,
																				projectItems);
		projectListView.setAdapter(projectListAdapter);
		projectLayout.addView(projectListView);

		mainLayout.addView(projectLayout);*/

		ArrayList<String> projectItems				= new ArrayList<String>();
		projectItems.add("PROJECT1");
		projectItems.add("PROJECT2");
		projectItems.add("PROJECT3");
		projectItems.add("PROJECT4");

		ArrayList<String> assigneeItems				= new ArrayList<String>();
		assigneeItems.add("ASSIGNEE1");
		assigneeItems.add("ASSIGNEE2");
		assigneeItems.add("ASSIGNEE3");
		assigneeItems.add("ASSIGNEE4");

		ArrayList<String> issueTypeItems				= new ArrayList<String>();
		issueTypeItems.add("TYPE1");
		issueTypeItems.add("TYPE2");
		issueTypeItems.add("TYPE3");
		issueTypeItems.add("TYPE4");

		ArrayList<String> reporterItems				= new ArrayList<String>();
		reporterItems.add("REPORTER1");
		reporterItems.add("REPORTER2");
		reporterItems.add("REPORTER3");
		reporterItems.add("REPORTER4");


		AutoCompleteTextView projectAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.projectAutoComplete);
		ArrayAdapter projectAdapter 				= new ArrayAdapter<String>(	context,
				android.R.layout.simple_dropdown_item_1line,
				projectItems);
		projectAutoCompleteTextView.setAdapter(projectAdapter);

		AutoCompleteTextView assigneeAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.assigneeAutoComplete);
		ArrayAdapter assigneeAdapter 				= new ArrayAdapter<String>(	context,
																				android.R.layout.simple_dropdown_item_1line,
																				assigneeItems);
		assigneeAutoCompleteTextView.setAdapter(assigneeAdapter);

		AutoCompleteTextView issueTypeAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.issueTypeAutoComplete);
		ArrayAdapter issueTypeAdapter 				= new ArrayAdapter<String>(	context,
				android.R.layout.simple_dropdown_item_1line,
				issueTypeItems);
		issueTypeAutoCompleteTextView.setAdapter(issueTypeAdapter);

		AutoCompleteTextView reporterAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.reporterAutoComplete);
		ArrayAdapter reporterAdapter 				= new ArrayAdapter<String>(	context,
				android.R.layout.simple_dropdown_item_1line,
				reporterItems);
		reporterAutoCompleteTextView.setAdapter(reporterAdapter);


		return rootView;
	}

}
