package com.sharifdev.torobche.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.sharifdev.torobche.Chat.ChatAdapter;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Integer> imageIds = new ArrayList<>();
    private ProgressBar progressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        //todo get contacts from server
//        names.add("zahra");
//        imageIds.add(R.drawable.plus);

//        names.add("zizi");
//        imageIds.add(R.drawable.ic_mail);

        progressBar = rootView.findViewById(R.id.load_search);
        progressBar.setVisibility(View.VISIBLE);
        getContacts(rootView);

        final MaterialSearchBar searchBar = rootView.findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                progressBar.setVisibility(View.VISIBLE);
                UserUtils.isUserAvailable(searchBar.getText(), new FunctionCallback<Boolean>() {
                    @Override
                    public void done(Boolean object, ParseException e) {
                        if (object) {
                            if (!names.contains(searchBar.getText())) {
                                names.add(searchBar.getText());
                                imageIds.add(R.drawable.ic_mail);
                                UserUtils.addContact(searchBar.getText());
                            }
                            getContacts(rootView);
                        } else {
                            Toast.makeText(getContext(), "Not Available!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onButtonClicked(int buttonCode) {
            }
        });

        return rootView;
    }

    private void updateList(View rootView) {
        RecyclerView userList = rootView.findViewById(R.id.user_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        userList.setLayoutManager(layoutManager);
        ChatAdapter chatAdapter = new ChatAdapter(getContext(), imageIds, names);
        userList.setAdapter(chatAdapter);
    }

    private void getContacts(final View view) {
        UserUtils.getContacts(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0) {
                    List<Object> contacts = objects.get(0).getList("contacts");
                    for (Object contact : contacts) {
                        if (!names.contains(((String) contact))) {
                            names.add(((String) contact));
                            imageIds.add(R.drawable.ic_mail);
                        }
                    }
                    updateList(view);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}