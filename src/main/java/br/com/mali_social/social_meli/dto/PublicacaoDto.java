package br.com.mali_social.social_meli.dto;

public class PublicacaoDto {

    private Long user_id;
    private String date;
    private ProdutoDto product;
    private Integer category;
    private Double price;
    private boolean has_promo;
    private double discount;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ProdutoDto getProduct() {
        return product;
    }

    public void setProduct(ProdutoDto product) {
        this.product = product;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

}
