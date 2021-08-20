package com.sharifdev.torobche.Game;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.sharifdev.torobche.Activity.QuestionMakeActivity;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.QuizUtils;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment {
    public ProgressBar progressBar;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        progressBar = rootView.findViewById(R.id.load_quiz);
        progressBar.bringToFront();

        Button one_player_button = (Button) rootView.findViewById(R.id.one_player_btn);
        one_player_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChooseTopicFragment fragment = new ChooseTopicFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, fragment, "choose_topic_fragment")
//                        .addToBackStack(null)
                        .commit();
            }
        });

        Button multi_player_button = (Button) rootView.findViewById(R.id.multi_player_btn);
        multi_player_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseTopicFragment fragment = new ChooseTopicFragment();
                Bundle args = new Bundle();
                args.putBoolean("is_multi", true);
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, fragment, "choose_topic_fragment")
                        //.addToBackStack(null)
                        .commit();
            }
        });

        // todo

        Button group_button = (Button) rootView.findViewById(R.id.gp_btn);
        group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), GameQuestionActivity.class);
                startActivity(i);
            }
        });

        Button custom = (Button) rootView.findViewById(R.id.custom_quiz);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                QuizUtils.getUsersGroupQuiz(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        final ArrayList<String> names = new ArrayList<>();
                        for (ParseObject object : objects) {
                            names.add(object.getString("name"));
                        }

                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext());
                        builder.setTitle("Choose Topic")
                                .setItems(names.toArray(new String[]{}), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        progressBar.setVisibility(View.VISIBLE);
                                        QuizUtils.createCurrentForCustomQuiz(objects.get(which), new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Intent intent = new Intent(getContext(), GameQuestionActivity.class);
                                                intent.putExtra("type", "custom");
                                                startActivity(intent);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                })
                        ;
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });


               /* ChooseTopicFragment fragment = new ChooseTopicFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, fragment, "choose_topic_fragment")
                        //.addToBackStack(null)
                        .commit();*/
            }
        });

        return rootView;
    }
}
