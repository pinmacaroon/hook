package com.github.pinmacaroon.dchook.util;

import com.github.pinmacaroon.dchook.Hook;
import com.github.zafarkhaja.semver.Version;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class VersionChecker {
    public static void checkVersion(){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://api.modrinth.com/v2/project/qJ9ZfKma/version"))
                    .build();

            HttpResponse<String> response = Hook.HTTPCLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            JsonArray body = JsonParser.parseString(response.body()).getAsJsonArray();
            if(status != 200){
                Hook.LOGGER.error("the version list couldn't be received, modrinth said sent a 404");
                return;
            }
            Optional<Version> remoteVersion = Version.tryParse(body.get(0).getAsJsonObject().get("version_number")
                    .getAsString());
            if(remoteVersion.isEmpty()){
                Hook.LOGGER.error("remote version number is invalid");
                return;
            }
            if(Hook.VERSION.withoutBuildMetadata().isLowerThan(remoteVersion.get().withoutBuildMetadata())){
                Hook.LOGGER.info("""
                        you are running an older version of the mod! please update to {}! \
                        link: {}""",
                        remoteVersion.get(),
                        body.get(0).getAsJsonObject()
                                .get("files").getAsJsonArray()
                                .get(0).getAsJsonObject()
                                .get("url").getAsString()
                );
            }
            else if(Hook.VERSION.withoutBuildMetadata().isHigherThan(remoteVersion.get().withoutBuildMetadata())){
                Hook.LOGGER.error("""
						!!!!!!!!!!!!!!!!!
						you are running an unreleased version! please do not use this unless you know what you are \
						doing
						!!!!!!!!!!!!!!!!!""");
            }
        } catch (Exception e) {
            Hook.LOGGER.error("{}:{}", e.getClass().getName(), e.getMessage());
        }
    }
}
