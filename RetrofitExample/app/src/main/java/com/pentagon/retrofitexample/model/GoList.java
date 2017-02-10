package com.pentagon.retrofitexample.model;

import java.io.Serializable;

/**
 * Created by RayChongJH on 8/2/17.
 */

public class GoList implements Serializable{
    String id;
    String name;
    String description;
    String photoReference;
    boolean isBookmark;
    boolean isFollowed;
    boolean isDivider;
    boolean isPreloader = false;
    boolean isHeader = false;
    int placeCount;
    int vote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public int getVote() {
        return vote;
    }
    public void setVote(int vote) {
        this.vote = vote;
    }


    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public boolean isDivider() {
        return isDivider;
    }

    public void setDivider(boolean divider) {
        isDivider = divider;
    }

    public boolean isPreloader() {
        return isPreloader;
    }

    public void setPreloader(boolean preloader) {
        isPreloader = preloader;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
