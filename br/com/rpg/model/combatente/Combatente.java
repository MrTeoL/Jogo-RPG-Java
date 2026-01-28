package br.com.rpg.model.combatente;

import br.com.rpg.model.enums.TipoAcao;
import br.com.rpg.util.RandomProvider;

import java.util.Random;

public abstract class Combatente {

    protected String nome;
    protected int vidaAtual;
    protected int vidaMaxima;
    protected int nivel;
    protected boolean defendendo;
    protected Random random;

    protected Combatente(String nome, int nivel, int vidaMaxima) {
        this.nome = nome;
        this.nivel = nivel;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.defendendo = false;
        this.random = RandomProvider.get();
    }

    public boolean estaVivo() {
        return vidaAtual > 0;
    }

    public void iniciarTurno() {
        defendendo = false;
    }

    public String executarAcao(TipoAcao acao, Combatente alvo) {

        if ((acao == TipoAcao.ATAQUE_BASICO || acao == TipoAcao.ESPECIAL) && alvo == null) {
            return "Nenhum alvo selecionado.";
        }

        return switch (acao) {
            case ATAQUE_BASICO -> realizarAtaqueBasico(alvo);
            case ESPECIAL -> realizarEspecial(alvo);
            case DEFENDER -> {
                defendendo = true;
                yield nome + " assumiu postura defensiva! (-50% dano recebido)";
            }
        };
    }

    protected String realizarAtaqueBasico(Combatente alvo) {
        int dano = 5 + (nivel * 2) + random.nextInt(5);
        return "Ataque Básico! " + alvo.receberDano(dano);
    }

    public String receberDano(int dano) {
        if (defendendo) dano /= 2;

        vidaAtual -= dano;
        if (vidaAtual < 0) vidaAtual = 0;

        return nome + " sofreu " + dano + " de dano." + (defendendo ? " (Bloqueado)" : "");
    }

    public void curar(int valor) {
        vidaAtual = Math.min(vidaAtual + valor, vidaMaxima);
    }

    public abstract String realizarEspecial(Combatente alvo);
    public abstract String getNomeRecurso();
    public abstract int getValorRecurso();
    public abstract int getMaxRecurso();

    public String getNome() { return nome; }
    public int getVidaAtual() { return vidaAtual; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getNivel() { return nivel; }
}
