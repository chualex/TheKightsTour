package com.csci448.alchu.thekightstour;

/**
 * Database table to store the fastest times
 */

public class GameDbSchema {

        public static final class RecordTable {
            public static final String NAME = "recordTable";

            public static final class Cols {
                public static final String BOARDSIZE = "boardsize";
                public static final String NAME = "name";
                public static final String TIME = "time";
            }
        }

}
