package com.yyh;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    //清除所有的活动
    public static void finishAll(){
     for(Activity activity:activities){{
        if(!activity.isFinishing()) {
            activity.finish();
        }
     }
    activities.clear();
    }
    }

}
