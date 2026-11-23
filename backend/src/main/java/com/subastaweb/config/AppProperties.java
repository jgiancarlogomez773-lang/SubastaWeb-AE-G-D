package com.subastaweb.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Storage storage, Cors cors, BootstrapAdmin bootstrapAdmin) {

    public record Storage(String dir) {
    }

    public record Cors(List<String> allowedOrigins) {
    }

    public record BootstrapAdmin(String email, String password, String fullName) {
    }
}
