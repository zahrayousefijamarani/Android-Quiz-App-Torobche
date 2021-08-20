package com.sharifdev.torobche.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.CategoryUtils;
import com.sharifdev.torobche.backUtils.QuestionUtils;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionMakeActivity extends AppCompatActivity {
    private boolean[] answer = new boolean[4];
    private ProgressBar progressBar;
    private ImageView[] choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_layout);
        progressBar = findViewById(R.id.save_ques);

        // edit current question
        editCurrentQuestion();

        choice = new ImageView[]{
                (ImageView) findViewById(R.id.imageView1),
                (ImageView) findViewById(R.id.imageView2),
                (ImageView) findViewById(R.id.imageView3),
                (ImageView) findViewById(R.id.imageView4)
        };

        setListeners((ImageView) findViewById(R.id.c1_image), 1);
        setListeners((ImageView) findViewById(R.id.c2_image), 2);
        setListeners((ImageView) findViewById(R.id.c3_image), 3);
        setListeners((ImageView) findViewById(R.id.c4_image), 4);
        setListeners((ImageView) findViewById(R.id.q_image), 5);

        final TextInputEditText topic = findViewById(R.id.topic_inp);
        topic.setInputType(EditorInfo.TYPE_NULL);
        TextInputLayout textInputLayout = findViewById(R.id.topic_inp_layout);
        setTopicPicker(topic, textInputLayout);

        final Button save = (Button) findViewById(R.id.save_question);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = 0;
                int ans = 0;
                for (int i = 0; i < answer.length; i++) {
                    if (answer[i]) {
                        selected += 1;
                        ans = i;
                    }
                }
                if (topic.getText() == null || topic.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Topic is Empty!!!", Toast.LENGTH_SHORT).show();
                } else if (selected == 0) {
                    Toast.makeText(getApplicationContext(), "Select an answer", Toast.LENGTH_SHORT).show();
                } else if (selected > 1) {
                    Toast.makeText(getApplicationContext(), "Select Just One Answer", Toast.LENGTH_SHORT).show();
                } else {
                    // todo edit
                    if (save.getText().equals(getString(R.string.edit)))
                        onBackPressed();

                    // save question
                    progressBar.setVisibility(View.VISIBLE);
                    QuestionUtils.saveUserQuestion(
                            ((TextView) findViewById(R.id.q_text)).getText().toString(),
                            ((TextView) findViewById(R.id.c1_text)).getText().toString(),
                            ((TextView) findViewById(R.id.c2_text)).getText().toString(),
                            ((TextView) findViewById(R.id.c3_text)).getText().toString(),
                            ((TextView) findViewById(R.id.c4_text)).getText().toString(),
                            ((TextInputEditText) findViewById(R.id.topic_inp)).getText().toString(),
                            progressBar,
                            ans + 1,
                            QuestionMakeActivity.this
                    );
//                    onBackPressed();
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel_question);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        for (int i = 0; i < choice.length; i++) {
            setAnswer(choice[i], i + 1);
        }

    }

    private void setListeners(ImageView imageView, final int requestCode) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, requestCode);
            }
        });

    }

    public void setAnswer(final ImageView imageView, final int a) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ImageView imageView1 : choice) {
                    imageView1.setImageResource(R.drawable.wrong);
                }
                if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.wrong).getConstantState()) {
                    imageView.setImageResource(R.drawable.correct);
                    for (int i = 0; i < answer.length; i++) {
                        answer[i] = false;
                    }
                    answer[a - 1] = true;
                } else {
                    imageView.setImageResource(R.drawable.wrong);
                    answer[a - 1] = false;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageView;
                switch (requestCode) {
                    case 1:
                        imageView = (ImageView) findViewById(R.id.c1_image);
                        break;
                    case 2:
                        imageView = (ImageView) findViewById(R.id.c2_image);
                        break;
                    case 3:
                        imageView = (ImageView) findViewById(R.id.c3_image);
                        break;
                    case 4:
                        imageView = (ImageView) findViewById(R.id.c4_image);
                        break;
                    case 5:
                        imageView = (ImageView) findViewById(R.id.q_image);
                        break;
                    default:
                        imageView = new ImageView(getApplicationContext());

                }
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setTopicPicker(final TextInputEditText topic, TextInputLayout textInputLayout) {
        topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final ArrayList<String> items = new ArrayList<>();
                CategoryUtils.getAllCategories(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e != null)
                            e.printStackTrace();
                        else {
                            for (ParseObject object : objects) {
                                items.add(object.getString("name"));
                            }
                        }
                        final String[] itemsStr = new String[]{};
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(QuestionMakeActivity.this);
                        builder.setTitle("Choose Topic")
                                .setSingleChoiceItems(items.toArray(itemsStr), 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        topic.setText(items.get(which));
                                        dialog.dismiss();
                                    }
                                })
                        ;
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            }
        });
    }

    private void editCurrentQuestion() {
        Button save = findViewById(R.id.save_question);
        String questionId = getIntent().getStringExtra("questionId");
        if (questionId != null) {
            save.setText(R.string.edit);
            progressBar.setVisibility(View.VISIBLE);
            QuestionUtils.getQuestionForEdit(questionId, this);
        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}