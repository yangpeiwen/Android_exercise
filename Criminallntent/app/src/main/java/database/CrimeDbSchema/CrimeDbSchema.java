package database.CrimeDbSchema;


    //数据库类，注意数据库类要放到包database下面，也就是
    public class CrimeDbSchema {
        public static final class CrimeTable{
            //数据库内部类，定义数据表元素的String常量

            //数据表的标题
            public static final String NAME = "crimes";

            //定义数据表字段
            public static final class Cols{
                public static final String UUID = "uuid";
                public static final String TITLE = "title";
                public static final String DATE = "date";
                public static final String SOLVED = "solved";
            }
        }

    }

}
