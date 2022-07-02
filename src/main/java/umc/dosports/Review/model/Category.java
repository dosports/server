package umc.dosports.Review.model;

enum Sports {
    gym, pilates, tennis, swim
}

enum Part {
    top, bottom, skirt, shoe, misc
}

enum Brand {
    nike, adidas
}

public class Category {
    private Sports sports;
    private Part part;
    private Brand brand;

    public Sports getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = Sports.valueOf(sports);
    }

    public Part getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = Part.valueOf(part);
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = Brand.valueOf(brand);
    }

    public void setCategory(String sports, String part, String brand){
        this.setSports(sports);
        this.setPart(part);
        this.setBrand(brand);
    }


    public int getSportsAsInt() {
        return sports.ordinal();
    }
    public int getPartAsInt() {
        return part.ordinal();
    }
    public int getBrandAsInt() {
        return brand.ordinal();
    }
}
