package de.htwb.utils;

public class Config {

	 /*
        required database settings with examples
     */

    /*STEP 1*/
    public static final String DB_USER = "postgres";
    public static final String DB_PASS = "root";
    public static final String DB_NAME = "ohdm";
    public static final String DB_HOST = "localhost";

    public static final String SCHEME_TEMP = "temp";
    public static final String SCHEME_TEST = "test";
    public static final String SCHEME_CACHE = "intermediateosm";
    public static final String DB_GEO_USER="geoserver";


    /*STEP 2*/
    public static final String DB_HOST_OHDM = "localhost";
    public static final String DB_PORT_OHDM = "5432";
    public static final String DB_USER_OHDM = "postgres";
    public static final String DB_PASS_OHDM = "root";
    //Intermediate
    public static final String DB_NAME_INTERMEDIATE ="ohdm"; // "postgres";
    //ohdm
    public static final String DB_NAME_OHDM = "ohdm";
    public static final String SCHEME_OHDM = "ohdm";//"importhistoric";

    public static final String SCHEME_INTERMEDIATE = "intermediateosm";
    public static final String GID="gid";
    public static final String GEOM="geom";
    public static final String VALIDSINCE="1990-10-03";
    public static final String VALIDUNTIL="2020-12-31";
    public static final String CLASSIFICATION_ID="13";

    public static final String JDBC_DRIVER_PATH="C:\\dev\\ohdm\\postgresql-42.1.1.jar";
    public static final String OHDM_CONVERTER_PATH="C:\\dev\\ohdm\\OHDMConverter.jar";

    public static String TABLENAME="";

    /*
        required Postgres application settings with examples
     */

    // for macOS
    //public static final String SHP_TO_PGSQL_FILE_PATH = "/Applications/Postgres.app/Contents/Versions/12/bin/shp2pgsql";
    //public static final String PGSQL_FILE_PATH = "/Applications/Postgres.app/Contents/Versions/12/bin/psql";

    // for Windows
    public static final String SHP_TO_PGSQL_FILE_PATH = "C:\\Program Files\\PostgreSQL\\9.6\\bin\\shp2pgsql.exe";
    public static final String PGSQL_FILE_PATH = "C:\\Program Files\\PostgreSQL\\9.6\\bin\\psql.exe";


}
