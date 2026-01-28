package br.com.rpg.model.combatente;

public class Arcanista extends Combatente {

    private int mana;
    private final int manaMax = 100;

    public Arcanista(String nome, int nivel) {
        super(nome, nivel, 80 + (nivel * 8));
        mana = manaMax;
    }

    @Override
    public String realizarEspecial(Combatente alvo) {
        if (mana < 30) {
            mana += 15;
            return "Sem mana! Concentrando... (+15 MP)";
        }
        mana -= 30;
        return "BOLA DE FOGO! " + alvo.receberDano(40 + nivel * 6);
    }

    @Override public String getNomeRecurso() { return "Mana"; }
    @Override public int getValorRecurso() { return mana; }
    @Override public int getMaxRecurso() { return manaMax; }
}

