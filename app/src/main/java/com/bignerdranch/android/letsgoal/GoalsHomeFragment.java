package com.bignerdranch.android.letsgoal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

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
    private Callbacks mCallbacks;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private boolean mDrawerClosed = true;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onGoalSelected(Goal goal);
        void onHomeCreation();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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

        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        mCallbacks.onHomeCreation();

        NavigationView nv = (NavigationView) v.findViewById(R.id.navigation_view);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings_button:

                    case R.id.menu_button:

                    case R.id.menu_add_button:
                        createNewGoal();
                }

                return true;
            }
        });


        updateUI();

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void createNewGoal() {
        Goal goal = new Goal();
        GoalStorage.get(getActivity()).addGoal(goal);
        updateUI();
        mCallbacks.onGoalSelected(goal);
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
        private CardView mCardView;

        public GoalHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_goal_card, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.goal_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.goal_date_text);
            mPercentTextView = (TextView) itemView.findViewById(R.id.goal_percent_text);
            mDaysRemainingTextView = (TextView) itemView.findViewById(R.id.days_left_text);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
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

            int bgColor;
            switch (mGoal.getImportance()) {
                case 0:
                    bgColor = getResources().getColor(R.color.colorNotImportant);
                    break;
                case 1:
                    bgColor = getResources().getColor(R.color.colorCasual);
                    break;
                case 2:
                    bgColor = getResources().getColor(R.color.colorNormal);
                    break;
                case 3:
                    bgColor = getResources().getColor(R.color.colorImportant);
                    break;
                case 4:
                    bgColor = getResources().getColor(R.color.colorVeryImportant);
                    break;
                default:
                    bgColor = getResources().getColor(R.color.cardview_light_background);
            }

            //mCardView.setCardBackgroundColor(bgColor);
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onGoalSelected(mGoal);
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
