package softuni.library.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.LibraryImportDto;
import softuni.library.models.dtos.LibraryRootImportDto;
import softuni.library.models.entities.Book;
import softuni.library.models.entities.Library;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.LibraryRepository;
import softuni.library.services.LibraryService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final XmlParser xmlParser;
    private final LibraryRepository libraryRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final static String LIBRARY_PATH = "src/main/resources/files/xml/libraries.xml";
    private final BookRepository bookRepository;

    public LibraryServiceImpl(XmlParser xmlParser, LibraryRepository libraryRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, BookRepository bookRepository) {
        this.xmlParser = xmlParser;
        this.libraryRepository = libraryRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean areImported() {
        return this.libraryRepository.count() > 0;
    }

    @Override
    public String readLibrariesFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(LIBRARY_PATH)));
    }

    @Override
    public String importLibraries() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        LibraryRootImportDto libraryRootImportDto = this.xmlParser.parseXml(LibraryRootImportDto.class, LIBRARY_PATH);
        for (LibraryImportDto importDto : libraryRootImportDto.getLibraries()) {
            Optional<Library> byName = this.libraryRepository.findByName(importDto.getName());
            if (this.validatorUtil.isValid(importDto) && byName.isEmpty()) {
                Library library = this.modelMapper.map(importDto, Library.class);
                library.setReating(importDto.getRating());
                library.setLocation(importDto.getLocation());
                Set<Book> bookSet = new HashSet<>();
                bookSet.add(this.bookRepository.getOne(importDto.getBook().getId()));
                library.setBooks(bookSet);

                this.libraryRepository.saveAndFlush(library);
                sb.append(String.format("Successfully imported Library: %s - %s", importDto.getName(), importDto.getLocation()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid Library")
                        .append(System.lineSeparator());
            }
        }


        return sb.toString();
    }
}
