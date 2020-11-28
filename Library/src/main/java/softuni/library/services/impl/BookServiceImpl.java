package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.BookImportDto;
import softuni.library.models.entities.Author;
import softuni.library.models.entities.Book;
import softuni.library.repositories.AuthorRepository;
import softuni.library.repositories.BookRepository;
import softuni.library.services.BookService;
import softuni.library.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper;
    private final Gson gson;
    private final BookRepository bookRepository;
    private final ValidatorUtil validatorUtil;
    private final static String BOOK_PATH = "src/main/resources/files/json/books.json";
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(ModelMapper modelMapper, Gson gson, BookRepository bookRepository, ValidatorUtil validatorUtil, AuthorRepository authorRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.bookRepository = bookRepository;
        this.validatorUtil = validatorUtil;
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean areImported() {
        return this.bookRepository.count() > 0;
    }

    @Override
    public String readBooksFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(BOOK_PATH)));
    }

    @Override
    public String importBooks() throws IOException {
        StringBuilder sb = new StringBuilder();
        BookImportDto[] bookImportDtos = this.gson.fromJson(readBooksFileContent(), BookImportDto[].class);
        for (BookImportDto bookImportDto : bookImportDtos) {
            if (this.validatorUtil.isValid(bookImportDto)) {
                Book book = this.modelMapper.map(bookImportDto, Book.class);

                Author author = this.authorRepository.findById(bookImportDto.getAuthor()).get();

                book.setAuthor(author);
                this.bookRepository.saveAndFlush(book);
                sb.append(String.format("Successfully imported Book: %s written in %s",bookImportDto.getName(), bookImportDto.getWritten()))
                .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid Book").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
