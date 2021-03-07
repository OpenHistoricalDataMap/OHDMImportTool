package de.htwb.model.imported;

import org.postgis.Polygon;

import java.util.Date;

public class ImportedShape
{
    private int id;
    private String name;
    private Date validSince;
    private int validSinceDay;
    private int validSinceMonth;
    private int validSinceYear;
    private Date validUntil;
    private int validUntilDay;
    private int validUntilMonth;
    private int validUntilYear;
    private int classificationId;
    private String username;
    private transient Polygon[] polygons;
    private String geoJson;


    //private int id;
    private String country ;
    private String geom;

    public ImportedShape(int id, String name, Date validSince, int validUntilYear, Polygon[] polygons)
    {
        this.id = id;
        this.name = name;
        this.validSince = validSince;
        this.validUntilYear = validUntilYear;
        this.polygons = polygons;
        this.classificationId = 0;
    }
    public ImportedShape(int id, String name, Date validSince,  int validSinceDay
            , int validSinceMonth
            , int validSinceYear
            , Date validUntil
            , int validUntilDay
            , int validUntilMonth
            , int validUntilYear, Polygon[] polygons)
    {
        this.id = id;
        this.name = name;
        this.validSince = validSince;
        this.validSinceDay = validSinceDay;
        this.validSinceMonth = validSinceMonth;
        this.validSinceYear = validSinceYear;
        this.validUntil = validUntil;
        this.validUntilDay = validUntilDay;
        this.validUntilMonth = validUntilMonth;
        this.validUntilYear = validUntilYear;
        this.polygons = polygons;
        this.classificationId = 0;
    }
    public ImportedShape(int id, String country, String geom,Polygon[] polygons)
    {
        this.id = id;
        this.country = country;
        this.geom = geom;

    }

    public Polygon[] getPolygons() {
        return polygons;
    }

    public Date getValidSince() { return validSince; }
    public int getValidSinceDay() { return validSinceDay; }
    public int getValidSinceMonth() { return validSinceMonth; }
    public int getValidSinceYear() { return validSinceYear; }

    public Date getValidUntil() {
        return validUntil;
    }
    public int getValidUntilDay() {
        return validUntilDay;
    }
    public int getValidUntilMonth() {
        return validUntilMonth;
    }
    public int getValidUntilYear() {
    return validUntilYear;
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
