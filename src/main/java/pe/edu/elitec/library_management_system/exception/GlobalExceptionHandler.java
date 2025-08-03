package pe.edu.elitec.library_management_system.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pe.edu.elitec.library_management_system.dto.ErrorResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException{

    //Manejo de validaciones de campos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.warn("Errores de validación en {}: {}", request.getRequestURI(), ex.getMessage());

        List<ErrorResponseDTO.FieldErrorDto> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDTO.FieldErrorDto(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Datos de entrada inválidos");
        errorResponse.setMessage("Los datos enviados no cumplen con las validaciones requeridas");
        errorResponse.setFieldErrors(fieldErrors);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrorId(generateErrorId());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    //Manejo de validaciones de Constraints
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        log.warn("Violación de constraints en {}: {}", request.getRequestURI(), ex.getMessage());

        List<ErrorResponseDTO.FieldErrorDto> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ErrorResponseDTO.FieldErrorDto(
                        getFieldName(violation),
                        violation.getInvalidValue(),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Violación de restricciones");
        errorResponse.setMessage("Los datos no cumplen con las restricciones definidas");
        errorResponse.setFieldErrors(fieldErrors);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrorId(generateErrorId());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    //Manejo de Libro no encontrado
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookNotFoundException(
            BookNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Libro no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    //ISBN Duplicado
    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateIsbnException(
            DuplicateIsbnException ex,
            HttpServletRequest request) {

        log.warn("ISBN duplicado en {}: {}", request.getRequestURI(), ex.getMessage());

        List<ErrorResponseDTO.FieldErrorDto> fieldErrors = List.of(
                new ErrorResponseDTO.FieldErrorDto("isbn", ex.getIsbn(), ex.getMessage())
        );

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError("Conflicto de datos");
        errorResponse.setMessage("El ISBN ya existe en el sistema");
        errorResponse.setFieldErrors(fieldErrors);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrorId(generateErrorId());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    //Manejo de Categoria Invalida
    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCategoryException(
            InvalidCategoryException ex,
            HttpServletRequest request) {

        log.warn("Categoría inválida en {}: {}", request.getRequestURI(), ex.getMessage());

        List<ErrorResponseDTO.FieldErrorDto> fieldErrors = List.of(
                new ErrorResponseDTO.FieldErrorDto("category", ex.getCategory(), ex.getMessage())
        );

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Categoría inválida");
        errorResponse.setMessage("La categoría especificada no es válida");
        errorResponse.setFieldErrors(fieldErrors);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrorId(generateErrorId());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    //Error de integridad de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        log.error("Error de integridad de datos en {}: {}", request.getRequestURI(), ex.getMessage());

        String message = "Error de integridad en la base de datos";
        String errorDetail = ex.getMessage();

        // Detectar tipos específicos de violación
        if (errorDetail != null) {
            if (errorDetail.contains("isbn")) {
                message = "El ISBN ya existe en el sistema";
            } else if (errorDetail.contains("constraint")) {
                message = "Los datos violan restricciones de la base de datos";
            }
        }

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflicto de integridad",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    //JSON malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.warn("JSON malformado en {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Formato JSON inválido",
                "El cuerpo de la petición contiene JSON malformado o inválido",
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    //Tipo de argumento incorrecto
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        log.warn("Tipo de argumento incorrecto en {}: {}", request.getRequestURI(), ex.getMessage());

        String message = String.format("El parámetro '%s' debe ser de tipo %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Tipo de parámetro incorrecto",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    private String generateErrorId() {
        return "ERR-" + System.currentTimeMillis();
    }

    private String getFieldName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        String[] parts = propertyPath.split("\\.");
        return parts.length > 0 ? parts[parts.length - 1] : propertyPath;
    }
}
