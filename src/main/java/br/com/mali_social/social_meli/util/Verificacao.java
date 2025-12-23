package br.com.mali_social.social_meli.util;

import br.com.mali_social.social_meli.dto.PublicacaoDto;

public class Verificacao {

    public static boolean verificadorStrigs(String texto){
        if (texto == null) return false;
        return texto.matches(".*[@$#!].*");
    }
    public static String verificaCadastroProduto (PublicacaoDto publicacaoDto){
        double precoMax = 10000000.00;
        if (publicacaoDto.getUser_id() == 0 || publicacaoDto.getUser_id() == null){
            return "PRODUTO NÄO CADASTRADA - ERRO 0";
        }
        else if (publicacaoDto.getDate() == null){
            return "PRODUTO NÁO CADASTRADA - ERRO 1";
        }
        else if (publicacaoDto.getCategory() == null){
            return "PRODUTO NÁO CADASTRADA - ERRO 2";
        }
        else if (publicacaoDto.getPrice() == null || publicacaoDto.getPrice() > precoMax){
            return "PRODUTO NÁO CADASTRADA - ERRO 3";
        }
        else if (publicacaoDto.getProduct().getProduct_id() == 0 || publicacaoDto.getProduct().getProduct_id() == null){
            return "PRODUTO NÄO CADASTRADA - ERRO 4";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getProduct_name()) || publicacaoDto.getProduct().getProduct_name().length() > 40){
            return "PRODUTO NÄO CADASTRADA - ERRO 5";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getType()) || publicacaoDto.getProduct().getType().length() > 15){
            return "PRODUTO NÄO CADASTRADA - ERRO 6";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getBrand()) || publicacaoDto.getProduct().getBrand().length() > 25){
            return "PRODUTO NÄO CADASTRADA - ERRO 7";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getColor()) || publicacaoDto.getProduct().getColor().length() > 15){
            return "PRODUTO NÄO CADASTRADA - ERRO 7";
        }
        else if (Verificacao.verificadorStrigs(publicacaoDto.getProduct().getNotes()) || publicacaoDto.getProduct().getNotes().length() > 80){
            return "PRODUTO NÄO CADASTRADA - ERRO 8";
        }

        return "CADASTRO REALZIADO COM SUCESSO";
    }


}
