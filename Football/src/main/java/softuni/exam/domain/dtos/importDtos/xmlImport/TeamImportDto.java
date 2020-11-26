package softuni.exam.domain.dtos.importDtos.xmlImport;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamImportDto {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "picture")
    private PictureImportDto picture;

    public TeamImportDto() {
    }


    @NotNull
    @Length(min = 3, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public PictureImportDto getPicture() {
        return picture;
    }

    public void setPicture(PictureImportDto picture) {
        this.picture = picture;
    }
}
