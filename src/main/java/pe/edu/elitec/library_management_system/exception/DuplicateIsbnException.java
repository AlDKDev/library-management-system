package pe.edu.elitec.library_management_system.exception;

public class DuplicateIsbnException extends RuntimeException {

   private final String isbn;

    public DuplicateIsbnException(String isbn) {
        super("Ya existe un libro registrado con el ISBN: " + isbn);
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }
}
