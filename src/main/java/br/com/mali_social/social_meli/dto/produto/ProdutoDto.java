package br.com.mali_social.social_meli.dto.produto;

public class ProdutoDto {

    private String product_name;
    private String type;
    private String brand;
    private String color;
    private String notes;
    private Long product_id;

    public ProdutoDto(String product_name, String notes, String color, Long product_id, String brand, String type) {
        this.product_name = product_name;
        this.notes = notes;
        this.color = color;
        this.product_id = product_id;
        this.brand = brand;
        this.type = type;
    }


    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    @Override
    public String toString() {
        return "ProdutoDto{" +
                ", product_name='" + product_name + '\'' +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

}
