package softuni.library.models.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "library")
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryImportDto {

    @XmlElement
    private String name;
    @XmlElement
    private String location;
    @XmlElement
    private int rating;
    @XmlElement
    private CharacterBookImportDto book;

    public LibraryImportDto() {
    }

    @Length(min = 3)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 5)
    @NotNull
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Range(min = 1, max = 10)
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public CharacterBookImportDto getBook() {
        return book;
    }

    public void setBook(CharacterBookImportDto book) {
        this.book = book;
    }
}
