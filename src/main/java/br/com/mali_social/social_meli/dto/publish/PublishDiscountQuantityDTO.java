package br.com.mali_social.social_meli.dto.publish;

public class PublishDiscountQuantityDTO {
    private Long user_id;
    private String user_name;
    private int  promo_products_count;


    public PublishDiscountQuantityDTO(Long user_id, String user_name, int promo_products_count) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.promo_products_count = promo_products_count;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int  getPromo_products_count() {
        return promo_products_count;
    }

    public void setPromo_products_count(int promo_products_count) {
        this.promo_products_count = promo_products_count;
    }

    @Override
    public String toString() {
        return "QuantidadePublicacaoDesconto{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", promo_products_count='" + promo_products_count + '\'' +
                '}';
    }
}
