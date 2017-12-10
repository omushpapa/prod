/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod;

/**
 *
 * @author giantas
 */
public class Tables {
    
    public class Prod {
        public static final String TABLE_NAME = "prod";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CREATE_DATE = "create_date";

        public static final String COLUMN_TITLE_TYPE = "TEXT NOT NULL";
        public static final String COLUMN_BODY_TYPE = "TEXT";
        public static final String COLUMN_DATE_TYPE = "DATE";
        public static final String COLUMN_CREATE_DATE_TYPE = "DATE NOT NULL";

        public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" + 
                COLUMN_TITLE + " " + COLUMN_TITLE_TYPE + "," +
                COLUMN_BODY + " " + COLUMN_BODY_TYPE + "," + 
                COLUMN_DATE + " " + COLUMN_DATE_TYPE + "," +
                COLUMN_CREATE_DATE + " " + COLUMN_CREATE_DATE_TYPE +")";
    }
    
    
    
}
