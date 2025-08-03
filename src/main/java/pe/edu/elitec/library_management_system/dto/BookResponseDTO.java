package pe.edu.elitec.library_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}
