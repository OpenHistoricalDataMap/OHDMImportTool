package de.htwb.servlets;

import com.google.gson.Gson;
import de.htwb.model.DatabaseRepository;
import de.htwb.model.imported.ImportedShape;
import de.htwb.utils.ImportResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/ShapeUpdate")
@MultipartConfig
public class ShapeUpdateServlet extends HttpServlet
{
    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            Gson gson = new Gson();
            String json = request.getParameter("shapeData");
            String tableName = request.getParameter("tableName");
            ImportedShape importedShape = gson.fromJson(json, ImportedShape.class);

            DatabaseRepository dbRepos = new DatabaseRepository();
            dbRepos.connect();

            dbRepos.updateImportedShape(importedShape, tableName);
        }
        catch(Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error: " + ex.getMessage());
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       try
        {
            DatabaseRepository dbRepos = new DatabaseRepository();
            dbRepos.connect();
            String tableKey = request.getParameter("tableKey");
            ArrayList<ImportedShape> importedShapes = dbRepos.getImportedShapesFromCache(tableKey);

            Gson gson = new Gson();

            ImportResult importResult = new ImportResult(tableKey, importedShapes);
            String json = gson.toJson(importResult);
            response.setHeader("content-type", "text/json");
            response.getWriter().write(json);

            dbRepos.disconnect();
        }
        catch (Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error: " + ex.getMessage());
        }

    }
}
