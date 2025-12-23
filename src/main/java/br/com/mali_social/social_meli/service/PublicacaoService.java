package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.PublicacaoDto;
import br.com.mali_social.social_meli.util.Verificacao;
import org.springframework.stereotype.Service;


@Service
public class PublicacaoService {

    public String salvarPublicacao(PublicacaoDto publicacaoDto){

        return(Verificacao.verificaCadastroProduto(publicacaoDto));

    }

}
