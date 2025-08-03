package pe.edu.elitec.library_management_system.exception;

public class BookNotFoundException extends RuntimeException{

    private final Long bookId;

    public BookNotFoundException(Long bookId){
        super("Libro con ID " + bookId + " no fue encontrado");
        this.bookId = bookId;
    }

    public BookNotFoundException(String message){
        super(message);
        this.bookId = null;
    }

    public Long getBookId(){
        return bookId;
    }
}
