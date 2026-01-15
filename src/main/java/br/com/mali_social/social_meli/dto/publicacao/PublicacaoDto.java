package br.com.mali_social.social_meli.dto.publicacao;

import br.com.mali_social.social_meli.dto.produto.ProdutoDto;

public class PublicacaoDto {
    private Long user_id;
    private Long post_id;
    private String date;
    private ProdutoDto product;
    private Integer category;
    private Double price;
    private boolean has_promo;
    private double discount;

    public PublicacaoDto(Long postId, Long user_id, String date, ProdutoDto product, Integer category, Double price, boolean has_promo, double discount) {
        this.post_id = postId;
        this.user_id = user_id;
        this.date = date;
        this.product = product;
        this.category = category;
        this.price = price;
        this.has_promo = has_promo;
        this.discount = discount;
    }

    public PublicacaoDto() {
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

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

    public boolean isHas_promo() {
        return has_promo;
    }

    public void setHas_promo(boolean has_promo) {
        this.has_promo = has_promo;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    @Override
    public String toString() {
        return "PublicacaoDto{" +
                "post_id=" + post_id +
                ", user_id=" + user_id +
                ", date='" + date + '\'' +
                ", product=" + product +
                ", category=" + category +
                ", price=" + price +
                ", has_promo=" + has_promo +
                ", discount=" + discount +
                '}';
    }
}
