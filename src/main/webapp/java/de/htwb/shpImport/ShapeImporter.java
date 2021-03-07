package de.htwb.shpImport;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.htwb.model.DatabaseRepository;
import de.htwb.model.imported.ImportedShape;
import org.apache.commons.io.*;

import static de.htwb.utils.Config.*;

public class ShapeImporter
{
    private final int BUFFER_SIZE = 8 * 1024;
    private final File ConfigFileDir = new File("configFiles");
    private final File uploadedFilesDir = new File("uploaded");
    private final File importFilesDir = new File("importFiles");
    File file = new File("configFiles\\GenConfigs");

    private DatabaseRepository dbRepos;

    public ShapeImporter()
    {

        ConfigFileDir.mkdir();
        uploadedFilesDir.mkdir();
        importFilesDir.mkdir();

        dbRepos = new DatabaseRepository();
    }

    public void CreateConfigs() {
        try {



                String[] GenConfigs = {
                        "DB_USER = postgres",
                        "DB_PASS = root",
                        "DB_NAME = ohdm",
                        "DB_HOST = localhost",
                        "SCHEME_TEMP = temp",
                        "SCHEME_TEST = test",
                        "SCHEME_CACHE = intermediateosm",
                        "DB_GEO_USER=geoserver",
                        /*STEP 2*/
                        "DB_HOST_OHDM = localhost",
                        "DB_PORT_OHDM = 5432",
                        "DB_USER_OHDM = postgres",
                        "DB_PASS_OHDM = root",
                        //Intermediate
                        "DB_NAME_INTERMEDIATE =ohdm",
                        //ohdm
                        "DB_NAME_OHDM = ohdm",
                        "SCHEME_OHDM = ohdm",
                        "SCHEME_INTERMEDIATE = intermediateosm",
                        "GID=gid",
                        "GEOM=geom",
                        "COLUMNVALIDSINCEDAY=23",
                        "COLUMNVALIDSINCEMONTH=09",
                        "COLUMNVALIDSINCEYEAR=1930",
                        "COLUMNVALIDUNTILDAY=07",
                        "COLUMNVALIDUNTILMONTH=07",
                        "COLUMNVALIDUNTILYEAR=2017",
                        "VALIDSINCE=12-04-1999",
                        "VALIDUNTIL=12-04-2022",
                        "CLASSIFICATION_ID=13",
                        "JDBC_DRIVER_PATH= C:\\dev\\ohdm\\postgresql-42.1.1.jar",
                        "OHDM_CONVERTER_PATH=C:\\dev\\ohdm\\OHDMConverter.jar",
                        "TABLENAME=" + TABLENAME,
                        "SHP_TO_PGSQL_FILE_PATH = C:\\Program Files\\PostgreSQL\\9.6\\bin\\shp2pgsql.exe",
                        "PGSQL_FILE_PATH = C:\\Program Files\\PostgreSQL\\9.6\\bin\\psql.exe",
                };
                Files.write(file.toPath(), Arrays.asList(GenConfigs));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void LoadConfigs() {
        try {

            List<String> fileLines = Files.readAllLines(file.toPath());

            for (String Param:fileLines)
                {
                    String[] KeyValue= Param.split("=");
                    String VarKey= KeyValue[0].trim();
                    String VarValue= KeyValue[1].trim();
                switch (VarKey){

                    case "DB_USER":
                        DB_USER = VarValue;
                        break;
                    case "DB_PASS":
                        DB_PASS = VarValue;
                        break;
                    case "DB_NAME":
                        DB_NAME = VarValue;
                        break;
                    case "DB_HOST":
                        DB_HOST = VarValue;
                        break;
                    case "SCHEME_TEMP":
                        SCHEME_TEMP = VarValue;
                        break;
                    case "SCHEME_TEST":
                        SCHEME_TEST = VarValue;
                        break;
                    case "SCHEME_CACHE":
                        SCHEME_CACHE = VarValue;
                        break;
                    case "DB_GEO_USER":
                        DB_GEO_USER = VarValue;
                        break;
                    case "DB_HOST_OHDM":
                        DB_HOST_OHDM = VarValue;
                        break;
                    case "DB_PORT_OHDM":
                        DB_PORT_OHDM = VarValue;
                        break;
                    case "DB_USER_OHDM":
                        DB_USER_OHDM = VarValue;
                        break;
                    case "DB_PASS_OHDM":
                        DB_PASS_OHDM = VarValue;
                        break;
                    case "DB_NAME_INTERMEDIATE":
                        DB_NAME_INTERMEDIATE = VarValue;
                        break;
                    case "DB_NAME_OHDM":
                        DB_NAME_OHDM = VarValue;
                        break;
                    case "SCHEME_OHDM":
                        SCHEME_OHDM = VarValue;
                        break;
                    case "SCHEME_INTERMEDIATE":
                        SCHEME_INTERMEDIATE = VarValue;
                        break;
                    case "GID":
                        GID = VarValue;
                        break;
                    case "GEOM":
                        GEOM = VarValue;
                        break;
                    case "COLUMNVALIDSINCEDAY":
                        COLUMNVALIDSINCEDAY = VarValue;
                        break;
                    case "COLUMNVALIDSINCEMONTH":
                        COLUMNVALIDSINCEMONTH = VarValue;
                        break;
                    case "COLUMNVALIDSINCEYEAR":
                        COLUMNVALIDSINCEYEAR = VarValue;
                        break;
                    case "COLUMNVALIDUNTILDAY":
                        COLUMNVALIDUNTILDAY = VarValue;
                        break;
                    case "COLUMNVALIDUNTILMONTH":
                        COLUMNVALIDUNTILMONTH = VarValue;
                        break;
                    case "COLUMNVALIDUNTILYEAR":
                        COLUMNVALIDUNTILYEAR = VarValue;
                        break;
                    case "VALIDSINCE":
                        VALIDSINCE = VarValue;
                        break;
                    case "VALIDUNTIL":
                        VALIDUNTIL = VarValue;
                        break;
                    case "CLASSIFICATION_ID":
                        CLASSIFICATION_ID = VarValue;
                        break;
                    case "JDBC_DRIVER_PATH":
                        JDBC_DRIVER_PATH = VarValue;
                        break;
                    case "OHDM_CONVERTER_PATH":
                        OHDM_CONVERTER_PATH = VarValue;
                        break;
                    case "TABLENAME":
                        TABLENAME = VarValue;
                        break;
                    case "SHP_TO_PGSQL_FILE_PATH":
                        SHP_TO_PGSQL_FILE_PATH = VarValue;
                        break;
                    case "PGSQL_FILE_PATH":
                        PGSQL_FILE_PATH = VarValue;
                        break;

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String importFile(File zipFile, String userName) throws Exception
    {
        List unzipFiles = unzipFile(zipFile);
        String shapeFilesPath = checkShapeFilesAndReturnFilepath(unzipFiles);

        File sqlFile = createSQLImportFile(shapeFilesPath);
        if(sqlFile == null)
            return null;

        String tableName = FilenameUtils.getBaseName(sqlFile.getName());
        importShapeIntoDb(sqlFile);

        dbRepos.transferShapesToCache(tableName, userName);

        //dbRepos.insertImportedShapes(shapes);

        //cleanUp(zipFile, sqlFile);

        return TABLENAME=tableName;
    }

    public ArrayList<ImportedShape> getImportedShapesFromTable(String tableName) throws Exception
    {
        return dbRepos.getImportedShapesFromTable(tableName);
    }

    public File saveUploadedPartToFile(Part part) throws IOException
    {

        String fileName = part.getSubmittedFileName();

        long milliseconds = System.currentTimeMillis();
        Random rnd = new Random();
        int rndNumber = Math.abs(rnd.nextInt());

        String tempDirName = String.format("%s/%d%d", uploadedFilesDir.getAbsolutePath(), milliseconds, rndNumber);

        File tempDir = new File(tempDirName);
        tempDir.mkdir();

        File targetFile = new File(String.format("%s/%s", tempDir.getAbsolutePath(), fileName));

        InputStream inputStream = part.getInputStream();
        try(FileOutputStream fops = new FileOutputStream(targetFile))
        {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while((bytesRead = inputStream.read(buffer)) != -1)
            {
                fops.write(buffer, 0, bytesRead);
            }
        }

        return targetFile;
    }

    private File createSQLImportFile(String path) throws IOException
    {
        String tableName =  DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()); //"yyyyMMdd-HHmmss"

        Process pr = new ProcessBuilder(SHP_TO_PGSQL_FILE_PATH, path, SCHEME_TEMP +"."+tableName).start();

        File importFile;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream())))
        {
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            String importOutput = sb.toString();

            importFile = new File("importFiles/" + tableName + ".sql");

            try(FileOutputStream fops = new FileOutputStream(importFile))
            {
                fops.write(importOutput.getBytes());
            }

            try(BufferedReader brErr = new BufferedReader(new InputStreamReader(pr.getErrorStream())))
            {
                String errorLine;
                while((errorLine = brErr.readLine()) != null)
                {
                    System.out.println(errorLine);
                }
            }

            return importFile;
        }
    }

    private void importShapeIntoDb(File importFile) throws Exception
    {

        Process pr = new ProcessBuilder(PGSQL_FILE_PATH, "-d", DB_NAME,"-U", DB_USER, "-h", DB_HOST,"-f", importFile.getAbsolutePath()).start();

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

    private List unzipFile(File zipFile) throws IOException
    {
        List<File> unzippedFiles = new ArrayList<>();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry;
        byte[] buffer = new byte[BUFFER_SIZE];
        while((zipEntry = zis.getNextEntry()) != null)
        {
            File newFile = new File(zipFile.getParent(), zipEntry.getName());
            unzippedFiles.add(newFile);
            try(FileOutputStream fops = new FileOutputStream(newFile))
            {
                int len;
                while((len = zis.read(buffer)) > 0)
                {
                    fops.write(buffer, 0, len);
                }
            }
        }

        zis.closeEntry();
        zis.close();
        return unzippedFiles;
    }

    private void cleanUp(File zipFile, File sqlFile) throws Exception
    {
        FileUtils.deleteDirectory(zipFile.getParentFile());
        dbRepos.dropTable(FilenameUtils.getBaseName(sqlFile.getName()));
        FileUtils.forceDelete(sqlFile);
        dbRepos.disconnect();
    }

    private String checkShapeFilesAndReturnFilepath(List<File> files) throws Exception {

        List<String> filenames = new ArrayList<>();

        for(File file : files){
            if(FilenameUtils.getExtension(file.getName()).equals("shp")){
                filenames.add(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.')));
            } else if(FilenameUtils.getExtension(file.getName()).equals("shx")) {
                filenames.add(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.')));
            } else if (FilenameUtils.getExtension(file.getName()).equals("dbf")) {
                filenames.add(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.')));
            }
        }
        if (filenames.size() == 3 && filenames.stream().distinct().count() == 1) {
            return filenames.get(0);
        } else {
            throw new Exception("The zip file does not contain the necessary shape files (.shp, .shx, .dbf), or they do not have the same name!");
        }
    }
}
