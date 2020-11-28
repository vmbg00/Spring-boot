package softuni.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.library.services.AuthorService;
import softuni.library.services.BookService;
import softuni.library.services.CharacterService;
import softuni.library.services.LibraryService;

@Component
public class Runner implements CommandLineRunner {

    private final AuthorService authorService;
    private final BookService bookService;
    private final CharacterService characterService;
    private final LibraryService libraryService;

    @Autowired
    public Runner(AuthorService authorService, BookService bookService, CharacterService characterService, LibraryService libraryService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.characterService = characterService;
        this.libraryService = libraryService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.authorService.importAuthors());
        System.out.println(this.bookService.importBooks());
        System.out.println(this.characterService.importCharacters());
        System.out.println(this.libraryService.importLibraries());
        System.out.println(this.characterService.findCharactersInBookOrderedByLastNameDescendingThenByAge());
    }
}
