package com.bignerdranch.android.letsgoal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by ianri on 11/28/2017.
 */

public class AddGoalFragment extends Fragment {

    private static final String ARG_GOAL_ID = "goal_id";
    private static final String ARG_IS_NEW = "is_new";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private Goal mGoal;
    private EditText mTitleField;
    private Spinner mSpinner;
    private Button mDateButton;
    private Button mSaveButton;
    private Button mCancelButton;
    private TextView mDateTextView;
    private Callbacks mCallbacks;

    private Boolean mIsNew;
    private String mTempTitle;
    private int mImportance;

    public interface Callbacks {
        void onGoalUpdated(Goal goal);
        void onGoalDeleted(Goal goal);
        void onGoalAdded(Goal goal);
    }

    public static AddGoalFragment newInstance(UUID goalId, boolean isNew) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_GOAL_ID, goalId);
        args.putBoolean(ARG_IS_NEW, isNew);

        AddGoalFragment fragment = new AddGoalFragment();
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
        mIsNew = getArguments().getBoolean(ARG_IS_NEW);
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
        View v = inflater.inflate(R.layout.fragment_add_goal, container, false);

        mTempTitle = mGoal.getTitle();

        mTitleField = (EditText) v.findViewById(R.id.goal_title);
        mTitleField.setText(mGoal.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTempTitle = s.toString();
                //mGoal.setTitle(s.toString());
                //updateGoal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateTextView = (TextView) v.findViewById(R.id.dateTextView);

        mDateButton = (Button) v.findViewById(R.id.calendarButton);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newIstance(mGoal.getDueDate());
                dialog.setTargetFragment(AddGoalFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSpinner = (Spinner) v.findViewById(R.id.impact_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.importance_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(mGoal.getSpinnerIndex());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                        mImportance = 4;
                        //tempGoal.setImportance(4);
                        break;
                    case 1:
                        mImportance = 3;
                        //tempGoal.setImportance(3);
                        break;
                    case 2:
                        mImportance = 2;
                        //tempGoal.setImportance(2);
                        break;
                    case 3:
                        mImportance = 1;
                        //tempGoal.setImportance(1);
                        break;
                    case 4:
                        mImportance = 0;
                        //tempGoal.setImportance(0);
                }
                updateGoal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSaveButton = (Button) v.findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoal.setTitle(mTempTitle);
                mGoal.setImportance(mImportance);
                updateGoal();
                getActivity().finish();
            }
        });

        mCancelButton = (Button) v.findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsNew) {
                    delelteGoal();
                } else {
                    getActivity().finish();
                }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mGoal.setDueDate(date);
            updateGoal();
            updateDate();
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

    private void updateDate() {
        mDateTextView.setText(mGoal.getDueDate().toString());
    }

    private class spAdapter implements SpinnerAdapter {

        private List<String> mTempList;

        public spAdapter() {
            String[] levels = {"Very Important", "Important", "Normal", "Causual", "Not Important"};
            for (String s : levels) {
                mTempList.add(s);
            }
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }


}
