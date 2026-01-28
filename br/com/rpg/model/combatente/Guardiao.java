package br.com.rpg.model.combatente;

public class Guardiao extends Combatente {

    private int vigor;
    private final int vigorMax = 100;

    public Guardiao(String nome, int nivel) {
        super(nome, nivel, 150 + (nivel * 25));
        this.vigor = Math.min(50 + (nivel * 5), vigorMax);
    }

    @Override
    public void iniciarTurno() {
        super.iniciarTurno();
        vigor = Math.min(vigor + 5, vigorMax);
    }

    @Override
    public String realizarEspecial(Combatente alvo) {
        if (vigor < 20) return "Sem vigor! " + realizarAtaqueBasico(alvo);
        vigor -= 20;
        return "ESMAGAR COM ESCUDO! " + alvo.receberDano(15 + nivel * 3);
    }

    @Override public String getNomeRecurso() { return "Vigor"; }
    @Override public int getValorRecurso() { return vigor; }
    @Override public int getMaxRecurso() { return vigorMax; }
}
