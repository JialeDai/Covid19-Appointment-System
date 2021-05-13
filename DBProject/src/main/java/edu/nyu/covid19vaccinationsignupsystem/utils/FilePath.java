package edu.nyu.covid19vaccinationsignupsystem.utils;

import lombok.Data;

@Data
public class FilePath {
    private static FilePath instance;
    private String url;
    private FilePath() {}
    public static synchronized FilePath getInstance() {
        if (instance == null) {
            instance = new FilePath();
        }
        return instance;
    }
}
