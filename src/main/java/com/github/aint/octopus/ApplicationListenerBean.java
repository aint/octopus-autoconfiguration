package com.github.aint.octopus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApplicationListenerBean implements ApplicationListener {

    private final String octopusUrl;
    private final DependencyJson json;

    public ApplicationListenerBean(String octopusUrl, DependencyJson json) {
        this.octopusUrl = octopusUrl;
        this.json = json;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            try {
                sendPostRequest(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event instanceof ContextStoppedEvent) {
            try {
                DependencyJson depJson = new DependencyJson(DependencyJson.EventType.DELETE, json.getServiceName(), "api", null);
                sendPostRequest(depJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPostRequest(DependencyJson json) throws IOException {
        URL url = new URL(octopusUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        byte[] out = new ObjectMapper().writeValueAsBytes(json);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try (OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
    }

}
