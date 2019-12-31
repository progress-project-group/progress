package DataBase;

import android.provider.BaseColumns;

public class FeedReaderContract {
    private FeedReaderContract(){}

    public static class Time implements  BaseColumns{
        public static final String TABLE_NAME = "Date";
        public static final String COLUMN_TIME = "Date";
    }

    public static class TimeLineData implements BaseColumns{
        public static final String TABLE_NAME = "TimeLineData";
        //记录开始时间
        public static final String COLUMN_HOUR = "StartTimeHOUR";
        public static final String COLUMN_MINS = "StartTimeMINS";
        //记录计划内容
        public static final String COLUMN_CONTENT = "Content";
        //记录计划种类
        public static final String COLUMN_TYPE = "Type";
    }

    public static class EventListData implements BaseColumns{
        public static final String TABLE_NAME = "EventListData";
        //番茄钟数以及每个番茄钟的工作与休息时间
        public static final String COLUMN_PORNUMS = "PorNums";
        public static final String COLUMN_WORK = "Work";
        public static final String COLUMN_RELAX = "Relax";
        //计划内容
        public static final String COLUMN_CONTENT = "Content";
        //时间类别
        public static final String COLUMN_TYPE = "Type";
    }
}
