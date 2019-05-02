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
        public static final String COLUMN_HOUR = "StartTimeHOUR";
        public static final String COLUMN_MINS = "StartTimeMINS";
        public static final String COLUMN_CONTENT = "Content";
    }

    public static class EventListData implements BaseColumns{
        public static final String TABLE_NAME = "EventListData";
        public static final String COLUMN_TIMEAMOUNT = "TimeAmount";
        public static final String COLUMN_CONTENT = "Content";
    }
}
