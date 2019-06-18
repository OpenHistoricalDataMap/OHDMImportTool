package de.htwb.model.ohdm;

public class Geoobject
{
    private long id;
    private String name;
    private long idSourceUser;

    public Geoobject()
    {
        this.id = 0;
    }

    public Geoobject(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public long getIdSourceUser()
    {
        return idSourceUser;
    }

    public void setIdSourceUser(long idSourceUser)
    {
        this.idSourceUser = idSourceUser;
    }
}
