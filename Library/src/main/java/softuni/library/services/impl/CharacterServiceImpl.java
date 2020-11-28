package softuni.library.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.CharacterImportDto;
import softuni.library.models.dtos.CharacterRootImportDto;
import softuni.library.models.entities.Character;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.CharacterRepository;
import softuni.library.services.CharacterService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CharacterServiceImpl implements CharacterService {

    private final XmlParser xmlParser;
    private final CharacterRepository characterRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final static String CHARACTER_PATH = "src/main/resources/files/xml/characters.xml";
    private final BookRepository bookRepository;

    @Autowired
    public CharacterServiceImpl(XmlParser xmlParser, CharacterRepository characterRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, BookRepository bookRepository) {
        this.xmlParser = xmlParser;
        this.characterRepository = characterRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean areImported() {
        return this.characterRepository.count() > 0;
    }

    @Override
    public String readCharactersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(CHARACTER_PATH)));
    }

    @Override
    public String importCharacters() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        CharacterRootImportDto characterImportDto = this.xmlParser.parseXml(CharacterRootImportDto.class, CHARACTER_PATH);
        for (CharacterImportDto importDto : characterImportDto.getCharacters()) {
            if (this.validatorUtil.isValid(importDto)) {
                Character character = this.modelMapper.map(importDto, Character.class);

                character.setBook(this.bookRepository.getOne(importDto.getBook().getId()));

                this.characterRepository.saveAndFlush(character);
                sb.append(String.format("Successfully import Character %s %s", importDto.getFirstName(), importDto.getLastName()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid Character")
                        .append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public String findCharactersInBookOrderedByLastNameDescendingThenByAge() {
        StringBuilder sb = new StringBuilder();
        Set<Character> characters = this.characterRepository.exportCharacters();
        for (Character character : characters) {
            sb
                    .append(String.format("Character name %s %s %s, age %d, in book %s"
                    ,character.getFirstName(),character.getMiddleName(), character.getLastName(),
                            character.getAge(),character.getBook().getName()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
