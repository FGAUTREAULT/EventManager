package com.gautreault.eventmanager.dao.group;

public class Group {
    // Notez que l'identifiant est un long
    private long id;
    private String name;

    public Group(long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
