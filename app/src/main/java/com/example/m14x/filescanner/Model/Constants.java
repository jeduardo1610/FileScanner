package com.example.m14x.filescanner.Model;

/**
 * Created by m14x on 04/20/2016.
 */
public class Constants {

    public interface ACTION {
        public static String MAIN_ACTION = "com.example.m14x.filescanner.action.main";
        public static String INIT_ACTION = "com.example.m14x.filescanner.action.init";
        public static String PREV_ACTION = "com.example.m14x.filescanner.action.stop";
        public static String PLAY_ACTION = "com.example.m14x.filescanner.action.play";
        public static String STOP_ACTION = "com.example.m14x.filescanner.action.next";
        public static String STARTFOREGROUND_ACTION = "com.example.m14x.filescanner.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.example.m14x.filescanner.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

}
