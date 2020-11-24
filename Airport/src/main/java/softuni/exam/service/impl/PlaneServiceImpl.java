package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.PlaneImportDto;
import softuni.exam.models.dtos.importDtos.PlaneImportRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PlaneServiceImpl implements PlaneService {

    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final PlaneRepository planeRepository;
    private final static String PLANE_PATH = "src/main/resources/files/xml/planes.xml";
    private final ValidatorUtil validatorUtil;

    @Autowired
    public PlaneServiceImpl(XmlParser xmlParser, ModelMapper modelMapper, PlaneRepository planeRepository, ValidatorUtil validatorUtil) {
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.planeRepository = planeRepository;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PLANE_PATH)));
    }

    @Override
    public String importPlanes() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        PlaneImportRootDto planeImportRootDtos = this.xmlParser.parseXml(PlaneImportRootDto.class, PLANE_PATH);

        for (PlaneImportDto importDto : planeImportRootDtos.getPlanes()) {

            Optional<Plane> byRegisterNumber = this.planeRepository.findByRegisterNumber(importDto.getRegisterNumber());
            if (this.validatorUtil.isValid(importDto) && byRegisterNumber.isEmpty()) {
                Plane plane = this.modelMapper.map(importDto, Plane.class);

                this.planeRepository.saveAndFlush(plane);
                sb.append(String.format("Successfully imported Plane %s", importDto.getRegisterNumber()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid plane")
                        .append(System.lineSeparator());
            }

        }

        return sb.toString();
    }
}
