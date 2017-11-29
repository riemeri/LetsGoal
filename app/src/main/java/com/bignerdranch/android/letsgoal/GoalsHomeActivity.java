package com.bignerdranch.android.letsgoal;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class GoalsHomeActivity extends SingleFragmentActivity
        implements GoalsHomeFragment.Callbacks, GoalFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new GoalsHomeFragment();
    }

    @Override
    public void onGoalSelected(Goal goal) {
        Intent intent = GoalPagerActivity.newIntent(this, goal.getID());
        startActivity(intent);
    }

    @Override
    public void onGoalAdded(Goal goal) {
        Intent intent = GoalPagerActivity.newIntent(this, goal.getID());
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
            newDetail = GoalFragment.newInstance(lastGoal.getID());

            GoalStorage.get(this).deleteGoal(goal.getID());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, newDetail)
                    .commit();
        }
        else {
            GoalStorage.get(this).deleteGoal(goal.getID());

            /*Goal newGoal = new Goal();
            GoalLab.get(this).addGoal(newGoal);

            newDetail = GoalFragment.newInstance(newGoal.getID());
            */
        }
        onGoalUpdated(goal);
    }
}
