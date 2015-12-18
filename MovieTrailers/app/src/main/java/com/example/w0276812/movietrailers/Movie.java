package com.example.w0276812.movietrailers;

/**
 * Created by w0276812 on 12/7/2015.
 */
public class Movie {
    private int id;
    private String title;
    private String description;
    private String alias;
    private String url;
    private int rating;

    public Movie(int id, String ttl, String desc, String als, String ul, int rtg) {
        this.id = id;
        title = ttl;
        description = desc;
        alias = als;
        url = ul;
        rating = rtg;
    }

    public int getId() { return id; }

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

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
