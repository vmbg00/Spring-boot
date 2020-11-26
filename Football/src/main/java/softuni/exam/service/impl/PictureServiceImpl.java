package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.importDtos.xmlImport.PictureImportDto;
import softuni.exam.domain.dtos.importDtos.xmlImport.PictureImportRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PictureServiceImpl implements PictureService {

    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final PictureRepository pictureRepository;
    private final ValidatorUtil validatorUtil;
    private final static String PICTURE_PATH = "src/main/resources/files/xml/pictures.xml";

    @Autowired
    public PictureServiceImpl(ModelMapper modelMapper, XmlParser xmlParser, PictureRepository pictureRepository, ValidatorUtil validatorUtil) {
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.pictureRepository = pictureRepository;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public String importPictures() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        PictureImportRootDto pictureImportRootDto = this.xmlParser.parseXml(PictureImportRootDto.class, PICTURE_PATH);

        for (PictureImportDto importDto : pictureImportRootDto.getPictures()) {
            if (this.validatorUtil.isValid(importDto)) {
                Picture picture = this.modelMapper.map(importDto, Picture.class);

                this.pictureRepository.saveAndFlush(picture);
                sb.append(String.format("Successfully imported picture - %s",importDto.getUrl()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid picture")
                        .append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public boolean areImported() {

        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PICTURE_PATH)));
    }

}
