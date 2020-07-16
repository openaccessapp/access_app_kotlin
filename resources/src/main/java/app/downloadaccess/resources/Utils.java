package app.downloadaccess.resources;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Utils {
    public static String getJwtToken(Context context) {
        String token1 = null;
        String token2 = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open("jwt_key.txt"), StandardCharsets.UTF_8))) {

            // do reading, usually loop until end of file reading
            token1 = reader.readLine();
            token2 = reader.readLine();
        } catch (IOException e) {
            //log the exception
        }
        //log the exception

        String jsontoken = "";
        if (token1 != null) {
            String jwt = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, token1.getBytes())
                    .setPayload("demo")
                    .compact();
            jsontoken = "Bearer " + jwt;
        }
        if (token2 != null) {
            String jwt = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, token2.getBytes())
                    .setPayload("demo")
                    .compact();
            jsontoken = jsontoken + " " + jwt;
        }
        System.out.println(jsontoken);
        return jsontoken;
    }
}
