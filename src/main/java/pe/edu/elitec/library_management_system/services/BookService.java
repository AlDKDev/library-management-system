package pe.edu.elitec.library_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pe.edu.elitec.library_management_system.entities.BookEntity;
import pe.edu.elitec.library_management_system.repositories.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public BookEntity createBook(BookEntity book){
        return bookRepository.save(book);
    }

    public List<BookEntity> getAllBooks(){
        return bookRepository.findAll();
    }

    public Optional<BookEntity> getBookById(Long id){
        return bookRepository.findById(id);
    }

    public Optional<BookEntity> updateBook(Long id, BookEntity bookNew){
        Optional<BookEntity> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()){
            BookEntity existBook = optionalBook.get();

            existBook.setName(bookNew.getName());
            existBook.setAuthor(bookNew.getAuthor());
            existBook.setIsbn(bookNew.getIsbn());
            existBook.setCategory(bookNew.getCategory());
            existBook.setAvailable(bookNew.getAvailable());
            existBook.setDescription(bookNew.getDescription());

            BookEntity updateBook = bookRepository.save(existBook);

            return Optional.of(updateBook);
        }

        return Optional.empty();
    }

    public boolean deleteBook(Long id){
        if (bookRepository.existsById(id)){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getTotalBooks(){
        return bookRepository.count();
    }
}
