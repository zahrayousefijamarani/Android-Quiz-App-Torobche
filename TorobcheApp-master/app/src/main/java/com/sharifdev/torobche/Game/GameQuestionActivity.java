package com.sharifdev.torobche.Game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharifdev.torobche.Activity.QuestionMakeActivity;
import com.sharifdev.torobche.Home;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.QuestionUtils;
import com.sharifdev.torobche.backUtils.QuizUtils;
import com.sharifdev.torobche.model.Question;
import com.sharifdev.torobche.model.Quiz;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GameQuestionActivity extends AppCompatActivity {
    private int q_number = 0;
    private Button next;
    boolean[] answers = new boolean[10];
    int h = 0;
    CountDownTimer mCountDownTimer;

    public Quiz quiz;
    public ProgressBar loadQuestion;
    private TextView num;
    public AlertDialog result;
    private String type;

    Question currentQuestion ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_question);

        loadQuestion = findViewById(R.id.loading_question_quiz);
        loadQuestion.bringToFront();
        getQuiz();

        num = findViewById(R.id.question_number);

        setAnswer((ImageView) findViewById(R.id.imageView1));
        setAnswer((ImageView) findViewById(R.id.imageView2));
        setAnswer((ImageView) findViewById(R.id.imageView3));
        setAnswer((ImageView) findViewById(R.id.imageView4));

        next = (Button) findViewById(R.id.next_question);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimer.cancel();
                saveAnswer();
                if (q_number == 10) {
                    loadQuestion.setVisibility(View.VISIBLE);
                    QuestionUtils.getPoint(type, answers, quiz, GameQuestionActivity.this);

                    result = new MaterialAlertDialogBuilder(GameQuestionActivity.this)
                            .setTitle("Finished! (Result are in History)")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadQuestion.setVisibility(View.GONE);
//                                    Intent i = new Intent(getApplicationContext(), Home.class);
//                                    startActivity(i);
                                    finish();
                                }
                            })
                            .create();

                    result.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            loadQuestion.setVisibility(View.GONE);
                            //Intent i = new Intent(getApplicationContext(), Home.class);
                            //startActivity(i);
                            finish();
                        }
                    });
                    result.show();
                }
                if (q_number < 10) {
                    //todo show correct answer( not important now)
                    //saveAnswer();
                    //showAnswer();
                    // next question
                    getNxtQuestion();
                }
            }
        });


    }

    private void showAnswers(int point) {
        TextView textView = findViewById(R.id.result_show);
        textView.setVisibility(View.VISIBLE);
        textView.bringToFront();
        textView.setText(point);
    }

        Button like = (Button)findViewById(R.id.like_question);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion.likes +=1;
            }
        });

        // todo get random question
        currentQuestion = new Question();
        setQuestion(currentQuestion);


    public void showAnswerOfOne(int answer) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Correct Answer: " + answer)
                .setPositiveButton("Continue", null).show();
    }

    public void getNxtQuestion() {
        loadQuestion.setVisibility(View.VISIBLE);
        final List<Object> questions = quiz.quiz.getList("questions");

        ((ParseObject) questions.get(q_number)).fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {


                if (object.getClassName().equals("QuestionDB")) {
                    Question w = new Question();
                    w.questionText = object.getString("text");
                    try {
                        ParseObject choice = object.getParseObject("choice1");
                        choice.fetch();
                        w.answerText1 = choice.getString("text");
                        choice = object.getParseObject("choice2");
                        choice.fetch();
                        w.answerText2 = choice.getString("text");
                        choice = object.getParseObject("choice3");
                        choice.fetch();
                        w.answerText3 = choice.getString("text");
                        choice = object.getParseObject("choice4");
                        choice.fetch();
                        w.answerText4 = choice.getString("text");
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    loadQuestion.setVisibility(View.GONE);
                    setQuestion(w);
                } else if (object.getClassName().equals("UserQuestions")) {
                    Question w = new Question();
                    w.questionText = object.getString("questionText");
                    try {
                        ParseObject choice = object.getParseObject("choice1");
                        choice.fetch();
                        w.answerText1 = choice.getString("text");
                        choice = object.getParseObject("choice2");
                        choice.fetch();
                        w.answerText2 = choice.getString("text");
                        choice = object.getParseObject("choice3");
                        choice.fetch();
                        w.answerText3 = choice.getString("text");
                        choice = object.getParseObject("choice4");
                        choice.fetch();
                        w.answerText4 = choice.getString("text");
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    loadQuestion.setVisibility(View.GONE);
                    setQuestion(w);
                }
            }
        });

    }

    private void getQuiz() {
        loadQuestion.setVisibility(View.VISIBLE);
        type = getIntent().getStringExtra("type");
        if (type != null && !type.isEmpty()) {
            switch (type) {
                case "single":
                    QuizUtils.getCurrentQuiz(type, this);
                    break;
                case "multi":
                    break;
                case "group":
                    break;
                case "custom":
                    QuizUtils.getCurrentQuiz(type, this);
                    break;
            }
        }
    }

    private void saveAnswer() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.correct).getConstantState()) {
            answers[q_number - 1] = 1;
            return;
        }
        imageView = (ImageView) findViewById(R.id.imageView2);
        if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.correct).getConstantState()) {
            answers[q_number - 1] = 2;
            return;
        }
        imageView = (ImageView) findViewById(R.id.imageView3);
        if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.correct).getConstantState()) {
            answers[q_number - 1] = 3;
            return;
        }
        imageView = (ImageView) findViewById(R.id.imageView4);
        if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.correct).getConstantState()) {
            answers[q_number - 1] = 4;
        }
    }

    public void setAnswer(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.wrong).getConstantState()) {
                    imageView.setImageResource(R.drawable.correct);
                } else {
                    imageView.setImageResource(R.drawable.wrong);
                }
            }
        });
    }

    /**
     * call it for each question ( set images to no_photo if the question have not photo)
     */
    private void setQuestion(Question q) {
        fillComponent(R.id.q_image, q.questionImage, R.id.q_text, q.getText());
        fillComponent(R.id.c1_image, q.image1, R.id.c1_text, q.answerText1);
        fillComponent(R.id.c2_image, q.image2, R.id.c2_text, q.answerText2);
        fillComponent(R.id.c3_image, q.image3, R.id.c3_text, q.answerText3);
        fillComponent(R.id.c4_image, q.image4, R.id.c4_text, q.answerText4);
        setDefaultImage((ImageView) findViewById(R.id.imageView1));
        setDefaultImage((ImageView) findViewById(R.id.imageView2));
        setDefaultImage((ImageView) findViewById(R.id.imageView3));
        setDefaultImage((ImageView) findViewById(R.id.imageView4));


        q_number += 1;
        num.setText(q_number + " / 10");

        setTimeBar();
    }


    private int calculateScore(int valueOfQ){
        int score = 0;
        for (int i =0 ;i< answers.length; i++){
            if(answers[i]){
                score += valueOfQ;
            }
        }
        return score;
    }

    private void setDefaultImage(ImageView image){
        image.setImageResource(R.drawable.wrong);
    }

    private void fillComponent(int imageId, int imageResource, int textId, String text) {
        ImageView image = findViewById(imageId);
        image.setImageResource(imageResource);

        TextView q_text = findViewById(textId);
        q_text.setText(text);
    }

    private void setTimeBar() {
        final ProgressBar timeBar = findViewById(R.id.time_bar);
        h = 0;
        timeBar.setProgress(h);
        mCountDownTimer = new CountDownTimer(10000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                h++;
                timeBar.setProgress((int) h * 100 / (10000 / 10));
            }

            @Override
            public void onFinish() {
                next.performClick();
                timeBar.setProgress(100);
            }
        };
        mCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }
}