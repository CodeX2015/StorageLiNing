package com.vipcartlining.vipcardlining;


import java.io.Serializable;

/**
 * Created by CodeX on 24.06.2015.
 */
public class Response implements Serializable{
    int status;
    String description;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
