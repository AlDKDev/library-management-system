package pe.edu.elitec.library_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "🚨 Respuesta estándar para errores de la API")
public class ErrorResponseDTO {
    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Mensaje de error principal", example = "Datos de entrada inválidos")
    private String error;

    @Schema(description = "Descripción detallada del error", example = "Los datos enviados no cumplen con las validaciones requeridas")
    private String message;

    @Schema(description = "Lista de errores específicos de validación")
    private List<FieldErrorDto> fieldErrors;

    @Schema(description = "Endpoint donde ocurrió el error", example = "/api/books")
    private String path;

    @Schema(description = "Timestamp del error")
    private LocalDateTime timestamp;

    @Schema(description = "ID único para tracking del error", example = "ERR-2024-001")
    private String errorId;

    public ErrorResponseDTO(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.errorId = generateErrorId();
    }

    private String generateErrorId() {
        return "ERR-" + System.currentTimeMillis();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Error específico de un campo")
    public static class FieldErrorDto {

        @Schema(description = "Nombre del campo con error", example = "name")
        private String field;

        @Schema(description = "Valor que causó el error", example = "")
        private Object rejectedValue;

        @Schema(description = "Mensaje de error específico", example = "El nombre del libro es obligatorio")
        private String message;
    }
}
