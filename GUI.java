import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {

  // The gaps between the each cell
  int spacing = 5;

  // The coordinates of the mouse
  public int mouseX = 0;
  public int mouseY = 0;
  
  Random rand = new Random();
  
  int[][] mines = new int[16][9];
  int[][] neighbours = new int [16][9];
  boolean revealed[][] = new boolean [16][9];
  boolean flagged[][] = new boolean [16][9];

  public GUI() {

    this.setTitle("Minesweeper");
    // Game is meant to be 1200x800 but the top bar of the window is 26 px tall
    // and the sides of the window are approx 3px each
    this.setSize(1286,837);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    this.setResizable(false);
    
    
    // Places the mines randomly
    for (int i = 0; i < 16; i++) {
        for (int j = 0; j < 9; j++) {
        	if (rand.nextInt(100) < 20) {
        		mines[i][j] = 1;
          }
        	else {
        		mines[i][j] = 0;
        	}
        } // for
      } // for

    // The actual game board
    gameArea game = new gameArea();
    this.setContentPane(game);

    // Action listeners for mouse movement and clicking
    Move move = new Move();
    this.addMouseMotionListener(move);

    Click click = new Click();
    this.addMouseListener(click);
  } // Constructor

  public class gameArea extends JPanel  {

    public void paintComponent(Graphics g) {
      g.setColor(Color.DARK_GRAY);
      g.fillRect(0, 0, 1280, 900);

      // Currently the cells are hardcoded to be a 16x9 grid
      for (int i = 0; i < 16; i++) {
        for (int j = 0; j < 9; j++) {

          int xCoord = spacing + i*80;
          int yCoord = spacing + j*80 + 80;
          int width = 80 - 2*spacing;

          // The cells default colour is grey but if the mouse is hovering over
          // the cell it changes to light great
          g.setColor(Color.GRAY);
          /*
          if (mines[i][j] == 1)
        	  g.setColor(Color.YELLOW);
          */
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
