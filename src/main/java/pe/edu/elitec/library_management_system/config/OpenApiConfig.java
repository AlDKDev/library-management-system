package pe.edu.elitec.library_management_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestión de Biblioteca Digital - ELITEC")
                        .description("""
                                🎯**API RESTful para la administración de libros digitales**
                                
                                Esta API permite realizar operaciones CRUD completas sobre un catálogo de libros, incluyendo gestión de categorías, disponibilidad y búsquedas avanzadas.
                                
                                ### 🔧 Funcionalidades principales:
                                - ✅ Gestión completa de libros (CRUD)
                                - 📊 Consultas y estadísticas
                                - 🔍 Búsquedas por múltiples criterios
                                - ✨ Validaciones automáticas
                                - 📈 Monitoreo con Actuator
                                
                                ### 📖 Desarrollado con:
                                - Spring Boot 3.5.3
                                - Java 21
                                - Spring Data JPA
                                - PostgreSQL/H2
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo ELITEC - Desarrollo Backend")
                                .email("sistemas@elitec.edu.pe")
                                .url("https://elitec.edu.pe"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("🏠 Servidor de Desarrollo Local"),
                        new Server()
                                .url("https://library-api-elitec.railway.app")
                                .description("☁️ Servidor de Producción (Railway)")
                ))
                .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                        .description("📖 Documentación completa del curso")
                        .url("https://docs.elitec.edu.pe/library-api"));
    }
}