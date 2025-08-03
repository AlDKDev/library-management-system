package pe.edu.elitec.library_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "📝 DTO para crear o actualizar un libro")
public class BookRequestDTO {
    @NotBlank(message = "El nombre del libro es obligatorio")
    @Size(min = 10, max = 200, message = "El nombre debe tener entre 10 y 200 caracteres")
    @Schema(
            description = "Título del libro",
            example = "Clean Code: A Handbook of Agile Software Craftsmanship",
            minLength = 10,
            maxLength = 200
    )
    private String name;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre del autor debe tener entre 2 y 200 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s\\.\\-']+$",
            message = "El nombre del autor solo puede contener letras, espacios, puntos, guiones y apostrofes"
    )
    @Schema(
            description = "Nombre completo del autor",
            example = "Robert C. Martin",
            pattern = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s\\.\\-']+$"
    )
    private String author;

    @NotBlank(message = "El ISBN es obligatorio")
    @Pattern(
            regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "El ISBN debe tener un formato válido (ISBN-10 o ISBN-13)"
    )
    @Schema(
            description = "Código ISBN del libro (formato ISBN-10 o ISBN-13)",
            example = "978-0132350884",
            pattern = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
    )
    private String isbn;

    @NotBlank(message = "La categoría es obligatoria")
    @Pattern(
            regexp = "^(FICCION|NO_FICCION|CIENCIA|TECNOLOGIA|HISTORIA|ARTE|FILOSOFIA|LITERATURA|EDUCACION|REFERENCIA)$",
            message = "La categoría debe ser una de las siguientes: FICCION, NO_FICCION, CIENCIA, TECNOLOGIA, HISTORIA, ARTE, FILOSOFIA, LITERATURA, EDUCACION, REFERENCIA"
    )
    @Schema(
            description = "Categoría del libro",
            example = "TECNOLOGIA",
            allowableValues = {"FICCION", "NO_FICCION", "CIENCIA", "TECNOLOGIA", "HISTORIA", "ARTE", "FILOSOFIA", "LITERATURA", "EDUCACION", "REFERENCIA"}
    )
    private String category;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    @Schema(
            description = "Indica si el libro está disponible para préstamo",
            example = "true"
    )
    private Boolean available;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Schema(
            description = "Descripción opcional del libro",
            example = "Una guía completa para escribir código limpio y mantenible, con principios y prácticas recomendadas.",
            maxLength = 1000
    )
    private String description;
}
