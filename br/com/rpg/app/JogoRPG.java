package br.com.rpg.app;

import br.com.rpg.model.combatente.*;
import br.com.rpg.model.enums.TipoAcao;
import br.com.rpg.ui.components.CardPersonagem;
import br.com.rpg.ui.theme.Estilos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class JogoRPG extends JFrame {

    private List<Combatente> herois;
    private List<Combatente> inimigos;
    private List<Combatente> filaTurnos;
    private int indiceTurno;
    private int rodadaGeral = 1;
    private String nomeJogador;

    private JPanel painelHerois;
    private JPanel painelInimigos;
    private JTextPane logPane;
    private StyledDocument docLog;
    private JLabel lblTitulo;

    private Map<Combatente, CardPersonagem> mapaCards;

    public JogoRPG() {
        configurarJanela();
        mostrarTelaInicial();
    }

    private void configurarJanela() {
        setTitle("RPG Torneio dos Reinos");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Estilos.FUNDO_ESCURO);
    }

    /* ================= LOG ================= */

    private void log(String texto, Color cor, boolean negrito) {
        try {
            SimpleAttributeSet estilo = new SimpleAttributeSet();
            StyleConstants.setForeground(estilo, cor);
            StyleConstants.setBold(estilo, negrito);
            StyleConstants.setFontFamily(estilo, "SansSerif");
            StyleConstants.setFontSize(estilo, 13);

            docLog.insertString(docLog.getLength(), texto + "\n", estilo);
            logPane.setCaretPosition(docLog.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /* ================= TELA INICIAL ================= */

    private void mostrarTelaInicial() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Estilos.FUNDO_ESCURO);

        JLabel titulo = new JLabel("TORNEIO DOS REINOS");
        titulo.setFont(new Font("Serif", Font.BOLD, 40));
        titulo.setForeground(Color.ORANGE);

        JTextField txtNome = new JTextField(15);
        txtNome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtNome.setBorder(BorderFactory.createTitledBorder(
                null, "Nome do Comandante",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                null, Color.LIGHT_GRAY));

        JButton btnStart = new JButton("INICIAR AVENTURA");
        btnStart.setBackground(new Color(50, 150, 50));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnStart.setFocusPainted(false);

        btnStart.addActionListener(e -> {
            nomeJogador = txtNome.getText().isBlank() ? "Viajante" : txtNome.getText();
            iniciarCampanha();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(titulo, gbc);
        gbc.gridy++;
        panel.add(txtNome, gbc);
        gbc.gridy++;
        panel.add(btnStart, gbc);

        setContentPane(panel);
        revalidate();
    }

    /* ================= CAMPANHA ================= */

    private void iniciarCampanha() {
        herois = new ArrayList<>();
        herois.add(new Guardiao(nomeJogador, 1));
        herois.add(new Arcanista("Mago Real", 1));
        herois.add(new Cacador("Arqueiro Élfico", 1));

        prepararInterfaceBatalha();
        iniciarNovaBatalha();
    }

    private void prepararInterfaceBatalha() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Estilos.FUNDO_ESCURO);

        lblTitulo = new JLabel();
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel arena = new JPanel(new GridLayout(1, 2, 20, 0));
        arena.setOpaque(false);
        arena.setBorder(new EmptyBorder(10, 20, 10, 20));

        painelHerois = new JPanel(new GridLayout(0, 1, 0, 10));
        painelHerois.setOpaque(false);

        painelInimigos = new JPanel(new GridLayout(0, 1, 0, 10));
        painelInimigos.setOpaque(false);

        arena.add(painelHerois);
        arena.add(painelInimigos);
        mainPanel.add(arena, BorderLayout.CENTER);

        logPane = new JTextPane();
        logPane.setEditable(false);
        logPane.setBackground(new Color(20, 20, 20));
        docLog = logPane.getStyledDocument();

        JScrollPane scroll = new JScrollPane(logPane);
        scroll.setPreferredSize(new Dimension(800, 150));
        scroll.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY), "Diário de Combate",
                TitledBorder.LEFT, TitledBorder.TOP, null, Color.WHITE));

        mainPanel.add(scroll, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        revalidate();
    }

    /* ================= BATALHA ================= */

    private void iniciarNovaBatalha() {
        inimigos = new ArrayList<>();

        int qtd = Math.min(1 + rodadaGeral / 2, 3);
        for (int i = 0; i < qtd; i++) {
            inimigos.add(new Guardiao("Orc Saqueador " + (i + 1), rodadaGeral));
        }

        if (rodadaGeral % 3 == 0) {
            inimigos.add(new Arcanista("Xamã Chefe", rodadaGeral + 2));
        }

        lblTitulo.setText("ONDA " + rodadaGeral);
        log("=== ONDA " + rodadaGeral + " INICIADA ===", Color.YELLOW, true);

        painelHerois.removeAll();
        painelInimigos.removeAll();
        mapaCards = new HashMap<>();

        for (Combatente h : herois) {
            CardPersonagem card = new CardPersonagem(h, true, () -> mostrarMenuAcao(h));
            painelHerois.add(card);
            mapaCards.put(h, card);
        }

        for (Combatente i : inimigos) {
            CardPersonagem card = new CardPersonagem(i, false, null);
            painelInimigos.add(card);
            mapaCards.put(i, card);
        }

        revalidate();
        repaint();
        iniciarTurnos();
    }

    private void iniciarTurnos() {
        filaTurnos = new ArrayList<>();
        filaTurnos.addAll(herois);
        filaTurnos.addAll(inimigos);
        filaTurnos.sort(Comparator.comparingInt(Combatente::getNivel).reversed());
        indiceTurno = 0;
        processarTurno();
    }

    private void processarTurno() {
        if (herois.stream().noneMatch(Combatente::estaVivo)) {
            JOptionPane.showMessageDialog(this, "GAME OVER!");
            System.exit(0);
        }

        if (inimigos.stream().noneMatch(Combatente::estaVivo)) {
            rodadaGeral++;
            JOptionPane.showMessageDialog(this, "Vitória! Próxima onda...");
            herois.forEach(h -> { if (h.estaVivo()) h.curar(30); });
            iniciarNovaBatalha();
            return;
        }

        if (indiceTurno >= filaTurnos.size()) indiceTurno = 0;

        Combatente atual = filaTurnos.get(indiceTurno);

        if (!atual.estaVivo()) {
            indiceTurno++;
            processarTurno();
            return;
        }

        atual.iniciarTurno();
        destacarAtivo(atual);

        if (herois.contains(atual)) {
            log("Sua vez: " + atual.getNome(), Color.WHITE, false);
        } else {
            new Timer(800, e -> {
                ((Timer) e.getSource()).stop();
                executarIA(atual);
            }).start();
        }
    }

    private void destacarAtivo(Combatente atual) {
        mapaCards.values().forEach(c -> c.setAtivo(false));
        mapaCards.get(atual).setAtivo(true);
    }

    /* ================= AÇÕES ================= */

    private void mostrarMenuAcao(Combatente heroi) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem basico = new JMenuItem("Ataque Básico");
        basico.addActionListener(e -> selecionarAlvo(heroi, TipoAcao.ATAQUE_BASICO));

        JMenuItem especial = new JMenuItem("Especial");
        especial.addActionListener(e -> selecionarAlvo(heroi, TipoAcao.ESPECIAL));

        JMenuItem defender = new JMenuItem("Defender");
        defender.addActionListener(e -> executarAcao(heroi, TipoAcao.DEFENDER, null));

        menu.add(basico);
        menu.add(especial);
        menu.addSeparator();
        menu.add(defender);

        JButton btn = mapaCards.get(heroi).getBotaoAcao();
        menu.show(btn, 0, btn.getHeight());
    }

    private void selecionarAlvo(Combatente atacante, TipoAcao tipo) {
        JPopupMenu menu = new JPopupMenu();

        for (Combatente inimigo : inimigos) {
            if (inimigo.estaVivo()) {
                JMenuItem item = new JMenuItem(
                        inimigo.getNome() + " (HP " + inimigo.getVidaAtual() + ")");
                item.addActionListener(e -> executarAcao(atacante, tipo, inimigo));
                menu.add(item);
            }
        }

        JButton btn = mapaCards.get(atacante).getBotaoAcao();
        menu.show(btn, btn.getWidth(), 0);
    }

    private void executarAcao(Combatente atacante, TipoAcao tipo, Combatente alvo) {
        String res = atacante.executarAcao(tipo, alvo);
        log(res, Color.WHITE, false);

        atualizarUI();
        indiceTurno++;
        processarTurno();
    }

    /* ================= IA ================= */

    private void executarIA(Combatente inimigo) {
        List<Combatente> alvos = herois.stream().filter(Combatente::estaVivo).toList();
        if (alvos.isEmpty()) return;

        Combatente alvo = alvos.get(new Random().nextInt(alvos.size()));
        TipoAcao acao = Math.random() < 0.3 ? TipoAcao.ESPECIAL : TipoAcao.ATAQUE_BASICO;

        log(inimigo.executarAcao(acao, alvo), Estilos.COR_INIMIGO, false);

        atualizarUI();
        indiceTurno++;
        processarTurno();
    }

    private void atualizarUI() {
        mapaCards.values().forEach(CardPersonagem::atualizarGraficos);
    }

    /* ================= MAIN ================= */

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new JogoRPG().setVisible(true));
    }
}

