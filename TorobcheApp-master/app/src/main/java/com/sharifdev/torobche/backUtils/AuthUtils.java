package com.sharifdev.torobche.backUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.sharifdev.torobche.Home;
import com.sharifdev.torobche.R;

import org.w3c.dom.Text;

import java.util.HashMap;

public class AuthUtils {
    public static void signUpUser(String username, String password, String email, SignUpCallback callback) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(callback);
    }

    public static class UserSignUpCallback implements SignUpCallback {
        TextView result;
        ProgressBar progressBar;
        FragmentManager fragmentManager;
        TextInputEditText code;
        TextView codeError;
        Context context;

        public UserSignUpCallback(TextView result, ProgressBar progressBar
                , FragmentManager fragmentManager, TextInputEditText code
                , TextView codeError, Context context) {
            this.result = result;
            this.progressBar = progressBar;
            this.fragmentManager = fragmentManager;
            this.code = code;
            this.codeError = codeError;
            this.context = context;
        }

        @Override
        public void done(ParseException e) {
            progressBar.setVisibility(View.GONE);
            if (e == null) {
                HashMap<String, Object> params = new HashMap<>();
                ParseCloud.callFunctionInBackground("sendcode", params, new FunctionCallback<String>() {
                    @Override
                    public void done(String object, ParseException e) {
                        if (e == null) {
                        } else {
                            Log.d("verify_error", "Parse Error");
                            result.setText(e.getMessage());
                        }
                    }
                });
                EmailVerificationDialog verificationDialog = new EmailVerificationDialog(code, progressBar,
                        fragmentManager, codeError, context);
                verificationDialog.show(fragmentManager, "Verify Email");
                //
            } else {
                result.setText(e.getMessage());
            }
        }
    }

    public static class EmailVerificationDialog extends DialogFragment {
        TextInputEditText code;
        ProgressBar progressBar;
        FragmentManager fragmentManager;
        TextView codeError;
        Context context;

        public EmailVerificationDialog(TextInputEditText code, ProgressBar progressBar
                , FragmentManager fragmentManager, TextView codeError, Context context) {
            this.code = code;
            this.progressBar = progressBar;
            this.fragmentManager = fragmentManager;
            this.codeError = codeError;
            this.context = context;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Nullable
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Verification Code");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // verify code
                    progressBar.setVisibility(View.VISIBLE);
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("code", input.getText().toString());
                    ParseCloud.callFunctionInBackground("verify_code", params, new FunctionCallback<Boolean>() {
                        @Override
                        public void done(Boolean object, ParseException e) {
                            progressBar.setVisibility(View.GONE);
                            if (e == null) {
                                if (object) {
                                    // Go to Home Page
                                    codeError.setTextColor(Color.GREEN);
                                    codeError.setText(R.string.verified);
                                    codeError.setVisibility(View.VISIBLE);
                                    //

                                    // Go to Home
                                    Intent intent = new Intent(context, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);

                                } else {
                                    codeError.setText(R.string.incorrect_code);
                                    codeError.setTextColor(Color.RED);
                                    codeError.setVisibility(View.VISIBLE);
                                    // Delete currently created user
                                    ParseUser.getCurrentUser().deleteInBackground();
                                    ParseUser.logOutInBackground();
                                }
                            } else {
                                Log.d("verify_error", e.getMessage());
                            }
                        }
                    });
                }
            }).setView(input);
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            return alertDialog;
        }
    }

    public static class UserLoginCallback implements LogInCallback {
        TextView status;
        ProgressBar progressBar;
        Context context;

        public UserLoginCallback(TextView status, ProgressBar progressBar, Context context) {
            this.status = status;
            this.progressBar = progressBar;
            this.context = context;
        }

        @Override
        public void done(ParseUser user, ParseException e) {
            progressBar.setVisibility(View.GONE);
            if (user != null) {
                status.setText(R.string.login_suc);
                status.setTextColor(Color.GREEN);
                status.setVisibility(View.VISIBLE);
                // move to home page
                Intent intent = new Intent(context, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //
            } else {
                status.setTextColor(Color.RED);
                status.setVisibility(View.VISIBLE);
                status.setText(e.getMessage());
            }
        }
    }

}
