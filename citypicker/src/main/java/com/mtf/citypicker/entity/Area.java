package com.mtf.citypicker.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by mtf on 2017/10/18.
 */

@Entity
public class Area {
    private int id;
    private String name;
    private int pid;
    @Generated(hash = 2069022254)
    public Area(int id, String name, int pid) {
        this.id = id;
        this.name = name;
        this.pid = pid;
    }
    @Generated(hash = 179626505)
    public Area() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPid() {
        return this.pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
}
