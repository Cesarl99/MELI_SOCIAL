package br.com.mali_social.social_meli.util;

import br.com.mali_social.social_meli.dto.publicacao.PublicacaoDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Verificacao {

    public static boolean verificadorStrigs(String texto){
        if (texto == null) return false;
        return texto.matches(".*[@$#!].*");
    }

    public void verificaCadastroProduto (PublicacaoDto publicacaoDto){
        double precoMax = 10000000.00;
        String ret = "";
        if (publicacaoDto.getUser_id() == 0 || publicacaoDto.getUser_id() == null){
            ret = "O id do usuario não pode estar vazio / id deve ser maior que zero";
        }
        else if (publicacaoDto.getDate() == null){
            ret = "A Data não pode estar vazia.";
        }
        else if (publicacaoDto.getCategory() == null){
            ret = "O campo Categoria não pode estar vazio.";
        }
        else if (publicacaoDto.getPrice() == null || publicacaoDto.getPrice() > precoMax){
            ret = "O campo Produto não pode estar vazio / O preço máximo por produto é de 10.000.000.";
        }
        else if (publicacaoDto.getProduct().getProduct_id() == 0 || publicacaoDto.getProduct().getProduct_id() == null){
            ret = "O id do Produto não pode estar vazio / id do produto deve ser maior que zero.";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getProduct_name()) || publicacaoDto.getProduct().getProduct_name().length() > 40){
            ret = "O campo Nome do produto não pode estar vazio / O comprimento não pode exceder 40 caracteres / O campo Nome do produto não pode conter caracteres especiais.";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getType()) || publicacaoDto.getProduct().getType().length() > 15){
            ret = "O campo Tipo não pode estar vazio. / O comprimento não pode exceder 15 caracteres. / O campo Tipo não pode conter caracteres especiais.";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getBrand()) || publicacaoDto.getProduct().getBrand().length() > 25){
            ret = "O campo Marca não pode estar vazio. / O comprimento não pode exceder 25 caracteres. / O campo não pode conter caracteres especiais." ;
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getColor()) || publicacaoDto.getProduct().getColor().length() > 15){
            ret = "O campo Marca não pode estar vazio. / O comprimento não pode exceder 15 caracteres. / O campo não pode conter caracteres especiais.";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getNotes()) || publicacaoDto.getProduct().getNotes().length() > 80){
            ret = "O campo Marca não pode estar vazio. / O comprimento não pode exceder 80 caracteres. / O campo não pode conter caracteres especiais.";
        }

        if (!ret.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ret);
        }
    }

    public void verificaIdUsuario (long userId){
        if (userId <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O id não pode estar vazio. id deve ser maior que zero.");
        }
    }


}
