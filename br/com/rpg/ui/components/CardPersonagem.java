package br.com.rpg.ui.components;

import br.com.rpg.model.combatente.*;
import br.com.rpg.ui.theme.Estilos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class CardPersonagem extends JPanel {

    private final Combatente combatente;
    private final JProgressBar barVida;
    private final JProgressBar barRecurso;
    private final JLabel lblNome;
    private JButton btnAcao;

    private final Font fonteNormal;
    private final Font fonteMorta;

    public CardPersonagem(Combatente combatente, boolean isJogador, Runnable onAcaoClick) {
        this.combatente = combatente;

        fonteNormal = new Font("SansSerif", Font.BOLD, 14);

        Map<TextAttribute, Object> atributos = new HashMap<>(fonteNormal.getAttributes());
        atributos.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        fonteMorta = new Font(atributos);

        setLayout(new BorderLayout(5, 5));
        setBackground(Estilos.FUNDO_CARD);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(220, 110));

        lblNome = new JLabel();
        lblNome.setHorizontalAlignment(SwingConstants.CENTER);
        lblNome.setForeground(Estilos.TEXTO_CLARO);
        lblNome.setFont(fonteNormal);

        barVida = criarBarra(Estilos.BARRA_VIDA);
        barRecurso = criarBarra(
                combatente instanceof Arcanista
                        ? Estilos.BARRA_MANA
                        : Estilos.BARRA_VIGOR
        );

        JPanel painelBarras = new JPanel(new GridLayout(2, 1, 0, 5));
        painelBarras.setOpaque(false);
        painelBarras.add(barVida);

        if (!(combatente instanceof Cacador)) {
            painelBarras.add(barRecurso);
        }

        if (isJogador) {
            btnAcao = new JButton("AÇÕES");
            estilizarBotao(btnAcao);
            btnAcao.addActionListener(e -> onAcaoClick.run());
            btnAcao.setEnabled(false);
            add(btnAcao, BorderLayout.EAST);
        }

        add(lblNome, BorderLayout.NORTH);
        add(painelBarras, BorderLayout.CENTER);

        atualizarGraficos();
    }

    private JProgressBar criarBarra(Color cor) {
        JProgressBar bar = new JProgressBar();
        bar.setForeground(cor);
        bar.setBackground(Color.DARK_GRAY);
        bar.setBorderPainted(false);
        bar.setStringPainted(true);
        bar.setFont(new Font("SansSerif", Font.BOLD, 10));
        return bar;
    }

    private void estilizarBotao(JButton btn) {
        btn.setBackground(new Color(60, 120, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
    }

    public void atualizarGraficos() {

        if (!combatente.estaVivo()) {
            lblNome.setText(combatente.getNome() + " (DERROTADO)");
            lblNome.setFont(fonteMorta);
            lblNome.setForeground(Color.RED);

            barVida.setValue(0);
            barVida.setString("MORTO");

            if (btnAcao != null) btnAcao.setVisible(false);
            setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            return;
        }

        lblNome.setText(combatente.getNome() + " (Nv " + combatente.getNivel() + ")");
        lblNome.setFont(fonteNormal);
        lblNome.setForeground(Estilos.TEXTO_CLARO);

        barVida.setMaximum(combatente.getVidaMaxima());
        barVida.setValue(combatente.getVidaAtual());
        barVida.setString(
                combatente.getVidaAtual() + "/" + combatente.getVidaMaxima()
        );

        if (!(combatente instanceof Cacador)) {
            barRecurso.setMaximum(combatente.getMaxRecurso());
            barRecurso.setValue(combatente.getValorRecurso());
            barRecurso.setString(
                    combatente.getNomeRecurso() + ": " + combatente.getValorRecurso()
            );
        }
    }

    public void setAtivo(boolean ativo) {
        if (!combatente.estaVivo()) return;

        if (ativo) {
            setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
            if (btnAcao != null) btnAcao.setEnabled(true);
        } else {
            setBorder(new EmptyBorder(10, 10, 10, 10));
            if (btnAcao != null) btnAcao.setEnabled(false);
        }
    }

    public JButton getBotaoAcao() {
        return btnAcao;
    }
}
