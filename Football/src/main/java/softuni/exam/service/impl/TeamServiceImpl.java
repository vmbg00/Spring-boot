package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.importDtos.xmlImport.TeamImportDto;
import softuni.exam.domain.dtos.importDtos.xmlImport.TeamImportRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final TeamRepository teamRepository;
    private final ValidatorUtil validatorUtil;
    private final static String TEAM_PATH = "src/main/resources/files/xml/teams.xml";
    private final PictureRepository pictureRepository;

    @Autowired
    public TeamServiceImpl(ModelMapper modelMapper, XmlParser xmlParser, TeamRepository teamRepository, ValidatorUtil validatorUtil, PictureRepository pictureRepository) {
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.teamRepository = teamRepository;
        this.validatorUtil = validatorUtil;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public String importTeams() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        TeamImportRootDto teamImportRootDto = this.xmlParser.parseXml(TeamImportRootDto.class, TEAM_PATH);

        for (TeamImportDto importDto : teamImportRootDto.getTeams()) {

            Optional<Picture> byUrl = this.pictureRepository.findByUrl(importDto.getPicture().getUrl());
            if (this.validatorUtil.isValid(importDto) && byUrl.isPresent()) {
                Team team = this.modelMapper.map(importDto, Team.class);



                this.teamRepository.saveAndFlush(team);
                sb.append(String.format("Successfully imported - %s",importDto.getName()))
                    .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid team").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public boolean areImported() {

        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {

        return String.join("", Files.readAllLines(Path.of(TEAM_PATH)));
    }
}
