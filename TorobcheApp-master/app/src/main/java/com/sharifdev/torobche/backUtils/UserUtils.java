package com.sharifdev.torobche.backUtils;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharifdev.torobche.Chat.ChatActivity;
import com.sharifdev.torobche.Chat.ChatFragment;
import com.sharifdev.torobche.Home;
import com.sharifdev.torobche.HomeFragment;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.model.Message;

import java.util.HashMap;
import java.util.List;

/**
 * Work with user table in Backend
 */
public class UserUtils {
    public static void changeProfile(int newId, final Fragment fragment, final View view) {
        int avatarId = newId + 1;
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("avatar_id", avatarId);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    e.printStackTrace();
                else
                    ((HomeFragment) fragment).loadUserData(view);
            }
        });
    }

    public static void isUserAvailable(String user, FunctionCallback<Boolean> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", user);
        ParseCloud.callFunctionInBackground("does_user_exists", params, callback);
    }

    public static void addPoint(int point) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("level", ((int) currentUser.get("level")) + point);
        currentUser.saveInBackground();
    }

    public static void addContact(final String name) {
        final ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Contacts");
        query1.whereEqualTo("creator", ParseUser.getCurrentUser());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() == 0) {
                    ParseObject contacts = new ParseObject("Contacts");
                    contacts.put("creator", ParseUser.getCurrentUser());
                    contacts.addUnique("contacts", name);
                    try {
                        contacts.save();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    objects.get(0).addUnique("contacts", name);
                    try {
                        objects.get(0).save();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
//        addContact(name, ParseUser.getCurrentUser().getUsername());
    }

    private static void addContact(final String first, final String name) {
        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("User");
        query2.whereEqualTo("username", first);
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                final ParseObject first_user = objects.get(0);
                final ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Contacts");
                query1.whereEqualTo("creator", first_user);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() == 0) {
                            ParseObject contacts = new ParseObject("Contacts");
                            contacts.put("creator", first_user);
                            contacts.addUnique("contacts", name);
                            try {
                                contacts.save();
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            objects.get(0).addUnique("contacts", name);
                            try {
                                objects.get(0).save();
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    public static void getContacts(FindCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Contacts");
        query.whereEqualTo("creator", ParseUser.getCurrentUser());
        query.findInBackground(callback);
    }

    public static void getUserChat(final String opp, final ChatActivity activity) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Chat");
        query.whereEqualTo("firstUser", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("secondUser", opp);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Chat");
                query.whereEqualTo("secondUser", ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo("firstUser", opp);
                try {
                    List<ParseObject> objects1 = query.find();

                    if ((objects.size() == 0)) {
                        objects = objects1;
                    }

                    if (objects.size() == 0) {
                        ParseObject chat = new ParseObject("Chat");
                        chat.put("firstUser", ParseUser.getCurrentUser().getUsername());
                        chat.put("secondUser", opp);
                        try {
                            chat.save();
                            activity.chat = chat;
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        activity.progressBar.setVisibility(View.GONE);
                    } else {
                        ParseObject parseObject = objects.get(0);
                        List<Object> messages = parseObject.getList("messages");
                        if (messages != null) {
                            for (Object message : messages) {
                                try {
                                    ((ParseObject) message).fetch();
                                    activity.messages.add(
                                            new Message(((ParseObject) message).getString("msg"), ((ParseObject) message)
                                                    .getString("sender"), R.drawable.no_photo));
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        activity.chat = objects.get(0);
                        activity.progressBar.setVisibility(View.GONE);
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                activity.setChat();
            }
        });
    }

    public static void sendMsg(final String msg, final String opp, final ChatActivity activity) {
        ParseObject message = new ParseObject("Message");
        message.put("sender", ParseUser.getCurrentUser().getUsername());
        message.put("receiver", opp);
        message.put("msg", msg);
        message.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ParseQuery<ParseObject> sentMessage = new ParseQuery<ParseObject>("Message");
                sentMessage.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
                sentMessage.whereEqualTo("receiver", opp);
                sentMessage.whereEqualTo("msg", msg);
                try {
                    List<ParseObject> sent = sentMessage.find();
                    activity.chat.fetch();
                    activity.chat.add("messages", sent.get(0));
                    activity.chat.save();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
