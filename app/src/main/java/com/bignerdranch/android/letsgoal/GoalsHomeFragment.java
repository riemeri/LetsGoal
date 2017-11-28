package com.bignerdranch.android.letsgoal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by ianri on 11/26/2017.
 */

public class GoalsHomeFragment extends Fragment {
    private static final String TAG = "GoalsHomeFragment";

    private RecyclerView mRecyclerView;
    private CardView mCardView;
    private GoalAdapter mAdapter;
    private Button mAddButton;

    public static GoalsHomeFragment newInstance() {
        return new GoalsHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goal_home, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.goal_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddButton = (Button) v.findViewById(R.id.add_goal);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewGoal();
            }
        });

        updateUI();

        return v;
    }

    private void createNewGoal() {
        Goal goal = new Goal();
        GoalStorage.get(getActivity()).addGoal(goal);
        updateUI();
    }

    public void updateUI() {
        GoalStorage goalStorage = GoalStorage.get(getActivity());
        List<Goal> goals = goalStorage.getGoals();

        if (mAdapter == null) {
            mAdapter = new GoalAdapter(goals);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setGoals(goals);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class GoalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mPercentTextView;
        private TextView mDaysRemainingTextView;
        private ProgressBar mProgressBar;
        private Goal mGoal;

        public GoalHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_goal_card, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.goal_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.goal_date_text);
            mPercentTextView = (TextView) itemView.findViewById(R.id.goal_percent_text);
            mDaysRemainingTextView = (TextView) itemView.findViewById(R.id.days_left_text);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }

        public void bind(Goal goal) {
            mGoal = goal;
            mTitleTextView.setText(mGoal.getTitle());
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            String date = DateFormat.getDateInstance(DateFormat.SHORT).format(mGoal.getDueDate());
            mDateTextView.setText(date.toString()); //EDIT THIS
            mDaysRemainingTextView.setText(mGoal.getDaysLeft() + " Days Left");
            mPercentTextView.setText(mGoal.getProgress() + "%");
            mProgressBar.setProgress(mGoal.getProgress());
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class GoalAdapter extends RecyclerView.Adapter<GoalHolder> {

        private List<Goal> mGoals;

        public GoalAdapter(List<Goal> goals) {
            mGoals = goals;
        }

        @Override
        public GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new GoalHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(GoalHolder holder, int position) {
            Goal goal = mGoals.get(position);
            holder.bind(goal);
        }

        @Override
        public int getItemCount() {
            return mGoals.size();
        }

        public void setGoals(List<Goal> goals) {
            mGoals = goals;
        }
    }
}
