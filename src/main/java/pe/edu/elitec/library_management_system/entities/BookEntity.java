package pe.edu.elitec.library_management_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un libro en la biblioteca")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del libro", example = "1")
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre del libro es obligatorio")
    @Size(min = 10, max = 200, message = "El nombre debe tener entre 10 y 200 caracteres")
    @Schema(description = "Titulo del libro", example = "Cien años de soledad")
    private String name;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 200, message = "El nombre del autor no puede exceder de 200 caracteres")
    @Schema(description = "Autor del libro", example = "Gabriel García Marquez")
    private String author;

    @Column(nullable = false, unique = true, length = 17)
    @NotBlank(message = "El ISBN es obligatorio")
    @Pattern(
            regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "El ISBN debe tener un formato valido"
    )
    @Schema(description = "Código ISBN del libro", example = "978-8-123456-98-7")
    private String isbn;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La categoria es obligatoria")
    @Pattern(
            regexp = "^(FICCION|NO_FICCION|CIENCIA|TECNOLOGIA|HISTORIA|ARTE|FILOSOFIA|LITERATURA|EDUCACION|REFERENCIA)$",
            message = "La categoria debe ser una de las permitidas"
    )
    @Schema(description = "Categoria del libro",
            example = "LITERATURA",
            allowableValues = {"FICCION", "NO_FICCION", "CIENCIA", "TECNOLOGIA", "HISTORIA", "ARTE", "FILOSOFIA", "LITERATURA", "EDUCACION", "REFERENCIA"})
    private String category;

    @Column(nullable = false)
    @NotNull(message = "El estado de disponibilidad es obligatorio")
    @Schema(description = "Indica si el libro está disponible para préstamo", example = "true")
    private Boolean available = true;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Schema(description = "Descripción del libro", example = "Una obra maestra de la literatura latinoamericana")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    @Schema(description = "Fecha y hora de última actualización")
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        if (this.available == null){
            this.available = true;
        }
    }

    @PreUpdate
    protected void onUpdate(){
        this.updateAt = LocalDateTime.now();
    }

    public boolean isAvailable(){
        return this.available != null && this.available;
    }

    public void markAsUnavailable(){
        this.available = false;
        this.updateAt = LocalDateTime.now();
    }

    public void markAsAvailable(){
        this.available = true;
        this.updateAt = LocalDateTime.now();
    }
}
