package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.SellerImportDto;
import softuni.exam.models.dtos.importDtos.SellerImportRootDto;
import softuni.exam.models.entities.Rating;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {

    private final XmlParser xmlParser;
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final static String SELLER_PATH = "src/main/resources/files/xml/sellers.xml";
    private final ValidatorUtil validatorUtil;

    @Autowired
    public SellerServiceImpl(XmlParser xmlParser, SellerRepository sellerRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.xmlParser = xmlParser;
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(SELLER_PATH)));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        SellerImportRootDto sellerImportDto = this.xmlParser.parseXml(SellerImportRootDto.class, SELLER_PATH);

        for (SellerImportDto importDto : sellerImportDto.getSellers()) {

            Optional<Seller> byEmail = this.sellerRepository.findByEmail(importDto.getEmail());
            if (this.validatorUtil.isValid(importDto) && byEmail.isEmpty()) {
                Seller seller = this.modelMapper.map(importDto, Seller.class);
                try {
                    seller.setRating(Rating.valueOf(importDto.getRating()));
                } catch (Exception e){
                    sb.append("Invalid seller").append(System.lineSeparator());
                    continue;
                }

                this.sellerRepository.saveAndFlush(seller);
                sb.append(String.format("Successfully imported seller - %s - %s",
                        importDto.getLastName(), importDto.getEmail()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid seller").append(System.lineSeparator());
            }
        }


        return sb.toString();
    }
}
