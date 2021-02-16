package de.htwb.shpImport;

import de.htwb.utils.Config;

import java.io.*;

import static de.htwb.utils.Config.*;

public class OhdmLoader {
    private final File ConfigFileDir = new File("configFiles");
    public OhdmLoader()
    {

    }

    public void importFromIntermediateIntoOhdm() throws Exception
    {

        CreateConfigFiles();
        CreateStep2Schema();

        ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows
        String dir=ConfigFileDir.getAbsolutePath();
        String cmd="java -classpath "+JDBC_DRIVER_PATH+" -jar "+OHDM_CONVERTER_PATH +" -h \""+dir+"\\db_ohdm_historic_local\" -d \""+dir+"\\db_shape_import\"";
        Process pr =processBuilder.command("cmd.exe", "/c", cmd).start();

       pr.waitFor();
        try(BufferedReader brErr = new BufferedReader(new InputStreamReader(pr.getErrorStream())))
        {
            String errorLine;
            while((errorLine = brErr.readLine()) != null)
            {
                System.out.println(errorLine);
            }
        }

    }
    private void CreateStep2Schema() throws Exception
    {
        String SqlCmd=" CREATE SCHEMA IF NOT EXISTS "+SCHEME_INTERMEDIATE+"; CREATE SCHEMA IF NOT EXISTS "+SCHEME_OHDM+";";

        Process pr = new ProcessBuilder(PGSQL_FILE_PATH, "-d", DB_NAME,"-U", DB_USER, "-h", DB_HOST,"-c", SqlCmd).start();

        pr.waitFor();
        try(BufferedReader brErr = new BufferedReader(new InputStreamReader(pr.getErrorStream())))
        {
            String errorLine;
            while((errorLine = brErr.readLine()) != null)
            {
                System.out.println(errorLine);
            }
        }
    }
    private void CreateConfigFiles( ) throws Exception
    {

         String OHDM_PARAMETER="servername:"+  DB_HOST_OHDM+
                "\nportnumber:"+ DB_PORT_OHDM+
                "\nusername:"+ DB_USER_OHDM+
                "\npwd:"+DB_PASS_OHDM+
                "\ndbname:"+DB_NAME_OHDM+
                "\nschema:"+ SCHEME_OHDM;

          String HISTORIC_IMPORT_PARAMETER="servername:"+  DB_HOST_OHDM+
                "\nportnumber:"+ DB_PORT_OHDM+
                "\nusername:"+ DB_USER_OHDM+
                "\npwd:"+DB_PASS_OHDM+
                "\ndbname:"+DB_NAME_INTERMEDIATE+
                "\nschema:"+ SCHEME_INTERMEDIATE+
                "\ncolumnNameObjectName:"+GID+
                "\ncolumnNameGeometry:"+ GEOM+
                "\ntableName:"+ TABLENAME+
                "\nvalidSince:"+ VALIDSINCE+
                "\nvalidUntil:"+ VALIDUNTIL+
                "\nclassificationID:"+ CLASSIFICATION_ID;
        PrintWriter writer = new PrintWriter("configFiles/db_shape_import", "UTF-8");
        writer.println(OHDM_PARAMETER);
        writer.close();
        writer = new PrintWriter("configFiles/db_ohdm_historic_local", "UTF-8");
        writer.println(HISTORIC_IMPORT_PARAMETER);
        writer.close();
    }

}
