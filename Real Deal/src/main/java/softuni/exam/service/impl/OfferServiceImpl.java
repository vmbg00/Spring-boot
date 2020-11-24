package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.OfferImportDto;
import softuni.exam.models.dtos.importDtos.OfferImportRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

@Service
public class OfferServiceImpl implements OfferService {

    private final XmlParser xmlParser;
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final static String OFFER_PATH = "src/main/resources/files/xml/offers.xml";
    private final ValidatorUtil validatorUtil;
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public OfferServiceImpl(XmlParser xmlParser, OfferRepository offerRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, CarRepository carRepository, SellerRepository sellerRepository) {
        this.xmlParser = xmlParser;
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(OFFER_PATH)));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        OfferImportRootDto importRootDto = this.xmlParser.parseXml(OfferImportRootDto.class, OFFER_PATH);
        for (OfferImportDto offerImportDto : importRootDto.getOffers()) {
            if (this.validatorUtil.isValid(offerImportDto)) {
                Offer offer = this.modelMapper.map(offerImportDto, Offer.class);

                Car car = this.carRepository.findById(offerImportDto.getCar().getId()).get();
                Seller seller = this.sellerRepository.findById(offerImportDto.getSeller().getId()).get();

                offer.setPictures(new HashSet<>(car.getPictures()));
                offer.setCar(car);
                offer.setSeller(seller);

                this.offerRepository.saveAndFlush(offer);
                sb.append(String.format("Successfully imported offer %s - %s", offer.getAddedOn(), offer.isHasGoldStatus()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid offer!").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
