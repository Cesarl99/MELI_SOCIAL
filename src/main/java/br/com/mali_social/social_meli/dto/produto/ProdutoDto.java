package br.com.mali_social.social_meli.dto.produto;

public class ProdutoDto {

    private String product_name;
    private String type;
    private String brand;
    private String color;
    private String notes;

    public ProdutoDto(String notes, String color, String brand, String type, String product_name) {
        this.notes = notes;
        this.color = color;
        this.brand = brand;
        this.type = type;
        this.product_name = product_name;
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
