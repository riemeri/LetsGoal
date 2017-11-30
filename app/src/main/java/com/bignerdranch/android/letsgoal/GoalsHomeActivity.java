package com.bignerdranch.android.letsgoal;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

public class GoalsHomeActivity extends SingleFragmentActivity
        implements GoalsHomeFragment.Callbacks, AddGoalFragment.Callbacks {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    /*@Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }*/

    @Override
    protected Fragment createFragment() {
        return new GoalsHomeFragment();
    }

    @Override
    public void onHomeCreation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onGoalSelected(Goal goal, boolean isNew) {
        Intent intent = GoalPagerActivity.newIntent(this, goal.getID(), isNew);
        startActivity(intent);
    }

    @Override
    public void onGoalAdded(Goal goal) {
        Intent intent = GoalPagerActivity.newIntent(this, goal.getID(), true);
        startActivity(intent);

        GoalsHomeFragment homeFragment = (GoalsHomeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        homeFragment.updateUI();
    }

    @Override
    public void onGoalUpdated(Goal goal) {
        GoalsHomeFragment homeFragment = (GoalsHomeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        homeFragment.updateUI();
    }

    @Override
    public void onGoalDeleted(Goal goal) {
        Goal lastGoal = GoalStorage.get(this).getLastOrNextGoal(goal);

        Fragment newDetail;

        if (lastGoal.getID().compareTo(goal.getID()) != 0) {
            newDetail = AddGoalFragment.newInstance(lastGoal.getID(), false);

            GoalStorage.get(this).deleteGoal(goal.getID());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, newDetail)
                    .commit();
        }
        else {
            GoalStorage.get(this).deleteGoal(goal.getID());

            /*Goal newGoal = new Goal();
            GoalLab.get(this).addGoal(newGoal);

            newDetail = AddGoalFragment.newInstance(newGoal.getID());
            */
        }
        onGoalUpdated(goal);
    }
}
