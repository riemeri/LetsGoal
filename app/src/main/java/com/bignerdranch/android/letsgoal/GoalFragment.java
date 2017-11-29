package com.bignerdranch.android.letsgoal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by ianri on 11/28/2017.
 */

public class GoalFragment extends Fragment {

    private static final String ARG_GOAL_ID = "goal_id";

    private Goal mGoal;
    private EditText mTitleField;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onGoalUpdated(Goal goal);
        void onGoalDeleted(Goal goal);
        void onGoalAdded(Goal goal);
    }

    public static GoalFragment newInstance(UUID goalId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_GOAL_ID, goalId);

        GoalFragment fragment = new GoalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID goalId = (UUID) getArguments().getSerializable(ARG_GOAL_ID);
        mGoal = GoalStorage.get(getActivity()).getGoal(goalId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        GoalStorage.get(getActivity()).updateGoal(mGoal);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goal, container, false);

        mTitleField = (EditText) v.findViewById(R.id.goal_title);
        mTitleField.setText(mGoal.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGoal.setTitle(s.toString());
                updateGoal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_goal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:
                delelteGoal();
                //getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateGoal() {
        GoalStorage.get(getActivity()).updateGoal(mGoal);
        getActivity();
    }

    private void delelteGoal() {
        //if its not for tablet view
        //if (getActivity().findViewById(R.id.detail_fragment_container) == null) {
            GoalStorage.get(getActivity()).deleteGoal(mGoal.getID());
            //mCallbacks.onCrimeUpdated(mCrime);
            getActivity().finish();
        /*}
        else {//for tablet view
            mCallbacks.onCrimeDeleted(mCrime);
            if (CrimeLab.get(getActivity()).getCrimes().size() == 0) {
                mLinearLayout.setVisibility(INVISIBLE);
                mNoCrimesText.setVisibility(VISIBLE);
                mAddCrimeButton.setVisibility(VISIBLE);
            }
        }*/


    }
}
