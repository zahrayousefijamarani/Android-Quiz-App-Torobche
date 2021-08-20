package com.sharifdev.torobche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sharifdev.torobche.Activity.ActivityFragment;
import com.sharifdev.torobche.Chat.ChatFragment;
import com.sharifdev.torobche.Game.GameFragment;
import com.sharifdev.torobche.model.User;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // top app bar
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigationBar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationBar.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        navigationBar.getMenu().getItem(1).setChecked(true);

        // load home fragment by default
        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);

        // load user from server
        updateUser(homeFragment);
    }

    private void updateUser(final HomeFragment home) {
        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                home.loadUserData(home.getView());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * listener for navigation bottom menu
     **/
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_activity:
                    loadFragment(new ActivityFragment());
                    return true;
                case R.id.menu_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.menu_game:
                    loadFragment(new GameFragment());
                    return true;
                case R.id.menu_chat:
                    loadFragment(new ChatFragment());
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.logout_appbar:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            finish();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.help_appbar:
                // todo
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}