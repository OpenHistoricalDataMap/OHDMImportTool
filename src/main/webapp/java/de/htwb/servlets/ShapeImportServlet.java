package de.htwb.servlets;

import de.htwb.model.imported.ImportedShape;
import de.htwb.shpImport.ShapeImporter;

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
    private OhdmLoader ohdmLoader;
    public String startDate;
    public String endDate;
    @Override
    public void init() throws ServletException {
        super.init();
        shapeImporter = new ShapeImporter();
        shapeImporter.CreateConfigs();
        shapeImporter.LoadConfigs();
       // ohdmLoader = new OhdmLoader();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {

            File shapefile = shapeImporter.saveUploadedPartToFile(request.getPart("shapefile"));
            String userName = request.getParameter("userName");
            // startDate = request.getParameter("startDate");
            // endDate = request.getParameter("endDate");
            //String classificationId = request.getParameter("classificationId");

            String tableName = shapeImporter.importFile(shapefile, userName);
            shapeImporter.CreateConfigs();
            ArrayList<ImportedShape> importedShapes = shapeImporter.getImportedShapesFromTable(tableName);
           // ohdmLoader.importFromIntermediateIntoOhdm();
            response.sendRedirect("ShapeUpdate.html?tableKey="+tableName);

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
