package com.bignerdranch.android.letsgoal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by ianri on 11/28/2017.
 */

public class GoalPagerActivity extends AppCompatActivity implements AddGoalFragment.Callbacks {
    private static final String EXTRA_GOAL_ID = "com.bignerdranch.android.letsgoal.goal_id";

    private ViewPager mViewPager;
    private List<Goal> mGoals;

    public static Intent newIntent(Context packageContext, UUID goalId) {
        Intent intent = new Intent(packageContext, GoalPagerActivity.class);
        intent.putExtra(EXTRA_GOAL_ID, goalId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_pager);

        UUID goalId = (UUID) getIntent().getSerializableExtra(EXTRA_GOAL_ID);

        mViewPager = (ViewPager) findViewById(R.id.goal_view_pager);

        mGoals = GoalStorage.get(this).getGoals();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Goal goal = mGoals.get(position);
                return AddGoalFragment.newInstance(goal.getID());
            }

            @Override
            public int getCount() {
                return mGoals.size();
            }
        });

        for (int i = 0; i < mGoals.size(); i++) {
            if (mGoals.get(i).getID().equals(goalId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onGoalUpdated(Goal goal) {

    }

    @Override
    public void onGoalAdded(Goal goal) {

    }

    @Override
    public void onGoalDeleted(Goal goal) {
        UUID goalId = goal.getID();

        if (mGoals.size() > 1) {
            for (int i = 0; i < mGoals.size(); i++) {
                if (mGoals.get(i).getID().equals(goalId)) {
                    mViewPager.setCurrentItem(i - 1);
                    break;
                }
            }
        }
    }

    public void setPagerPreviousItem(int item) {
        mViewPager.setCurrentItem(item - 1);
    }
    
}
