package com.project.ozyegin.vesteljiramobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.ozyegin.vesteljiramobile.AssignedToMeFragment;
import com.project.ozyegin.vesteljiramobile.SearchIssueFragment;
import com.project.ozyegin.vesteljiramobile.ReportedToMeFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Assigned To Me fragment activity
			return new AssignedToMeFragment();
		case 1:
			// Reported To Me fragment activity
			return new ReportedToMeFragment();
		case 2:
			// Search Issue fragment activity
			return new SearchIssueFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
