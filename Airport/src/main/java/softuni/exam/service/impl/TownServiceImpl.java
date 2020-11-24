package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.TownImportDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class TownServiceImpl implements TownService {

    private final Gson gson;
    private final TownRepository townRepository;
    private final static String TOWN_PATH = "src/main/resources/files/json/towns.json";
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public TownServiceImpl(Gson gson, TownRepository townRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.gson = gson;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }


    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(TOWN_PATH)));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        TownImportDto[] townImportDto = this.gson.fromJson(readTownsFileContent(),TownImportDto[].class);

        for (TownImportDto importDto : townImportDto) {

            Optional<Town> byName = this.townRepository.findByName(importDto.getName());
            if (this.validatorUtil.isValid(importDto) && byName.isEmpty()) {
                Town town = this.modelMapper.map(importDto, Town.class);

                this.townRepository.saveAndFlush(town);
                sb
                        .append(String.format("Successfully imported Town %s - %d",
                                importDto.getName(),
                                importDto.getPopulation()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid Town").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
