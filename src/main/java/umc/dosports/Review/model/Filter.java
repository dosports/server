package umc.dosports.Review.model;

public class Filter {
    private String gender;
    private double height;
    private int weight;
    private level level;


    public umc.dosports.Review.model.level getLevel() {
        return level;
    }

    public void setLevel(umc.dosports.Review.model.level level) {
        this.level = level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
