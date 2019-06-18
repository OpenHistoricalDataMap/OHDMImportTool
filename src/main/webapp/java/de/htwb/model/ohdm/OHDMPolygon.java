package de.htwb.model.ohdm;

import org.postgis.Polygon;

public class OHDMPolygon
{
    private long id;
    private Polygon polygon;
    private long idSourceUser;

    public OHDMPolygon()
    {
        this.id = 0;
    }

    public OHDMPolygon(long id) {
        this.id = id;
    }

    public long getIdSourceUser() {
        return idSourceUser;
    }

    public void setIdSourceUser(long idSourceUser) {
        this.idSourceUser = idSourceUser;
    }

    public org.postgis.Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
