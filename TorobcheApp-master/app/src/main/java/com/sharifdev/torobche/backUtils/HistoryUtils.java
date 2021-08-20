package com.sharifdev.torobche.backUtils;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.sharifdev.torobche.model.History;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Quiz History table in Backend
 */
public class HistoryUtils {
    public static void getUserHistory(FunctionCallback<List<ParseObject>> callback) {
        final HashMap<String, String> params = new HashMap<>();
        ParseCloud.callFunctionInBackground("get_user_history", params, callback);
    }
}
