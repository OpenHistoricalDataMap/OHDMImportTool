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
    private Date validUntil;

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

    public Date getValidSince() {
        return validSince;
    }

    public void setValidSince(Date validSince) {
        this.validSince = validSince;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

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
