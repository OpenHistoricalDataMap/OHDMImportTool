package de.htwb.servlets;

import com.google.gson.Gson;
import de.htwb.model.imported.ImportedShape;
import de.htwb.shpImport.ShapeImporter;
import de.htwb.utils.ImportResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/ShapeImport")
@MultipartConfig
public class ShapeImportServlet extends HttpServlet {

    private ShapeImporter shapeImporter;

    @Override
    public void init() throws ServletException {
        super.init();
        shapeImporter = new ShapeImporter();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            File shapefile = shapeImporter.saveUploadedPartToFile(request.getPart("shapefile"));
            String userName = request.getParameter("userName");
            String tableName = shapeImporter.importFile(shapefile, userName);
            ArrayList<ImportedShape> importedShapes = shapeImporter.getImportedShapesFromTable(tableName);
            response.setHeader("content-type", "text/json");
            response.getWriter().write(String.format("{ \"key\" : %s}", tableName));

        }
        catch (Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error: " + ex.getMessage());
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
