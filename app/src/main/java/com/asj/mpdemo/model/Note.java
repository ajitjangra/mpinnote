package com.asj.mpdemo.model;

import android.arch.persistence.room.Entity;


import java.io.Serializable;

@Entity
public class Note implements Serializable {

  private String id;
  private String title;
  private String description;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
