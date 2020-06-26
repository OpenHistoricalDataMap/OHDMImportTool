package de.htwb.shpImport;

import javax.servlet.http.Part;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    private File createSQLImportFile(String path) throws IOException
    {
        String tableName =  DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());

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
        Process pr = new ProcessBuilder(PGSQL_FILE_PATH, "-d", DB_NAME,"-U", DB_USER, "-h", DB_HOST, "-f", importFile.getAbsolutePath()).start();

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
