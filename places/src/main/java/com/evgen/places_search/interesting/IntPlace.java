package com.evgen.places_search.interesting;

public class IntPlace {

    private String name;
    private String xid;

    public IntPlace(String name) {
        this.name = name;
    }

    public IntPlace() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    @Override
    public String toString(){
        return name;
    }

}
