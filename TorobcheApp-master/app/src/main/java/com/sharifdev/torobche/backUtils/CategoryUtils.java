package com.sharifdev.torobche.backUtils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sharifdev.torobche.Category.SelectCategoryActivity;
import com.sharifdev.torobche.Home;
import com.sharifdev.torobche.HomeFragment;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Work with categories table in Backend
 */
public class CategoryUtils {
    public static void getAllCategories(final ArrayList<SelectCategoryActivity.HolderClass> holderClasses,
                                        final SelectCategoryActivity activity) {
        final HashMap<String, Integer> categories = new HashMap<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    e.printStackTrace();
                else {
                    for (ParseObject parseObject : objects) {
                        categories.put(((String) parseObject.get("name")), ((Integer) parseObject.get("icon_id")));
                        // todo category image
                        holderClasses.add(new SelectCategoryActivity.HolderClass(
                                ((String) parseObject.get("name")),
                                getCategoryImageByID(parseObject.getInt("icon_id"))
                        ));
                    }
                    // do other things for holder
                    activity.setUpHolders();
                    // stop progress bar
                    activity.progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    public static void getAllCategories(FindCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.findInBackground(callback);
    }

    public static int getCategoryImageByID(int id) {
        switch (id) {
            case 1:
                return R.drawable.ic_technology;
            case 2:
                return R.drawable.ic_sport;
            case 3:
                return R.drawable.ic_games;
            case 4:
                return R.drawable.ic_history;
            case 5:
                return R.drawable.ic_food;
            case 6:
                return R.drawable.ic_education;
            case 7:
                return R.drawable.ic_business;
            case 8:
                return R.drawable.ic_tv;
            case 9:
                return R.drawable.ic_music;
            default:
                return R.drawable.ic_general;
        }
    }

    public static int getCategoryImageByName(String catName) {
        switch (catName) {
            case "Technology":
                return R.drawable.ic_technology;
            case "Sports":
                return R.drawable.ic_sport;
            case "Games":
                return R.drawable.ic_games;
            case "History":
                return R.drawable.ic_history;
            case "Food":
                return R.drawable.ic_food;
            case "Education":
                return R.drawable.ic_education;
            case "Business":
                return R.drawable.ic_business;
            case "TV":
                return R.drawable.ic_tv;
            case "Music":
                return R.drawable.ic_music;
            default:
                return R.drawable.ic_general;
        }
    }

    public static class SaveCategoryOnClockListener implements View.OnClickListener {
        SelectCategoryActivity activity;

        public SaveCategoryOnClockListener(SelectCategoryActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            activity.progressBar.setVisibility(View.VISIBLE);
            ArrayList<SelectCategoryActivity.HolderClass> selectedCategory = activity.getSelectedCategory();
            final ParseUser currentUser = ParseUser.getCurrentUser();
            ArrayList<String> catNames = new ArrayList<>();
            for (SelectCategoryActivity.HolderClass cat : selectedCategory) {
                catNames.add(cat.name);
            }

            // query these categories
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
            query.whereContainedIn("name", catNames);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null)
                        e.printStackTrace();
                    else {
                        currentUser.addAllUnique("FavoriteCategories", objects);
                        try {
                            currentUser.save();
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        activity.progressBar.setVisibility(View.GONE);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("saved", true);
                        activity.setResult(Activity.RESULT_OK, resultIntent);
                        activity.finish();
                    }
                }
            });
        }
    }

    public static void getCategoriesByPointer(List<ParseObject> cats,
                                              final HomeFragment fragment,
                                              final View view) {
        if (cats == null) {
            fragment.initCategoryView(view, new ArrayList<ParseObject>(), false);
            return;
        }

        ArrayList<String> pointers = new ArrayList<>();
        for (ParseObject cat : cats) {
            pointers.add(cat.getObjectId());
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.whereContainedIn("objectId", pointers);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    e.printStackTrace();
                else
                    fragment.initCategoryView(view, objects, false);
            }
        });
    }

//    public static Question fetchQuestion(){
//
//    }
}
