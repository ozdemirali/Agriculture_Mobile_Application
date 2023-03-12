package model;

public class AgriculturalDisease {
    private String Id;
    private int AgriculturalProductId;
    private int DiseaseId;

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
