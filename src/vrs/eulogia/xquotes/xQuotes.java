package vrs.eulogia.xquotes;

import android.app.Application;
import android.content.Context;

public class xQuotes extends Application{

    private static Context context;
    
    public static final int FAV_SELECTED = 1;
    public static final int BAN_SELECTED = 2;
    public static final int NONE_SELECTED = 0;
    

    public void onCreate(){
        super.onCreate();
        xQuotes.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return xQuotes.context;
    }
}
