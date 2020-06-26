package de.htwb.servlets;

import com.google.gson.Gson;
import de.htwb.model.DatabaseRepository;
import de.htwb.model.ohdm.OHDMClassification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/OHDMInformation")
public class OHDMInformationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseRepository dbRepos = new DatabaseRepository();
        resp.setStatus(200);
        Gson gson = new Gson();
        try
        {
            dbRepos.connect();
            ArrayList<OHDMClassification> classifications = dbRepos.getClassifications();
            String json = gson.toJson(classifications);
            resp.setHeader("content-type", "text/json");
            resp.getWriter().write(json);
            dbRepos.disconnect();
        }
        catch(Exception ex)
        {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Internal server error: " + ex.getMessage());
        }
    }
}
