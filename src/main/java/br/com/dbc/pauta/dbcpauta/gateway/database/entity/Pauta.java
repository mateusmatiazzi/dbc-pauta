package br.com.dbc.pauta.dbcpauta.gateway.database.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pauta")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @JoinColumn(name = "id_sessao_votacao", referencedColumnName = "id", unique = true)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private SessaoVotacao sessaoVotacao;
}
