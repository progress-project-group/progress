package DataBase.LongTermSchedule;

public class LongTermScheduleSchema {
    public static final class LongTermTable{
        public static final String NAME = "longTermSchedule";
        public static final class Cols{
            public static final String UUID = "long_term_uuid";
            public static final String NAME = "long_term_name";
            public static final String DATE = "long_term_date";
            public static final String INDEX = "long_term_index";
        }
    }

    public static final class SubLongTermTable{
        public static final String NAME = "subLongTermSchedule";
        public static final class Cols{
            public static final String UUID = "long_term_uuid";
            public static final String SUB_UUID = "sub_long_term_uuid";
            public static final String FINISHED = "sub_long_term_finished";
            public static final String TITLE = "sub_long_term_title";
            public static final String PERCENT = "sub_long_term_percent";
        }
    }
}
