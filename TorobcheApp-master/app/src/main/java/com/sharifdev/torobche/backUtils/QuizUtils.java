package com.sharifdev.torobche.backUtils;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharifdev.torobche.Activity.QuestionMakeActivity;
import com.sharifdev.torobche.Game.GameQuestionActivity;
import com.sharifdev.torobche.model.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizUtils {
    public static void addQuiz(String quizName, String time,
                               FunctionCallback<ParseObject> callback,
                               ArrayList<String> usernames,
                               ArrayList<String> chosen) {
        Gson gson = new Gson();
        HashMap<String, String> params = new HashMap<>();
        params.put("name", quizName);
        params.put("time", time);
        params.put("users", gson.toJson(usernames.toArray(new String[]{})));
        params.put("questions", gson.toJson(chosen.toArray(new String[]{})));
        ParseCloud.callFunctionInBackground("add_quiz", params, callback);
    }

    public static void getQuizes(FunctionCallback<List<ParseObject>> callback) {
        HashMap<String, String> params = new HashMap<>();
        ParseCloud.callFunctionInBackground("get_user_quizes", params, callback);
    }

    public static void createSinglePlayer(String topic, FunctionCallback<ParseObject> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("topic", topic);
        ParseCloud.callFunctionInBackground("generate_single_quiz", params, callback);
    }

    public static void getCurrentQuiz(final String type, final GameQuestionActivity activity) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CurrentQuiz");
        query.whereEqualTo("type", type);
        ArrayList<ParseUser> users = new ArrayList<>();
        users.add(ParseUser.getCurrentUser());
        query.whereContainedIn("users", users);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    e.printStackTrace();
                else {
                    if (objects.size() > 0) {
                        System.out.println("------------3--------------");
                        final ParseObject parseObject = objects.get(0);
                        parseObject.getParseObject("quiz").fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                activity.quiz = new Quiz(object, type, parseObject);
                                activity.loadQuestion.setVisibility(View.GONE);
                                activity.getNxtQuestion();
                            }
                        });
                    }
                }
            }
        });
    }

    public static void getAnswer(Quiz quiz, final int number, final GameQuestionActivity activity) {
        List<Object> questions = quiz.quiz.getList("questions");
        final ParseObject q = ((ParseObject) questions.get(number));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    q.fetch();
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AnswerOfQuestion");
                    query.whereEqualTo("questionDB", ((ParseObject) q));
                    try {
                        List<ParseObject> parseObjects = query.find();
                        int answer = (int) parseObjects.get(0).get("answer");
                        activity.showAnswerOfOne(answer);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void getUsersGroupQuiz(FindCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Quiz");
        ArrayList<Object> parseUSers = new ArrayList<>();
        parseUSers.add(ParseUser.getCurrentUser());
        query.whereContainedIn("participants", parseUSers);
        query.whereExists("creator");
        query.findInBackground(callback);
    }

    public static void createCurrentForCustomQuiz(ParseObject q, SaveCallback callback) {
        ParseObject quiz = new ParseObject("CurrentQuiz");
        quiz.put("quiz", q);
        quiz.put("type", "custom");
        ArrayList<ParseUser> users = new ArrayList<>();
        users.add(ParseUser.getCurrentUser());
        quiz.put("users", users);
        quiz.saveInBackground(callback);
    }

    public static void goToWaitingRoom(String topic, FindCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("WaitingRoom");
        //ArrayList<ParseUser> useres = new ArrayList<>();
        //useres.add(ParseUser.getCurrentUser());
        //query.whereContainedIn("users", useres);
        query.whereEqualTo("topic", topic);
        query.findInBackground(callback);
    }
}
