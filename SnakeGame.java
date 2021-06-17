import java.awt.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SnakeGame {
	// the snake game class creates a JFrame and adds a Game object to it

	// variable holding the size of the game board we want
	static int canvasSide = 700;

	public static void main(String[] args) {
		// creates the JFrame and makes it visible to the user

		// makes a new frame
		JFrame frame = new JFrame("Snake Game");
		// set the dimensions of the frame
		frame.getContentPane().setPreferredSize(new Dimension(canvasSide, canvasSide));
		// adds the Game object to the grame
		frame.add(new Game(canvasSide));
		// shows the frame to the user
		frame.pack();
		frame.setVisible(true);
	}
}

class Game extends JPanel implements KeyListener, ActionListener {
	// creates the snake game

	// has the size (width/height) or the game board
	int canvasSide;
	// holds the snake information
	ArrayList<Point> snake;
	// creates a timer object
	private Timer timer;
	// sets the "frame rate"
	private int delay = 200;
	// sets the initial direction
	String direction = "right";
	// sets the grid size (how big each chunk of the snake is)
	static int gridSize = 50;
	// sets the initial fruit position
	Point fruit = new Point(6, 6);
	// keeps track of the users score (how many fruits are eaten)
	int score = 0;
	// holds the highest score the user has got
	int highScore = 0;
	// will hold the number of squares in each row/col
	int squares;
	// creates a scanner object
	Scanner scan = new Scanner(System.in);

    public Game(int canvasSide) {
    	// constructor that initializes variables

    	// sets canvas side
    	this.canvasSide = canvasSide;
    	// calculates squares
    	squares = canvasSide / gridSize;
    	// creates the key listener
    	addKeyListener(this);
    	setFocusable(true);
    	setFocusTraversalKeysEnabled(true);
    	// creates a new timer and starts it (calls the action preformed every delay time)
    	timer = new Timer(delay, this);
    	timer.start();
    	// creates the original snake
    	snake = new ArrayList<Point>();
		snake.add(new Point(5, 3));
		snake.add(new Point(4, 3));
		snake.add(new Point(3, 3));
		snake.add(new Point(2, 3));
    }

    public void paintComponent(Graphics g) {
    	// paints the JPanel

        super.paintComponent(g);  
        // draws the snake     
        g.setColor(Color.decode("#BBD0DD"));
        for (Point point: snake) {
        	g.fillRect((int) point.getX() * gridSize, (int) point.getY() * gridSize, gridSize, gridSize);
        }
        // draws the head
        g.setColor(Color.decode("#78A1BB"));
        g.fillRect((int) snake.get(0).getX() * gridSize, (int) snake.get(0).getY() * gridSize, gridSize, gridSize);
        // draws the fruit
        g.setColor(Color.decode("#C82D4C"));
        g.fillOval((int) fruit.getX() * gridSize, (int) fruit.getY() * gridSize, gridSize, gridSize);
        g.dispose();
    }

    public void reset() {
    	// resets the game

    	snake = new ArrayList<Point>();
		snake.add(new Point(5, 3));
		snake.add(new Point(4, 3));
		snake.add(new Point(3, 3));
		snake.add(new Point(2, 3));
		score = 0;
		fruit = new Point(6, 6);
		direction = "right";
		timer = new Timer(delay, this);
  		timer.start();
		repaint();
    }

    public Point placeFruit() {
    	// finds random new position for the fruit that is on the game board, but not under the snake
    	// (calls recursively until a valid fruit postion is found)

    	int newX = (int) Math.floor(Math.random() * squares);
    	int newY = (int) Math.floor(Math.random() * squares);
    	for (Point point: snake) {
    		if (point.getX() == newX && point.getY() == newY) {
    			return placeFruit();
    		}
    	}
    	return new Point(newX, newY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	// moves the snake

    	// holds the position of the snake's head
    	Point newHead;

    	// determines where the new head will be
    	if (direction == "right") {
    		newHead = new Point((int) snake.get(0).getX() + 1, (int) snake.get(0).getY());
    	} else if (direction == "left") {
    		newHead = new Point((int) snake.get(0).getX() - 1, (int) snake.get(0).getY());
    	} else if (direction == "up") {
    		newHead = new Point((int) snake.get(0).getX(), (int) snake.get(0).getY() - 1);
    	} else {
    		newHead = new Point((int) snake.get(0).getX(), (int) snake.get(0).getY() + 1);
    	}

    	// checks if the snake runs into the fruit and adjust the end and score accordingly
    	if (newHead.getX() == fruit.getX() && newHead.getY() == fruit.getY()) {
	    		fruit = placeFruit();
	    		score += 1;
	    	} else {
	    		snake.remove(snake.size() - 1);
	    	}
	    // checks if the snake is going to run into itself
    	boolean hitItself = false;
    	for (Point point: snake) {
    		if (point.getX() == newHead.getX() && point.getY() == newHead.getY()) {
    			hitItself = true;
    		}
    	}
    	// determines if the game is over or not (if a new frame should be drawn)
    	if (newHead.getX() >= 0 && newHead.getX() <= squares - 1 && newHead.getY() >= 0 && newHead.getY() <= squares - 1 && !hitItself) {
    		snake.add(0, newHead);
    		repaint();
    	} else {
    		timer.stop();
    		// updates high score if needed
    		if (score > highScore) {
    			highScore = score;
    			System.out.println("You got the new high score of " + score);
    		} else {
    			System.out.println("Your score is " + score);
    		}
    		System.out.println("Press space to play again!");
    	}
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    	// handles key events

    	// changes the direction based on what has been clicked and resets the game with space
    	if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != "left") {
    		direction = "right";
    	} else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != "right") {
    		direction = "left";
    	} else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != "up") {
    		direction = "down";
    	} else if (e.getKeyCode() == KeyEvent.VK_UP && direction != "down") {
    		direction = "up";
    	} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
    		reset();
    	}
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}