package br.com.dbc.pauta.dbcpauta.gateway.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessao_votacao")
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "tempo_de_votacao")
    private int tempoDeVotacao = 1;

    @Column(name = "qte_sim")
    private Long qteSim = 0L;

    @Column(name = "qte_nao")
    private Long qteNao = 0L;

    @JsonIgnore
    @OneToOne(mappedBy = "sessaoVotacao", fetch = FetchType.LAZY)
    private Pauta pauta;
}
