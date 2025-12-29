package br.com.mali_social.social_meli.util;

import br.com.mali_social.social_meli.dto.PublicacaoDto;

public class Verificacao {

    public static boolean verificadorStrigs(String texto){
        if (texto == null) return false;
        return texto.matches(".*[@$#!].*");
    }
    public boolean verificaCadastroProduto (PublicacaoDto publicacaoDto){
        double precoMax = 10000000.00;
        boolean ret;
        if (publicacaoDto.getUser_id() == 0 || publicacaoDto.getUser_id() == null){
            ret = false;
        }
        else if (publicacaoDto.getDate() == null){
            ret = false;
        }
        else if (publicacaoDto.getCategory() == null){
            ret = false;
        }
        else if (publicacaoDto.getPrice() == null || publicacaoDto.getPrice() > precoMax){
            ret = false;
        }
        else if (publicacaoDto.getProduct().getProduct_id() == 0 || publicacaoDto.getProduct().getProduct_id() == null){
            ret = false;
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getProduct_name()) || publicacaoDto.getProduct().getProduct_name().length() > 40){
            ret = false;
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getType()) || publicacaoDto.getProduct().getType().length() > 15){
            ret = false;
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getBrand()) || publicacaoDto.getProduct().getBrand().length() > 25){
            ret = false;
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getColor()) || publicacaoDto.getProduct().getColor().length() > 15){
            ret = false;
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getNotes()) || publicacaoDto.getProduct().getNotes().length() > 80){
            ret = false;
        }

        ret = true;
        return ret;
    }


}
