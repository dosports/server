package umc.dosports.Review.model;


public class GetReviewReq {
    private Object category;
    private Object height;
    private Object weight;
    private Object level;
    private Object max_price;
    private Object min_price;

    private Object sort_param;

    public GetReviewReq(Object category, Object height, Object weight, Object level, Object max_price, Object min_price){
        this.category = category;
        this.height = height;
        this.weight = weight;
        this.level = level;
        this.max_price = max_price;
        this.min_price = min_price;
    }
    public String getCategory() {
        if(this.category == null) return "";
        return category.toString();
    }
    public int getMax_price() {
        if(this.max_price == null) return -1;
        return Integer.parseInt(max_price.toString());
    }
    public int getMin_price() {
        if(this.min_price == null) return -1;
        return Integer.parseInt(min_price.toString());
    }
    public int getLevel() {
        if(this.level == null) return -1;
        return Integer.parseInt(level.toString());
    }
    public int getHeight() {
        if(this.height == null) return -1;
        return Integer.parseInt(height.toString());
    }
    public int getWeight() {
        if(this.weight == null) return -1;
        return Integer.parseInt(weight.toString());
    }
    public Object getSort_param() {
        return sort_param;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public void setLevel(Object level) {
        this.level = level;
    }

    public void setMax_price(Object max_price) {
        this.max_price = max_price;
    }

    public void setMin_price(Object min_price) {
        this.min_price = min_price;
    }

    public void setSort_param(Object sort_param) {
        this.sort_param = sort_param;
    }

}
