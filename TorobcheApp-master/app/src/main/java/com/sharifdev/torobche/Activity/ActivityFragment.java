package com.sharifdev.torobche.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.sharifdev.torobche.Category.SelectCategoryActivity;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.HistoryUtils;
import com.sharifdev.torobche.backUtils.QuestionUtils;
import com.sharifdev.torobche.backUtils.QuizUtils;
import com.sharifdev.torobche.model.Question;
import com.sharifdev.torobche.model.Quiz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {
    private ArrayList<HistoryAdapter.HistoryValue> histories = new ArrayList<>();
    private ArrayList<Quiz> quizzes = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    private ProgressBar progressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activity, container, false);

        progressBar = rootView.findViewById(R.id.get_activity_loading);
        progressBar.setVisibility(View.VISIBLE);

        initHistoryRecyclerView(rootView);

        initQuizRecyclerView(rootView);

        initQuestionRecyclerView(rootView);

        return rootView;
    }

    private void initHistoryRecyclerView(View rootView) {
        final RecyclerView historyRecyclerView = rootView.findViewById(R.id.history_recyclerView);
        historyRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        historyRecyclerView.setLayoutManager(layoutManager);

        HistoryAdapter mAdapter = new HistoryAdapter(getContext(), histories);
        historyRecyclerView.setAdapter(mAdapter);

        HistoryUtils.getUserHistory(new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                histories = new ArrayList<>();
                if (e != null)
                    e.printStackTrace();
                else {
                    for (ParseObject parseObject : object) {
                        histories.add(new HistoryAdapter.HistoryValue(
                                parseObject.getInt("point"),
                                new SimpleDateFormat("dd-MM-yyyy").format(parseObject.getDate("quizDate")),
                                ((String) parseObject.get("categoryName"))
                        ));
                    }
                }
                HistoryAdapter mAdapter = new HistoryAdapter(getContext(), histories);
                historyRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    private void initQuizRecyclerView(View rootView) {
        final RecyclerView quizRecyclerView = rootView.findViewById(R.id.quiz_recyclerView);
        quizRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        quizRecyclerView.setLayoutManager(layoutManager);

        quizzes = new ArrayList<>();
        QuizAdapter mAdapter = new QuizAdapter(getContext(), quizzes, questions, this);
        quizRecyclerView.setAdapter(mAdapter);

        QuizUtils.getQuizes(new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                if (e != null)
                    e.printStackTrace();
                else {
                    quizzes = new ArrayList<>();
                    for (ParseObject parseObject : object) {
                        quizzes.add(new Quiz(
                                parseObject.getString("name"),
                                parseObject.getList("participants"),
                                parseObject.getInt("time"),
                                parseObject.getList("questions")
                        ));
                    }
                    QuizAdapter mAdapter = new QuizAdapter(getContext(), quizzes, questions, ActivityFragment.this);
                    quizRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    public void initQuestionRecyclerView(View rootView) {
        final RecyclerView questionRecyclerView = rootView.findViewById(R.id.question_recyclerView);

        questionRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        questionRecyclerView.setLayoutManager(layoutManager);

        questions = new ArrayList<>();
        QuestionAdapter mAdapter = new QuestionAdapter(getContext(), questions, this);
        questionRecyclerView.setAdapter(mAdapter);


        QuestionUtils.getUserQuestions(new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                questions = new ArrayList<>();
                if (e != null)
                    e.printStackTrace();
                else {
                    for (ParseObject parseObject : object) {
                        questions.add(new Question(
                                parseObject.getString("questionText"),
                                parseObject.getParseObject("choice1"),
                                parseObject.getParseObject("choice2"),
                                parseObject.getParseObject("choice3"),
                                parseObject.getParseObject("choice4"),
                                parseObject.getString("topic"),
                                parseObject.getObjectId()
                        ));

                    }
                }
                QuestionAdapter mAdapter = new QuestionAdapter(getContext(), questions, ActivityFragment.this);
                questionRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null && data.getBooleanExtra("question_added", false)) {
                View view = getView();
                if (view != null) {
                    initQuestionRecyclerView(view);
                }
            }
            if (data != null && data.getBooleanExtra("quiz_added", false)) {
                View view = getView();
                if (view != null) {
                    initQuizRecyclerView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
