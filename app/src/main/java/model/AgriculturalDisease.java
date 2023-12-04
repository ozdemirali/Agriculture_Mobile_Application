package model;

public class AgriculturalDisease {
    private String Id;
    private int AgriculturalProductId;
    private int DiseaseId;
    private String Not;
    private String ImageName;

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getNot() {
        return Not;
    }

    public void setNot(String not) {
        Not = not;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getAgriculturalProductId() {
        return AgriculturalProductId;
    }

    public void setAgriculturalProductId(int agriculturalProductId) {
        AgriculturalProductId = agriculturalProductId;
    }

    public int getDiseaseId() {
        return DiseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        DiseaseId = diseaseId;
    }
}
