package com.bignerdranch.android.letsgoal;

import android.support.v4.app.Fragment;

public class GoalsHomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GoalsHomeFragment.newInstance();
    }
}
