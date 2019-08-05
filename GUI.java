import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {

  int spacing = 5;

  public int mouseX = 0;
  public int mouseY = 0;

  public GUI() {
    this.setTitle("Minesweeper");
    this.setSize(1286,837);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    gameArea game = new gameArea();
    this.setContentPane(game);

    Move move = new Move();
    this.addMouseMotionListener(move);

    Click click = new Click();
    this.addMouseListener(click);
  } // Constructor

  public class gameArea extends JPanel  {

    public void paintComponent(Graphics g) {
      g.setColor(Color.DARK_GRAY);
      g.fillRect(0, 0, 1280, 900);

      for (int i = 0; i < 16; i++) {
        for (int j = 0; j < 9; j++) {

          int xCoord = spacing + i*80;
          int yCoord = spacing + j*80 + 80;
          int width = 80 - 2*spacing;

          g.setColor(Color.GRAY);
          if (mouseX >= xCoord && mouseX < (xCoord+width)) {
            if (mouseY >= yCoord+26 && mouseY < (yCoord+width+26))
              g.setColor(Color.LIGHT_GRAY);
          }


          g.fillRect(xCoord , yCoord, width, width);
        } // for
      } // for
    } // paintComponent
  } // gameArea

  public class Move implements MouseMotionListener {

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved (MouseEvent e) {
      // System.out.println("Move Works");
      mouseY = e.getY();
      mouseX = e.getX();
    }

  } // Click

  public class Click implements MouseListener {

    @Override
    public void mouseClicked (MouseEvent e) {
      // System.out.println("Click Works");
    }

    @Override
    public void mouseEntered (MouseEvent arg0) {

    }

    @Override
    public void mouseExited (MouseEvent arg0) {

    }

    @Override
    public void mousePressed (MouseEvent arg0) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }


  }
} // GUI
