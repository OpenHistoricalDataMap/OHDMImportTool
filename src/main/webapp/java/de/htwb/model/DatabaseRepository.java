package de.htwb.model;

import java.sql.*;
import java.util.ArrayList;

import de.htwb.model.imported.ImportedShape;
import de.htwb.model.ohdm.*;
import org.postgis.*;
import org.postgresql.PGConnection;

import static de.htwb.utils.Config.*;

public class DatabaseRepository {

    private Connection connection;

    public void connect() throws SQLException
    {
        if(connection != null)
        {
            return;
        }

        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://" + DB_HOST + "/" + DB_NAME,
                            DB_USER, DB_PASS);

            ((PGConnection) connection).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void disconnect() throws SQLException
    {
        if(connection != null)
        {
            connection.close();
            connection = null;
            System.out.println("Database connection closed.");
        }
    }

    public ArrayList<ImportedShape> getImportedShapesFromCache(String tableName) throws SQLException
    {
        if(connection == null)
            connect();

        final String sql = String.format("SELECT gid FROM \"%s\".\"%s\"", SCHEME_CACHE, tableName);
        ArrayList<ImportedShape> importedShapes = new ArrayList<>();
        PreparedStatement stmtGetImportedShapes = connection.prepareStatement(sql);

        try(ResultSet rs = stmtGetImportedShapes.executeQuery())
        {
            while(rs.next())
            {
                ImportedShape currentShape = getImportedShapeFromCache(tableName, rs.getInt(1));
                if(currentShape != null)
                    importedShapes.add(currentShape);
            }
        }

        return importedShapes;
    }

    public ArrayList<ImportedShape> getImportedShapesFromTable(String tableName) throws SQLException
    {
        if(connection == null)
            connect();

        final String sql = String.format("SELECT gid FROM \"%s\".\"%s\"", SCHEME_TEMP, tableName);
        ArrayList<ImportedShape> importedShapes = new ArrayList<>();
        PreparedStatement stmtGetImportedShapes = connection.prepareStatement(sql);

        try(ResultSet rs = stmtGetImportedShapes.executeQuery())
        {
            while(rs.next())
            {
                ImportedShape currentShape = getImportedShapeForId(tableName, rs.getInt(1));
                if(currentShape != null)
                    importedShapes.add(currentShape);
            }
        }

        return importedShapes;
    }

    private ImportedShape getImportedShapeForId(String tableName, int id) throws SQLException
    {
        final String sql = String.format("SELECT name, start_date, end_date FROM \"%s\".\"%s\" WHERE gid = ?", SCHEME_TEMP,  tableName);
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);

