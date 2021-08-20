package com.sharifdev.torobche.backUtils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sharifdev.torobche.Activity.QuestionMakeActivity;
import com.sharifdev.torobche.Game.GameQuestionActivity;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.model.Quiz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Table of questions in Backend
 */
public class QuestionUtils {
    public static void saveUserQuestion(final String questionText, String choice1Text,
                                        String choice2Text,
                                        String choice3Text,
                                        String choice4Text,
                                        String categoryName,
                                        final ProgressBar progressBar,
                                        final int answer,
                                        final QuestionMakeActivity activity) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("questionText", questionText);
        params.put("choice1Text", choice1Text);
        params.put("choice2Text", choice2Text);
        params.put("choice3Text", choice3Text);
        params.put("choice4Text", choice4Text);
        params.put("answer", String.valueOf(answer));
        params.put("topic", categoryName);

        ParseCloud.callFunctionInBackground("save_user_question", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e != null)
                    e.printStackTrace();
                else {
                    progressBar.setVisibility(View.GONE);
                }
                Intent resultInten = new Intent();
                resultInten.putExtra("question_added", true);
                activity.setResult(Activity.RESULT_OK, resultInten);
                activity.onBackPressed();
            }
        });


    }

    public static void getUserQuestions(FunctionCallback<List<ParseObject>> callback) {
        HashMap<String, String> params = new HashMap<>();
        ParseCloud.callFunctionInBackground("get_user_questions", params, callback);
    }

    public static void getQuestionForEdit(String id, final QuestionMakeActivity activity) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserQuestions");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    e.printStackTrace();
                else {
                    // todo get the answer of question too
                    TextInputEditText topic = activity.findViewById(R.id.topic_inp);
                    EditText choice1 = activity.findViewById(R.id.c1_text);
                    EditText choice2 = activity.findViewById(R.id.c2_text);
                    EditText choice3 = activity.findViewById(R.id.c3_text);
                    EditText choice4 = activity.findViewById(R.id.c4_text);
                    EditText questionTxt = activity.findViewById(R.id.q_text);

                    topic.setText(objects.get(0).getString("topic"));
                    questionTxt.setText(objects.get(0).getString("questionText"));
                    try {
                        choice1.setText(objects.get(0).getParseObject("choice1").fetchIfNeeded().getString("text"));
                        choice2.setText(objects.get(0).getParseObject("choice2").fetchIfNeeded().getString("text"));
                        choice3.setText(objects.get(0).getParseObject("choice3").fetchIfNeeded().getString("text"));
                        choice4.setText(objects.get(0).getParseObject("choice4").fetchIfNeeded().getString("text"));
                    } catch (ParseException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    activity.getProgressBar().setVisibility(View.GONE);
                }
            }
        });
    }

    public static void addQuestions(ArrayList<ParseObject> ids, ProgressBar progressBar) {
    }

    public static void getPoint(final String type, final int[] answers, final Quiz quiz, final GameQuestionActivity activity) {
        final ParseObject q = quiz.quiz;
        final List<Object> questions = q.getList("questions");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String topic = "";
                ArrayList<String> ids = new ArrayList<>();
                int point = 0;
                int counter = 0;
                for (Object question : questions) {
                    if (topic.isEmpty())
                        topic = ((ParseObject) question).getString("topic");
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AnswerOfQuestion");
                    if (type.equals("single"))
                        query.whereEqualTo("questionDB", ((ParseObject) question));
                    else
                        query.whereEqualTo("userQuestion", ((ParseObject) question));
                    try {
                        List<ParseObject> parseObjects = query.find();
                        if (answers[counter++] == ((int) parseObjects.get(0).get("answer"))) {
                            point += 1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                ParseObject his = new ParseObject("UserQuizHistory");
                his.put("user", ParseUser.getCurrentUser());
                his.put("point", point);
                his.put("quizDate", new Date());
                his.put("categoryName", topic);
                try {
                    his.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // todo
                UserUtils.addPoint(point);
                try {
                    quiz.currentQ.delete();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
