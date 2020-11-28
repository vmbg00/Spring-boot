package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.AuthorImportDto;
import softuni.library.models.entities.Author;
import softuni.library.repositories.AuthorRepository;
import softuni.library.services.AuthorService;
import softuni.library.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final static String AUTHOR_PATH = "src/main/resources/files/json/authors.json";

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper modelMapper, Gson gson, ValidatorUtil validatorUtil) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.authorRepository.count() > 0;
    }

    @Override
    public String readAuthorsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(AUTHOR_PATH)));
    }

    @Override
    public String importAuthors() throws IOException {
        StringBuilder sb = new StringBuilder();
        AuthorImportDto[] authorImportDto = this.gson.fromJson(readAuthorsFileContent(), AuthorImportDto[].class);
        for (AuthorImportDto importDto : authorImportDto) {
            if (this.validatorUtil.isValid(importDto)) {
                Author author = this.modelMapper.map(importDto, Author.class);

                this.authorRepository.saveAndFlush(author);
                sb.append(String.format("Successfully imported Author %s %s - %s",
                        importDto.getFirstName(), importDto.getLastName(),importDto.getBirthTown()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid Author").append(System.lineSeparator());
            }
        }


        return sb.toString();
    }
}
