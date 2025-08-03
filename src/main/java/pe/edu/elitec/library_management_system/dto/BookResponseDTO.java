package pe.edu.elitec.library_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "📄 Respuesta con información completa del libro")
public class BookResponseDTO {
    @Schema(description = "ID único del libro", example = "1")
    private Long id;

    @Schema(description = "Título del libro", example = "Clean Code")
    private String name;

    @Schema(description = "Autor del libro", example = "Robert C. Martin")
    private String author;

    @Schema(description = "Código ISBN", example = "978-0132350884")
    private String isbn;

    @Schema(description = "Categoría del libro", example = "TECNOLOGIA")
    private String category;

    @Schema(description = "¿Está disponible?", example = "true")
    private Boolean available;

    @Schema(description = "Descripción del libro")
    private String description;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Última actualización")
    private LocalDateTime updateAt;

    @Schema(description = "Estado de disponibilidad en texto", example = "Disponible")
    private String availabilityStatus;

    @Schema(description = "Tiempo desde la creación", example = "Hace 2 días")
    private String timeAgo;

    public String getAvailabilityStatus() {
        return available != null && available ? "Disponible" : "No disponible";
    }

    public String getTimeAgo() {
        if (createdAt == null) return "Fecha no disponible";

        LocalDateTime now = LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(createdAt, now);

        long days = duration.toDays();
        if (days > 0) return "Hace " + days + " día" + (days > 1 ? "s" : "");

        long hours = duration.toHours();
        if (hours > 0) return "Hace " + hours + " hora" + (hours > 1 ? "s" : "");

        long minutes = duration.toMinutes();
        if (minutes > 0) return "Hace " + minutes + " minuto" + (minutes > 1 ? "s" : "");

        return "Hace unos segundos";
    }
}
