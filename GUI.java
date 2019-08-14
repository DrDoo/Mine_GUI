
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {

  // The coordinates of the mouse
  public int mouseX = 0;
  public int mouseY = 0;

  // Coordinates for the reset button
  public int resetX = 600;
  public int resetY = 5;

  // Tool for choosing random numbers
  Random rand = new Random();

  // Game state booleans. Initialised as false as the player has neither won or
  // lost at the start of the game
  public boolean won = false;
  public boolean lost = false;

  // The gaps between the each cell
  int spacing = 5;

  // The number of bordering mines
  int borderingMines= 0;

  // Arrays to hold reference to the mine locations, the number of bordering
  // mines for each cell, whether the cell has been revealed and whether it has
  // been flagged
  int[][] mines = new int[16][9];
  int[][] borderMines = new int [16][9];
  boolean revealed[][] = new boolean [16][9];
  boolean flagged[][] = new boolean [16][9];

  public GUI() {

    this.setTitle("Minesweeper");
    // Game is meant to be 1200x800 but the top bar of the window is 26 px tall
    // and the sides of the window are approx 3px each
    this.setSize(1286,837);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    //this.setResizable(false);

    /*
██ ███    ██ ██ ████████
██ ████   ██ ██    ██
██ ██ ██  ██ ██    ██
██ ██  ██ ██ ██    ██
██ ██   ████ ██    ██
*/

    // Chooses the mine locations randomly
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {

        // The value here determines the percentage of cells that are mines
      	if (rand.nextInt(100) < 20) {
      		mines[i][j] = 1;
        }
      	else {
      		mines[i][j] = 0;
      	}
        revealed[i][j] = false;
      } // for
    } // for

    // Figures out how many bordering mines there are for each cell
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        // Number initialised to 0 for each cell
        borderingMines = 0;
        for (int m = 0; m < 16; m++) {
          for (int n = 0; n < 9; n++) {
            // Here we make sure were not comparing a cell to itself. If the
            // bordering cell contains a mine then the number goes up
            if (!(m==i && n==j))
              if (isNeighbour(i, j, m, n) == true)
                borderingMines++;
          } // for
        } // for
        // The number of bordering mines is placed into the array
        borderMines[i][j] = borderingMines;
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

    public void paintComponent(Graphics graphics) {
      graphics.setColor(Color.DARK_GRAY);
      graphics.fillRect(0, 0, 1290, 850);

      // Currently the cells are hardcoded to be a 16x9 grid
      for (int i = 0; i < 16; i++) {
        for (int j = 0; j < 9; j++) {

          int xCoord = spacing + i*80;
          int yCoord = spacing + j*80 + 80;
          int width = 80 - 2*spacing;

          // The cells default colour is grey but if the mouse is hovering over
          // the cell it changes to light great
          graphics.setColor(Color.GRAY);

          if (revealed[i][j] == true) {
        	  graphics.setColor(Color.WHITE);
            if (mines[i][j] == 1) {
              graphics.setColor(Color.RED);
            }
          }

          // If the mouse is hovering over a cell its colour is changed
          if (mouseX >= xCoord && mouseX < (xCoord+width)) {
            if (mouseY >= yCoord+26 && mouseY < (yCoord+width+26)) {
              if (!revealed[i][j])
                graphics.setColor(Color.LIGHT_GRAY);
            }

          }

          graphics.fillRect(xCoord , yCoord, width, width);

          // If a cell has been revlealed we either show the number of border
          // mines for the mine itself
          if (revealed[i][j] == true) {
            // If the cell is not a mine, show the number of border mines
            if (mines[i][j] == 0) {
              // The switch colour codes the number
              switch (borderMines[i][j]) {
                case 0:
                  graphics.setColor(Color.BLACK);
                  break;
                case 1:
                  graphics.setColor(Color.BLUE);
                  break;
                case 2:
                  graphics.setColor(Color.GREEN);
                  break;
                case 3:
                  graphics.setColor(Color.RED);
                  break;
                case 4:
                  graphics.setColor(Color.MAGENTA); // Purple
                  break;
                case 5:
                  graphics.setColor(Color.BLACK);
                  break;
                case 6:
                  graphics.setColor(Color.ORANGE); // Maroon
                  break;
                case 7:
                  graphics.setColor(Color.GRAY);
                  break;
                case 8:
                  graphics.setColor(Color.CYAN); // Turquoise
                  break;
              } // switch
              graphics.setFont(new Font("Ubuntu Mono", Font.BOLD, 40));
              if (borderMines[i][j] != 0)
                graphics.drawString(""+borderMines[i][j], xCoord+27, yCoord+50);
            } // if
            // If the cell is a mine, show the mine
            else if (mines[i][j] == 1) {
              graphics.setColor(Color.BLACK);
              graphics.fillRect(xCoord+25, yCoord+15, 20, 40);
              graphics.fillRect(xCoord+15, yCoord+25, 40, 20);
              graphics.fillRect(xCoord+20, yCoord+20, 30, 30);
              graphics.fillRect(xCoord+33, yCoord+10, 4, 50);
              graphics.fillRect(xCoord+10, yCoord+33, 50, 4);
            }
          } // if
        } // for
      } // for

      // The rest button
      graphics.setColor(Color.LIGHT_GRAY);
      graphics.fillRect(resetX, resetY, 160, 70);
      graphics.setColor(Color.BLACK);
      graphics.setFont(new Font("Ubuntu Mono", Font.BOLD, 40));
      graphics.drawString("Reset?", resetX+20, resetY+47);


    } // paintComponent
  } // gameArea

  /*
███    ███  ██████  ██    ██ ███████
████  ████ ██    ██ ██    ██ ██
██ ████ ██ ██    ██ ██    ██ █████
██  ██  ██ ██    ██  ██  ██  ██
██      ██  ██████    ████   ███████
*/



  public class Move implements MouseMotionListener {

    @Override
    public void mouseDragged(MouseEvent event) {
    }

    @Override
    public void mouseMoved (MouseEvent event) {
      // System.out.println("Move Works");
      mouseY = event.getY();
      mouseX = event.getX();
    }

  } // Move

  /*
 ██████ ██      ██  ██████ ██   ██
██      ██      ██ ██      ██  ██
██      ██      ██ ██      █████
██      ██      ██ ██      ██  ██
 ██████ ███████ ██  ██████ ██   ██
*/



  public class Click implements MouseListener {

    @Override
    public void mouseClicked (MouseEvent e) {

      // If a cell is clicked it is marked as being revealed
      if (inBoxX() != -1 && inBoxY() != -1) {
        revealed[inBoxX()][inBoxY()] = true;
        if (borderMines[inBoxX()][inBoxY()] == 0
            && mines[inBoxX()][inBoxY()] == 0) {
          openEmpties(inBoxX(), inBoxY());
        }
      }

      // This bit is for debugging/testing
      if (inBoxX() != -1 && inBoxY() != -1) {
        System.out.println("Cell [" + inBoxX() + "," + inBoxY() +  "]");
        System.out.println("Neighbours : " + borderMines[inBoxX()][inBoxY()]);
      }
      else System.out.println("The mouse isnt in a cell");

      if (inReset())
        reset();
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

  } // Click

  /*
██████  ███████ ███████ ███████ ████████
██   ██ ██      ██      ██         ██
██████  █████   ███████ █████      ██
██   ██ ██           ██ ██         ██
██   ██ ███████ ███████ ███████    ██
*/



  public void reset() {
    won = false;
    lost = false;

    // Chooses the mine locations randomly
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {

        //We replace all the mines
        // The value here determines the percentage of cells that are mines
      	if (rand.nextInt(100) < 20) {
      		mines[i][j] = 1;
        }
      	else {
      		mines[i][j] = 0;
      	}
        revealed[i][j] = false;
        // All mines are unflagged
        flagged[i][j] = false;
      } // for
    } // for

    // Figures out how many bordering mines there are for each cell
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        // Number initialised to 0 for each cell
        borderingMines = 0;
        for (int m = 0; m < 16; m++) {
          for (int n = 0; n < 9; n++) {
            // Here we make sure were not comparing a cell to itself. If the
            // bordering cell contains a mine then the number goes up
            if (!(m==i && n==j))
              if (isNeighbour(i, j, m, n) == true)
                borderingMines++;
          } // for
        } // for
        // The number of bordering mines is placed into the array
        borderMines[i][j] = borderingMines;
      } // for
    } // for
  }

  public boolean inReset() {
    if (mouseX >= resetX && mouseX < resetX + 160) {
      if (mouseY >= resetY && mouseY < resetY + 110)
        return true;
    }
    return false;
  }

  /*
██ ███    ██     ██████   ██████  ██   ██
██ ████   ██     ██   ██ ██    ██  ██ ██
██ ██ ██  ██     ██████  ██    ██   ███
██ ██  ██ ██     ██   ██ ██    ██  ██ ██
██ ██   ████     ██████   ██████  ██   ██
*/



  // Method to see which collumn the mouse is clicking on
  public int inBoxX() {

    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {

        int xCoord = spacing + i*80;
        int yCoord = spacing + j*80 + 80;
        int width = 80 - 2*spacing;

        if (mouseX >= xCoord && mouseX < (xCoord+width)) {
          if (mouseY >= yCoord+26 && mouseY < (yCoord+width+26))
            return i;
        } // if
      } // for
    } // for
    return -1;
  } // inBoxX

  // Method to see which row the mouse is clicking on
  public int inBoxY() {

    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {

        int xCoord = spacing + i*80;
        int yCoord = spacing + j*80 + 80;
        int width = 80 - 2*spacing;

        if (mouseX >= xCoord && mouseX < (xCoord+width)) {
          if (mouseY >= yCoord+26 && mouseY < (yCoord+width+26))
            return j;
        } // if
      } // for
    } // for
    return -1;
  } // inBoxY

  /*
██ ███████     ███    ██ ███████ ██  ██████  ██   ██ ██████   ██████  ██    ██ ██████
██ ██          ████   ██ ██      ██ ██       ██   ██ ██   ██ ██    ██ ██    ██ ██   ██
██ ███████     ██ ██  ██ █████   ██ ██   ███ ███████ ██████  ██    ██ ██    ██ ██████
██      ██     ██  ██ ██ ██      ██ ██    ██ ██   ██ ██   ██ ██    ██ ██    ██ ██   ██
██ ███████     ██   ████ ███████ ██  ██████  ██   ██ ██████   ██████   ██████  ██   ██
*/



  // This is used to see if a bordering cell contains a mine
  public boolean isNeighbour(int cellX, int cellY, int otherX, int otherY) {

    // We loop through all cells untill we reach cells a distance of 1 away, if
    // this cell contains a mine then the check passes
    if ((cellX - otherX < 2) && (cellX - otherX > -2)
        && (cellY - otherY < 2) && (cellY - otherY > -2)
        && mines[otherX][otherY] == 1)
      return true;
    return false;
  } // isNeighbour

  public void openEmpties(int cellX, int cellY) {

    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        System.out.println("Cell : " + i + "," + j);
        if ((cellX - i < 2) && (cellX - i > -2)
            && (cellY - j < 2) && (cellY - j > -2)
            && mines[cellX][cellY] == 0) {
          if (borderMines[i][j] == 0 && revealed[i][j] == false) {
            if (!(cellX==i && cellY==j)) {
              openEmpties(i, j);
            }
          }
          revealed[i][j] = true;
          /*
          if (borderMines[i][j] == 0)
            openEmpties(i, j);
          revealed[i][j] = true;
          */
        }
      }
    }

    /*
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 9; j++) {
        if ((inBoxX() - i < 2) && (inBoxX() - i > -2)
            && (inBoxY() - j < 2) && (inBoxY() - j > -2)
            && mines[inBoxX()][inBoxY()] == 0)
          revealed[i][j] = true;
      }
    }
    */
  }


} // GUI
