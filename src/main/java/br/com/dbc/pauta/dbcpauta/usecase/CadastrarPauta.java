package br.com.dbc.pauta.dbcpauta.usecase;

import br.com.dbc.pauta.dbcpauta.exception.BusinessException;
import br.com.dbc.pauta.dbcpauta.gateway.database.entity.Pauta;
import br.com.dbc.pauta.dbcpauta.gateway.database.repository.PautaRepositoryFacade;
import br.com.dbc.pauta.dbcpauta.http.domain.PautaDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class CadastrarPauta {

    private final PautaRepositoryFacade pautaRepositoryFacade;

    public Pauta executar(PautaDTO pautaDTO) {
        validaSeCamposEstaoCorretos(pautaDTO);
        return salvar(pautaDTO);
    }

    private void validaSeCamposEstaoCorretos(PautaDTO pautaDTO) {
        if (isNull(pautaDTO)) throw new BusinessException("É necessário os dados da pauta");

        if (isNull(pautaDTO.getNome())) throw new BusinessException("É obrigatório que a pauta tenha um nome");

        if (isNull(pautaDTO.getDescricao()))
            throw new BusinessException("É obrigatório que a pauta tenha uma descrição");

        if (nonNull(buscaPautaPorNome(pautaDTO))) throw new BusinessException("Já existe uma pauta com esse nome");
    }

    private Pauta salvar(PautaDTO pautaDTO) {
        Pauta pauta = gerarPauta(pautaDTO);
        pautaRepositoryFacade.save(pauta);

        return pauta;
    }

    private Pauta gerarPauta(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        pauta.setId(pautaDTO.getId());
        pauta.setNome(pautaDTO.getNome());
        pauta.setDescricao(pautaDTO.getDescricao());
        return pauta;
    }

    private Pauta buscaPautaPorNome(PautaDTO pautaDTO) {
        return pautaRepositoryFacade.findByNome(pautaDTO.getNome());
    }

}
