package edu.nyu.covid19vaccinationsignupsystem.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilTest {

    @Test
    void getProperty() throws IOException {
        System.out.println(PropertiesUtil.getProperty("google-map-apk"));
    }
}