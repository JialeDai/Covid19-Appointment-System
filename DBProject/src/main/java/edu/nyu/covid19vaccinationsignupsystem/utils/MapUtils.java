package edu.nyu.covid19vaccinationsignupsystem.utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class MapUtils {


    public static final double PI = 3.14159265358979324;
    // Equator radius (unit: meter)
    private static final double EARTH_RADIUS = 6378137;


    // {lat=40.710403, lng=-73.9912671}
    public static Map<String, Object> getGeoCodingInfoFromAddress(String address) throws URISyntaxException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + formatAddress(address) + "&key="+ PropertiesUtil.getProperty("google-map-apk");
        URI uri = new URI(baseUrl);
        ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {
        };
        ResponseEntity<Map<String, Object>> result = restTemplate.exchange(uri, HttpMethod.GET, null, typeRef);
        return (Map<String, Object>) ((Map<String, Object>) ((List<Map<String, Object>>) result.getBody().get("results")).get(0).get("geometry")).get("location");
    }

    private static String formatAddress(String address) {
        address = address.trim().replaceAll(" +", " ").replaceAll("\n", "").replaceAll(" ", "+");
        return address;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    // unit: mile
    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return Double.parseDouble(String.format("%.2f",s * 0.00062137119));
    }

    public static double getDistance(String from, String to) throws URISyntaxException, IOException {
        Map<String, Object> fromInfo = getGeoCodingInfoFromAddress(from);
        Map<String, Object> toInfo = getGeoCodingInfoFromAddress(to);
        return GetDistance((Double) fromInfo.get("lng"), (Double) fromInfo.get("lat"), (Double) toInfo.get("lng"), (Double) toInfo.get("lat"));
    }
}