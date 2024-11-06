package com.application.prueba.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "API-REST-PRUEBA",
                                description = "The application provides an API to manage a list of franchises",
                                termsOfService = "www.franquiciasprueba.com/terminos_y_servicios",
                                version = "1.0.0",
                                contact = @Contact(
                                        name = "Steven Rodriguez",
                                        url = "www.franquiciasprueba.com/contact",
                                        email = "stevenrodriguezb34@gmail.com"
                                    ), license = @License(
                                            name = "Standard Sotfware Use License for franquiciasprueba",
                                            url = "www.franquiciasprueba.com/license"
                                    )
                                ), servers = {
                                            @Server(
                                                    description = "DEV_SERVER",
                                                    url = "http://localhost:8080"
                                            )
                                    }
                            )

public class SwaggerConfig {}
