package pe.edu.elitec.library_management_system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.elitec.library_management_system.entities.BookEntity;
import pe.edu.elitec.library_management_system.services.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Tag(name = "📚 Gestión de Libros",
    description = "Operaciones CRUD para la administración del catálogo de libros")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @Operation(
            summary = "➕ Crear un nuevo libro",
            description = """
        Registra un nuevo libro en el catálogo de la biblioteca.
        
        ### ✅ Validaciones automáticas:
        - ISBN único en el sistema
        - Nombre entre 10-200 caracteres
        - Categoría válida según enum
        - Formato ISBN válido
        
        ### 📋 Datos requeridos:
        - Nombre del libro (obligatorio)
        - Autor (obligatorio)
        - ISBN (obligatorio y único)
        - Categoría (obligatorio)
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "✅ Libro creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookEntity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌ Datos de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "⚠ ISBN ya existe en el sistema",
                    content = @Content(
                            mediaType = "application/json")
            )
    })
    public ResponseEntity<BookEntity> createBook(
            @Parameter(
                    description =  "📖 Datos del libro a crear",
                    required = true,
                    schema = @Schema(implementation = BookEntity.class)
            )
            @Valid
            @RequestBody BookEntity book){
        BookEntity newBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @GetMapping
    @Operation(
            summary = "📋 Obtener todos los libros",
            description = """
        Retorna la lista completa de libros registrados en el catálogo.
        
        ### 📊 Información incluida:
        - Datos básicos del libro
        - Estado de disponibilidad
        - Fechas de creación y actualización
        - Categoría y descripción
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Lista de libros obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookEntity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Error interno del servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    public List<BookEntity> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "🔍 Buscar libro por ID",
            description = """
        Busca y retorna un libro específico usando su identificador único.
        
        ### 🎯 Casos de uso:
        - Consultar detalles específicos de un libro
        - Verificar disponibilidad antes de préstamo
        - Obtener información completa para edición
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Libro encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookEntity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "📭 Libro no encontrado",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<BookEntity> getBookById(
            @Parameter(
                    description = "🆔 ID único del libro",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable Long id){
        Optional<BookEntity> book = bookService.getBookById(id);

        if (book.isPresent()){
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookEntity> updateBook(@PathVariable Long id, @RequestBody BookEntity book){
        Optional<BookEntity> newBook = bookService.updateBook(id, book);
        if (newBook.isPresent()){
            return ResponseEntity.ok(newBook.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        boolean deleted = bookService.deleteBook(id);

        if (deleted){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    public long getTotalBooks(){
        return bookService.getTotalBooks();
    }
}
