package de.htwb.model.ohdm;

public class OHDMClassification
{
    private int id;
    private String name;
    private String subclassName;

    public OHDMClassification(int id, String name, String subclassName)
    {
        this.id = id;
        this.name = name;
        this.subclassName = subclassName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubclassName() {
        return subclassName;
    }

    public void setSubclassName(String subclassName) {
        this.subclassName = subclassName;
    }
}