        try(ResultSet rs = ps.executeQuery())
        {
            if(!rs.next())
                return null;

            String name = rs.getString(1);
            Date validSince = rs.getDate(2);
            Date validUntil = rs.getDate(3);
            ArrayList<Polygon> polygonList = getPolygonsForId(tableName, id);
            Polygon[] polygons = getPolygonsForId(tableName, id).toArray(new Polygon[polygonList.size()]);
            return new ImportedShape(id, name, validSince, validUntil, polygons);
        }
    }

    private ImportedShape getImportedShapeFromCache(String tableName, int id) throws SQLException
    {
        final String sql = String.format("SELECT \"name\", \"validSince\", \"validUntil\", \"geom\", ST_ASGEOJSON(geom) FROM \"%s\".\"%s\" WHERE gid = ?", SCHEME_CACHE, tableName);
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);

        try(ResultSet rs = ps.executeQuery())
        {
            if(!rs.next())
                return null;

            String name = rs.getString(1);
            Date validSince = rs.getDate(2);
            Date validUntil = rs.getDate(3);
            PGgeometry geom = (PGgeometry) rs.getObject(4);
            MultiPolygon multiPolygon = new MultiPolygon(geom.getGeometry().getValue());
            Polygon[] polygons = multiPolygon.getPolygons();
            ImportedShape is = new ImportedShape(id, name, validSince, validUntil, polygons);
            is.setGeoJson(rs.getString(5));
            return is;
        }
    }

    private ArrayList<Polygon> getPolygonsForId(String tableName, int id) throws SQLException
    {
        ArrayList<Polygon> polygons = new ArrayList<>();
        final String sql = String.format("SELECT ST_GeometryN(geom, generate_series(1, ST_NumGeometries(geom)))  FROM %s.\"%s\" WHERE gid = ?", SCHEME_TEMP, tableName);
        PreparedStatement stmtPolygons = connection.prepareStatement(sql);
        stmtPolygons.setInt(1, id);

        try(ResultSet rs = stmtPolygons.executeQuery())
        {
            while(rs.next())
            {
                PGgeometry geom = (PGgeometry)rs.getObject(1);
                Polygon p = (Polygon)geom.getGeometry();
                polygons.add(p);
            }

        }

        return polygons;
    }

    public void insertImportedShapes(ArrayList<ImportedShape> importedShapes) throws SQLException
    {
        for(ImportedShape importedShape : importedShapes)
        {
            ArrayList<OHDMPolygon> OHDMPolygons = getOHDMPolygonsFromShape(importedShape);
            insertOHDMPolygons(OHDMPolygons);
            Geoobject geoobject = getGeoobjectFromShape(importedShape);
            insertGeoobject(geoobject);
            ArrayList<GeoobjectGeometry> geoobjectGeometries = getGeoobjectGeometries(importedShape, geoobject, OHDMPolygons);
            insertGeoobjectGeometries(geoobjectGeometries);
        }
    }


    private ArrayList<OHDMPolygon> getOHDMPolygonsFromShape(ImportedShape importedShape)
    {
        ArrayList<OHDMPolygon> OHDMPolygons = new ArrayList<>();
        for(Polygon polygon : importedShape.getPolygons())
        {
            OHDMPolygon newOHDMOHDMPolygon = new OHDMPolygon();
            newOHDMOHDMPolygon.setPolygon(polygon);
            OHDMPolygons.add(newOHDMOHDMPolygon);
        }
        return OHDMPolygons;
    }

    private Geoobject getGeoobjectFromShape(ImportedShape importedShape)
    {
        Geoobject geoobject = new Geoobject();
        geoobject.setName(importedShape.getName());
        return geoobject;
    }

    private ArrayList<GeoobjectGeometry> getGeoobjectGeometries(ImportedShape importedShape, Geoobject geoobject, ArrayList<OHDMPolygon> OHDMPolygons)
    {
        ArrayList<GeoobjectGeometry> geoobjectGeometries = new ArrayList<>();

        for(OHDMPolygon polygon : OHDMPolygons)
        {
            GeoobjectGeometry newGeometry = new GeoobjectGeometry();
            newGeometry.setIdGeoobjectSource(geoobject.getId());
            newGeometry.setIdTarget(polygon.getId());
            newGeometry.setIdTargetType(TypeTarget.POLYGON);
            newGeometry.setValidSince(importedShape.getValidSince());
            newGeometry.setValidUntil(importedShape.getValidUntil());
            newGeometry.setIdSourceUser(geoobject.getIdSourceUser());
            geoobjectGeometries.add(newGeometry);
        }
        return geoobjectGeometries;
    }

    private void insertOHDMPolygons(ArrayList<OHDMPolygon> polygons) throws SQLException
    {
        for(OHDMPolygon polygon : polygons)
        {
            insertOHDMPolygon(polygon);
        }
    }

    private void insertOHDMPolygon(OHDMPolygon polygon) throws SQLException
    {
        final String sql = String.format("INSERT INTO %s.\"polygons\" (polygon, source_user_id) VALUES (?, ?) RETURNING id", SCHEME_TEST);
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, new PGgeometry(polygon.getPolygon()));
        stmt.setLong(2, polygon.getIdSourceUser());

        if(stmt.execute())
        {
            ResultSet rs = stmt.getResultSet();
            rs.next();
            long polygonId = rs.getInt(1);
            polygon.setId(polygonId);
        }

    }

    private void insertGeoobject(Geoobject geoobject) throws SQLException
    {
        final String sql = String.format("INSERT INTO %s.\"geoobject\" (name, source_user_id) VALUES (?, ?) RETURNING id", SCHEME_TEST);
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, geoobject.getName());
        stmt.setLong(2, geoobject.getIdSourceUser());

        if(stmt.execute())
        {
            ResultSet rs = stmt.getResultSet();
            rs.next();
            long geoobjectId = rs.getInt(1);
            geoobject.setId(geoobjectId);
        }
    }

    private void insertGeoobjectGeometries(ArrayList<GeoobjectGeometry> geoobjectGeometries) throws SQLException
    {
        for (GeoobjectGeometry geoobjectGeometry : geoobjectGeometries) {
            insertGeoobjectGeometry(geoobjectGeometry);
        }
    }

    private void insertGeoobjectGeometry(GeoobjectGeometry geoobjectGeometry) throws SQLException
    {
        final String sql = String.format("INSERT INTO %s.\"geoobject_geometry\" (id_target, type_target, id_geoobject_source, classification_id, valid_since, valid_until, source_user_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id", SCHEME_TEST);
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, geoobjectGeometry.getIdTarget());
        stmt.setLong(2, geoobjectGeometry.getIdTargetType());
        stmt.setLong(3, geoobjectGeometry.getIdGeoobjectSource());
        stmt.setLong(4, geoobjectGeometry.getIdClassification());

        if(geoobjectGeometry.getValidSince() != null)
            stmt.setDate(5, new java.sql.Date(geoobjectGeometry.getValidSince().getTime()));
        else
            stmt.setNull(5, Types.DATE);

        if(geoobjectGeometry.getValidUntil() != null)
            stmt.setDate(6, new java.sql.Date(geoobjectGeometry.getValidUntil().getTime()));
        else
            stmt.setDate(6, new Date(0));

        stmt.setLong(7, geoobjectGeometry.getIdSourceUser());

        if(stmt.execute())
        {
            ResultSet rs = stmt.getResultSet();
            rs.next();
            long id = rs.getLong(1);
            geoobjectGeometry.setId(id);
        }
    }

    public void dropTable(String tableName) throws SQLException
    {
        final String sql = String.format("DROP TABLE %s.\"%s\"", SCHEME_TEMP, tableName);
        PreparedStatement stmt = connection.prepareStatement(sql);

        if(!stmt.execute())
        {
            System.err.println("Table could not be deleted");
        }
    }

    public ArrayList<OHDMClassification> getClassifications() throws SQLException
    {
        final String sql = String.format("SELECT * FROM "+SCHEME_TEST+".\"classification\"");
        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        if(rs == null)
            return null;

        ArrayList<OHDMClassification> classifications = new ArrayList<>();

        while(rs.next())
        {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String subclassName = rs.getString(3);
            classifications.add(new OHDMClassification(id, name, subclassName));
        }

        return classifications;
    }

    public void transferShapesToCache(String tableName, String userName) throws Exception
    {
        if(connection == null)
            connect();

        createCacheTable(tableName);

        ArrayList<ImportedShape> shapes = getImportedShapesFromTable(tableName);

        for(ImportedShape is : shapes)
        {
            insertShapeIntoCache(tableName, is, userName);
        }
    }

    private boolean createCacheTable(String tableName) throws Exception
    {
        final String sql = String.format("CREATE TABLE \""+SCHEME_CACHE+"\".\"%s\"\n" +
                "(\n" +
                "    gid SERIAL,\n" +
                "    name character varying(254),\n" +
                "    \"validSince\" date,\n" +
                "    \"validUntil\" date,\n" +
                "    \"classId\" integer,\n" +
                "    username character varying(254),\n" +
                "    geom geometry,\n" +
                "    PRIMARY KEY (gid)\n" +
                ")\n" +
                "WITH (\n" +
                "    OIDS = FALSE\n" +
                ");\n" +
                "\n" +
                "ALTER TABLE \""+SCHEME_CACHE+"\".\"%s\"\n" +
                "    OWNER to geoserver;", tableName, tableName);

        PreparedStatement ps = connection.prepareStatement(sql);

        return ps.execute();
    }

    private void insertShapeIntoCache(String tableName, ImportedShape shape, String userName) throws Exception
    {
        final String sql = String.format("INSERT INTO \""+SCHEME_CACHE+"\".\"%s\" (\"name\", \"validSince\", \"validUntil\", \"classId\", \"username\", \"geom\") VALUES(?, ?, ?, ?, ?, ?)", tableName);
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, shape.getName());
        if(shape.getValidSince() != null)
            ps.setDate(2, new java.sql.Date(shape.getValidSince().getTime()));
        else
            ps.setNull(2, Types.DATE);

        if(shape.getValidUntil() != null)
            ps.setDate(3, new java.sql.Date(shape.getValidUntil().getTime()));
        else
            ps.setNull(3, Types.DATE);

        ps.setInt(4, shape.getClassificationId());
        ps.setString(5, userName);
        ps.setObject(6, new PGgeometry(new MultiPolygon(shape.getPolygons())));

        ps.execute();
    }

    public void updateImportedShape(ImportedShape updatedImportedShape, String table) throws Exception
    {
        final String sql = String.format("UPDATE \""+SCHEME_CACHE+"\".\"%s\" SET \"name\" = ?, \"validSince\" = ?, \"validUntil\" = ?, \"classId\" = ? WHERE gid = ?", table);

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, updatedImportedShape.getName());
        ps.setDate(2,  new java.sql.Date(updatedImportedShape.getValidSince().getTime()));
        ps.setDate(3,  new java.sql.Date(updatedImportedShape.getValidUntil().getTime()));
        ps.setInt(4, updatedImportedShape.getClassificationId());
        ps.setInt(5, updatedImportedShape.getId());

        ps.executeUpdate();
    }
}