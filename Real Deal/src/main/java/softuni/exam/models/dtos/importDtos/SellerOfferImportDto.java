package softuni.exam.models.dtos.importDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerOfferImportDto {

    private int id;

    public SellerOfferImportDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
