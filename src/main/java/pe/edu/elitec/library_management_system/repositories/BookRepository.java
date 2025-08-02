package pe.edu.elitec.library_management_system.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.elitec.library_management_system.entities.BookEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByIsbn(String isbn);

    List<BookEntity> findByCategory(String category);

    List<BookEntity> findByAvailable(Boolean available);

    List<BookEntity> findByAuthorIgnoreCase(String author);

    List<BookEntity> findByNameContainingIgnoreCase(String name);

    //Cantidad de libros por categoria
        // Método JPQL
    @Query("SELECT COUNT(b) FROM BookEntity b WHERE b.category = :category1")
    long countByCategory(@Param("category1") String category);

        // Método Nativo
    @Query(value = "SELECT category, COUNT(*) as count FROM book GROUP BY category ORDER BY count DESC",
        nativeQuery = true)
    List<Object[]> getCategoryStatistics();

    //Verificar si existe un libro con el ISBN, excluyendo un ID
    @Query("SELECT COUNT(b) FROM BookEntity b WHERE b.isbn = :isbn AND b.id != :id")
    boolean existsByIsbnAndIdNot(@Param("isbn") String isbn, @Param("id") Long id);

    //Buscar los libros ordenados por fecha de registro
    @Query("SELECT b FROM BookEntity b ORDER BY b.createdAt DESC")
    Page<BookEntity> findByRecentBooks(Pageable pageable);

    //Busqueda avanzada de libros por multiples criterios
    @Query("SELECT b FROM BookEntity b WHERE " +
            "(:searchTerm IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "OR LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))" +
            "AND (:category IS NULL OR b.category = :category)" +
            "AND (:available IS NULL OR b.available = :available)")
    Page<BookEntity> findBooksWithFilters (@Param("searchTerm") String searchTerm,
                                           @Param("category") String category,
                                           @Param("available") String available, Pageable pageable);
}