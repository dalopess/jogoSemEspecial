package ifpr.paranavai.jogo.modelo;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Fase extends JPanel implements ActionListener, KeyListener {
    private Image fundo;
    private Personagem personagem;
    private static final int DELAY = 5;
    private Timer timer;
    private static final int LARGURA_JANELA = 938;
    private boolean atirar = true;
    private ArrayList<Inimigo> inimigos;
    private static final int QTDE_DE_INIMIGOS = 60;

    public Fase() {

        setFocusable(true);
        setDoubleBuffered(true);

        ImageIcon carregando = new ImageIcon("recursos\\fundo.gif");
        fundo = carregando.getImage();

        this.personagem = new Personagem();
        this.personagem.carregar();

        this.inicializaInimigos();
        addKeyListener(this);
        timer = new Timer(DELAY, this);
        timer.start();

        addKeyListener(new TecladoAdp());
        timer = new Timer(DELAY, (ActionListener) this);
        timer.start();
    }

    public void inicializaInimigos() {
        inimigos = new ArrayList<Inimigo>();

        for (int i = 0; i < QTDE_DE_INIMIGOS; i++) {
            int x = (int) (Math.random() * 8000 + 1024);
            int y = (int) (Math.random() * 650 + 30);
            Inimigo inimigo = new Inimigo(x, y);
            inimigos.add(inimigo);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graficos = (Graphics2D) g;
        graficos.drawImage(fundo, 0, 0, null);
        graficos.drawImage(personagem.getImagem(), personagem.getPosicaoEmX(), personagem.getPosicaoEmY(), this);

        ArrayList<Tiro> tiros = personagem.getTiros();   
        for (Tiro tiro : tiros) {
            tiro.carregar();
            graficos.drawImage(tiro.getImagem(), tiro.getPosicaoEmX(), tiro.getPosicaoEmY(), this);
        }

        for (Inimigo inimigo : inimigos) {
            inimigo.carregar();
            graficos.drawImage(inimigo.getImagem(), inimigo.getPosicaoEmX(), inimigo.getPosicaoEmY(), this);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        personagem.atualizar();

        ArrayList<Tiro> tiros = personagem.getTiros();

        for (int i = 0; i < tiros.size(); i++) {
            if (tiros.get(i).getPosicaoEmX() > LARGURA_JANELA)
                tiros.remove(i);
            else
                tiros.get(i).atualizar();
        }

        for (int i = 0; i < inimigos.size(); i++) {
            if (inimigos.get(i).getPosicaoEmX() < 0)
                inimigos.remove(i);

            else
                inimigos.get(i).atualizar();
        }

        repaint();
    }

    private class TecladoAdp extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            personagem.mover(e);
        }

        public void keyReleased(KeyEvent e) {
            personagem.parar(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            personagem.atirar();
        else
            personagem.mover(e);
    }

    



    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            atirar = true;

        personagem.parar(e);
    }
}
