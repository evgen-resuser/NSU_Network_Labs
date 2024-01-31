package org.evgen.model.int_places;

public record IntPlace(String name, String xid) {
    @Override
    public String toString(){
        return name;
    }
}
