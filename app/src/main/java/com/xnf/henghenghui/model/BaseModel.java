
package com.xnf.henghenghui.model;

public class BaseModel {

    protected long id;
    protected String name;
    protected int type;

    public BaseModel() {
        super();
    }

    public BaseModel(long id, String name, int type) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "BaseModel [id=" + id + ", name=" + name + ", type=" + type + "]";
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
