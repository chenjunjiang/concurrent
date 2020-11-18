package com.chenjj.concurrent.metrics.plugin;

import com.codahale.metrics.health.HealthCheck;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RESTfulServiceHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://127.0.0.1:8080/xx/yy").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return Result.healthy("The RESTful API service work well.");
            }
        } catch (Exception e) {

        }
        return Result.unhealthy("Detected RESTful server is unhealthy.");
    }
}
