package softuni.exam.domain.dtos.importDtos.jsonImport;

import com.google.gson.annotations.Expose;
import softuni.exam.domain.entities.Picture;

import java.util.List;

public class TeamImportJsonDto {

    @Expose
    private String name;
    @Expose
    private PictureImportDto picture;

    public TeamImportJsonDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureImportDto getPicture() {
        return picture;
    }

    public void setPicture(PictureImportDto picture) {
        this.picture = picture;
    }
}
