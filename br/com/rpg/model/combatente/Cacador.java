package br.com.rpg.model.combatente;

public class Cacador extends Combatente {

    public Cacador(String nome, int nivel) {
        super(nome, nivel, 100 + (nivel * 12));
    }

    @Override
    public String realizarEspecial(Combatente alvo) {
        if (random.nextBoolean()) {
            return "TIRO NA CABEÇA! " + alvo.receberDano((30 + nivel * 5) * 2) + " (CRÍTICO)";
        }
        return "Disparo falhou... " + alvo.receberDano(5);
    }

    @Override public String getNomeRecurso() { return "Munição"; }
    @Override public int getValorRecurso() { return 1; }
    @Override public int getMaxRecurso() { return 1; }
}

