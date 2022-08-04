package umc.dosports.Review.model;


public class Filter {
    private Object category;
    private Object height;
    private Object weight;
    private Object level;
    private Object max_price;
    private Object min_price;

    public Filter(Object category, Object height, Object weight, Object level, Object max_price, Object min_price){
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

    public String makeFilterQuery(){
        String findQuery = "select r.reviewIdx from review as r "+
                "where r.gender = ? and r.sports = ? ";
        if(this.category != null) findQuery += "and r.category = ? ";
        else findQuery += "and ? ";
        if(this.height != null) findQuery += "and r.height between ? and ? ";
        else findQuery += "and ? and ? ";
        if(this.weight != null) findQuery += "and r.weight between ? and ? ";
        else findQuery += "and ? and ? ";
        if(this.level != null) findQuery += "and r.level = ? ";
        else findQuery += "and ? ";
        if(this.min_price != null && this.max_price != null) findQuery += "and r.price between ? and ? ";
        else {
            if(this.min_price != null) findQuery += "and r.price >= ? ";
            else findQuery += "and ? ";
            if(this.max_price != null) findQuery += "and r.price <= ? ";
            else findQuery += "and ? ";
        }
        return findQuery;
    }

    public void printFilter(){
        System.out.print(this.makeFilterQuery());
        System.out.print(this.category+ " ");
        System.out.print(this.height+ " ");
        System.out.print(this.weight+ " ");
        System.out.print(this.min_price+ " ");
        System.out.print(this.max_price+ " ");
        System.out.println(this.level);
    }
}
