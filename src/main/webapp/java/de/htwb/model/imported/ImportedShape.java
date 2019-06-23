package de.htwb.model.imported;

import org.postgis.Polygon;

import java.util.Date;

public class ImportedShape
{
    private int id;
    private String name;
    private Date validSince;
    private Date validUntil;
    private int classificationId;
    private String username;
    private transient Polygon[] polygons;
    private String geoJson;

    public ImportedShape(int id, String name, Date validSince, Date validUntil, Polygon[] polygons)
    {
        this.id = id;
        this.name = name;
        this.validSince = validSince;
        this.validUntil = validUntil;
        this.polygons = polygons;
        this.classificationId = 0;
    }

    public Polygon[] getPolygons() {
        return polygons;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public Date getValidSince() {
        return validSince;
    }

    public String getName() {
        return name;
    }

    public int getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(int classificationId) {
        this.classificationId = classificationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(String geoJson) {
        this.geoJson = geoJson;
    }
}
