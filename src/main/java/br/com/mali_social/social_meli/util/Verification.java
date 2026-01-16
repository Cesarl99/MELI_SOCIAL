package br.com.mali_social.social_meli.util;

import br.com.mali_social.social_meli.dto.publish.PublishDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Verification {

    public static boolean checkStrigs(String text){
        if (text == null) return false;
        return text.matches(".*[@$#!].*");
    }

    public void checkPublicationRegistration (PublishDTO publishDTO){
        System.out.println("Dentro do verifica Cadastro Produto");
        double priceMax = 10000000.00;
        String ret = "";
        if (publishDTO.getUser_id() == 0 || publishDTO.getUser_id() == null){
            ret = "O id do usuario não pode estar vazio / id deve ser maior que zero";
        }
        else if (publishDTO.getDate() == null){
            ret = "A Data não pode estar vazia.";
        }
        else if (publishDTO.getCategory() == null){
            ret = "O campo Categoria não pode estar vazio.";
        }
        else if (publishDTO.getPrice() == null || publishDTO.getPrice() > priceMax){
            ret = "O campo Produto não pode estar vazio / O preço máximo por produto é de 10.000.000.";
        }
        else if (Verification.checkStrigs(publishDTO.getProduct().getProduct_name()) || publishDTO.getProduct().getProduct_name().length() > 40){
            ret = "O campo Nome do produto não pode estar vazio / O comprimento não pode exceder 40 caracteres / O campo Nome do produto não pode conter caracteres especiais.";
        }
        else if (Verification.checkStrigs(publishDTO.getProduct().getType()) || publishDTO.getProduct().getType().length() > 15){
            ret = "O campo Tipo não pode estar vazio. / O comprimento não pode exceder 15 caracteres. / O campo Tipo não pode conter caracteres especiais.";
        }
        else if (Verification.checkStrigs(publishDTO.getProduct().getBrand()) || publishDTO.getProduct().getBrand().length() > 25){
            ret = "O campo Marca não pode estar vazio. / O comprimento não pode exceder 25 caracteres. / O campo não pode conter caracteres especiais." ;
        }
        else if (Verification.checkStrigs(publishDTO.getProduct().getColor()) || publishDTO.getProduct().getColor().length() > 15){
            ret = "O campo Marca não pode estar vazio. / O comprimento não pode exceder 15 caracteres. / O campo não pode conter caracteres especiais.";
        }
        else if (Verification.checkStrigs(publishDTO.getProduct().getNotes()) || publishDTO.getProduct().getNotes().length() > 80){
            ret = "O campo Marca não pode estar vazio. / O comprimento não pode exceder 80 caracteres. / O campo não pode conter caracteres especiais.";
        }
        System.out.println("valor do ret" + ret);
        if (!ret.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ret);
        }
    }

    public void checkUserId (long userId){
        if (userId <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O id não pode estar vazio. id deve ser maior que zero.");
        }
    }


}
