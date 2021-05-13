package edu.nyu.covid19vaccinationsignupsystem.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

class MapUtilsTest {

    @Test
    void getGeoCodingInfoFromAddress() throws URISyntaxException, IOException {
//        System.out.println(MapUtils.getGeoCodingInfoFromAddress("225 Cherry St, New York NY"));
        System.out.println(MapUtils.getGeoCodingInfoFromAddress("350 W 88th st, New York, NY"));
    }

    @Test
    void getDistance() throws URISyntaxException, IOException {
        System.out.println(MapUtils.getDistance("225 cherry st, New York, NY, 10002","350 W 88th st, New York, NY 10024"));
    }
}