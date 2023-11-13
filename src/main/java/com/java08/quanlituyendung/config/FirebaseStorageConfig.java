package com.java08.quanlituyendung.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseStorageConfig {

    @Value("${firebase.storage.bucket}")
    private String bucketName;

    @Bean
    public Storage firebaseStorage() throws IOException {
        InputStream serviceAccount = getClass().getResourceAsStream("/firebase-adminsdk.json");

        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();
    }

    @Bean
    public String firebaseBucketName() {
        return bucketName;
    }
}