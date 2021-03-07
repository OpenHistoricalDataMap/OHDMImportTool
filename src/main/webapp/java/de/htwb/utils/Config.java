package de.htwb.utils;

public class Config {

	 /*
        required database settings with examples
     */

    /*STEP 1*/
    public static String DB_USER ;//= "postgres";//
    public static  String DB_PASS ;//= "root";
    public static  String DB_NAME ;//= "ohdm";
    public static  String DB_HOST ;//= "localhost";

    public static  String SCHEME_TEMP ;//= "temp";
    public static  String SCHEME_TEST ;//= "test";
    public static  String SCHEME_CACHE;// = "intermediateosm";
    public static  String DB_GEO_USER;//="geoserver";


    /*STEP 2*/
    public static  String DB_HOST_OHDM ;//= "localhost";
    public static  String DB_PORT_OHDM ;//= "5432";
    public static  String DB_USER_OHDM ;//= "postgres";
    public static  String DB_PASS_OHDM ;//= "root";
    //Intermediate
    public static  String DB_NAME_INTERMEDIATE ;//="ohdm";
    //ohdm
    public static  String DB_NAME_OHDM;// = "ohdm";
    public static  String SCHEME_OHDM;// = "ohdm";

    public static  String SCHEME_INTERMEDIATE;// = "intermediateosm";
    public static  String GID;//="gid";
    public static  String GEOM;//="geom";
    public static  String VALIDSINCE;//="";
    public static  String VALIDUNTIL;//="";
    public static  String COLUMNVALIDSINCEDAY;
    public static  String COLUMNVALIDSINCEMONTH;
    public static  String COLUMNVALIDSINCEYEAR;
    public static  String COLUMNVALIDUNTILDAY;
    public static  String COLUMNVALIDUNTILMONTH;
    public static  String COLUMNVALIDUNTILYEAR;

    public static  String CLASSIFICATION_ID;//="13";

    public static  String JDBC_DRIVER_PATH;
    public static  String OHDM_CONVERTER_PATH;


    public static String TABLENAME="''";

    /*
        required Postgres application settings with examples
     */

    // for macOS
    //public static final String SHP_TO_PGSQL_FILE_PATH = "/Applications/Postgres.app/Contents/Versions/12/bin/shp2pgsql";
    //public static final String PGSQL_FILE_PATH = "/Applications/Postgres.app/Contents/Versions/12/bin/psql";

    // for Windows
    public static  String SHP_TO_PGSQL_FILE_PATH ;//= "C:\\Program Files\\PostgreSQL\\9.6\\bin\\shp2pgsql.exe";
    public static  String PGSQL_FILE_PATH ;//= "C:\\Program Files\\PostgreSQL\\9.6\\bin\\psql.exe";


}
