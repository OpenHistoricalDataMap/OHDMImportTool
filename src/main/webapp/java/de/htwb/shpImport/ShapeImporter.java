package de.htwb.shpImport;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.htwb.model.DatabaseRepository;
import de.htwb.model.imported.ImportedShape;
import org.apache.commons.io.*;

public class ShapeImporter
{
    private final String SHP_TO_PGSQL_FILE_PATH = "C:\\Program Files\\PostgreSQL\\11\\bin\\shp2pgsql.exe";
    private final String PGSQL_FILE_PATH = "C:\\Program Files\\PostgreSQL\\11\\bin\\psql.exe";
    private final int BUFFER_SIZE = 8 * 1024;

    private final File uploadedFilesDir = new File("uploaded");
    private final File importFilesDir = new File("importFiles");

    private DatabaseRepository dbRepos;

    public ShapeImporter()
    {
        uploadedFilesDir.mkdir();
        importFilesDir.mkdir();
        dbRepos = new DatabaseRepository();
    }

    public String importFile(File zipFile, String userName) throws Exception
    {
        unzipFile(zipFile);


        File sqlFile = createSQLImportFile(zipFile);
        if(sqlFile == null)
            return null;

        String tableName = FilenameUtils.getBaseName(sqlFile.getName());
        importShapeIntoDb(sqlFile);

        dbRepos.transferShapesToCache(tableName, userName);

        //dbRepos.insertImportedShapes(shapes);

        //cleanUp(zipFile, sqlFile);
        return tableName;
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

    private File createSQLImportFile(File zipFile) throws IOException
    {
        long milliseconds = System.currentTimeMillis();
        Random rnd = new Random();
        int rndNumber = Math.abs(rnd.nextInt());

        String tableName = String.format("%d%d", rndNumber, milliseconds);
        String shpImportCmd = String.format("\"%s\" \"%s\" \"temp\".\"%s\"", SHP_TO_PGSQL_FILE_PATH, zipFile.getAbsolutePath(), tableName);

        Process pr = new ProcessBuilder(shpImportCmd).start();
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
        String sqlImportCommand = String.format("\"%s\" -d \"%s\" -U \"%s\" -h \"%s\" -f \"%s\"", PGSQL_FILE_PATH, "ohdm_test", "geoserver", "ohdm.f4.htw-berlin.de", importFile.getAbsolutePath());
        Process pr = new ProcessBuilder(sqlImportCommand).start();

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

    private void unzipFile(File zipFile) throws IOException
    {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry;
        byte[] buffer = new byte[BUFFER_SIZE];
        while((zipEntry = zis.getNextEntry()) != null)
        {
            File newFile = new File(zipFile.getParent(), zipEntry.getName());
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
    }

    private void cleanUp(File zipFile, File sqlFile) throws Exception
    {
        FileUtils.deleteDirectory(zipFile.getParentFile());
        dbRepos.dropTable(FilenameUtils.getBaseName(sqlFile.getName()));
        FileUtils.forceDelete(sqlFile);
        dbRepos.disconnect();
    }
}
