package de.htwb.model.ohdm;

import java.util.Date;

public class GeoobjectGeometry {
    private long id;
    private long idGeoobjectSource;
    private long idTarget;
    private long idTargetType;
    private long idClassification;
    private long idSourceUser;
    private Date validSince;
    private int validSinceDay;
    private int validSinceMonth;
    private int validSinceYear;
    private Date validUntil;
    private int validUntilDay;
    private int validUntilMonth;
    private int validUntilYear;

    public GeoobjectGeometry()
    {
        this.id = 0;
    }

    public GeoobjectGeometry(long id)
    {
        this.id = id;
    }

    public long getIdGeoobjectSource() {
        return idGeoobjectSource;
    }

    public void setIdGeoobjectSource(long idGeoobjectSource) {
        this.idGeoobjectSource = idGeoobjectSource;
    }

    public long getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(long idTarget) {
        this.idTarget = idTarget;
    }

    public long getIdTargetType() {
        return idTargetType;
    }

    public void setIdTargetType(long idTargetType) {
        this.idTargetType = idTargetType;
    }

    public long getIdClassification() {
        return idClassification;
    }

    public void setIdClassification(long idClassification) {
        this.idClassification = idClassification;
    }


    public Date getValidSince() { return validSince; }
    public int getValidSinceDay() { return validSinceDay; }
    public int getValidSinceMonth() { return validSinceMonth; }
    public int getValidSinceYear() { return validSinceYear; }

    public void setValidSince(Date validSince) {
        this.validSince = validSince;
    }
    public void setValidSinceDay(int validSinceDay) { this.validSinceDay=validSinceDay ; }
    public void setValidSinceMonth(int validSinceMonth) { this.validSinceMonth=validSinceMonth; }
    public void setValidSinceYear(int validSinceYear) { this.validSinceYear=validSinceYear; }

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

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }
    public void setValidUntilDay(int validUntilDay) { this.validUntilDay=validUntilDay ; }
    public void setValidUntilMonth(int validUntilMonth) { this.validUntilMonth=validUntilMonth; }
    public void setValidUntilYear(int validUntilYear) { this.validUntilYear=validUntilYear; }




    public long getIdSourceUser() {
        return idSourceUser;
    }

    public void setIdSourceUser(long idSourceUser) {
        this.idSourceUser = idSourceUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
