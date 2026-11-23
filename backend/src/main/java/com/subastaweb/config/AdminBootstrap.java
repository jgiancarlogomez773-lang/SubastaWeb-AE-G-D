package com.subastaweb.config;

import com.subastaweb.application.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** RN-01: el ADMIN solo se crea por procedimiento controlado (variables de entorno), nunca por la API pública. */
@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final AppProperties props;
    private final AuthService authService;

    public AdminBootstrap(AppProperties props, AuthService authService) {
        this.props = props;
        this.authService = authService;
    }

    @Override
    public void run(ApplicationArguments args) {
        AppProperties.BootstrapAdmin admin = props.bootstrapAdmin();
        if (admin == null || !StringUtils.hasText(admin.email()) || !StringUtils.hasText(admin.password())) {
            return;
        }
        String fullName = StringUtils.hasText(admin.fullName()) ? admin.fullName() : "Administrador";
        if (authService.createAdminIfAbsent(fullName, admin.email(), admin.password())) {
            log.info("Administrador inicial creado.");
        }
    }
}
