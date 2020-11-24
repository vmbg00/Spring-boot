package softuni.exam.models.dtos.importDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerImportRootDto {

    @XmlElement(name = "seller")
    private List<SellerImportDto> sellerImportDtos;

    public SellerImportRootDto() {
    }

    public List<SellerImportDto> getSellers() {
        return sellerImportDtos;
    }

    public void setSellers(List<SellerImportDto> sellers) {
        this.sellerImportDtos = sellers;
    }
}
