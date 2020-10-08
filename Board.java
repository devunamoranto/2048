import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
/*  NOTE TO MARIA: Line 343 contains the image pathway to pull up the image. After C:\\User\\ change the user name to whatever computer you're on,
 *  or the images won't show up
 * 
 */
public class Board extends JPanel implements KeyListener
{
    private int score = 0;
    private boolean gameOver=false;
    private boolean win = false;
    private int tileType;

    String fileName = "";
    private int myWindowWidth = 550;
    private int myWindowHeight = 560;
    String userName = System.getProperty("user.home");

    public Square[][] board = new Square[4][4];
    public boolean[][] occupiedSquares = new boolean[4][4];
    public boolean[][] sterileSquares = new boolean[4][4];

    Font stringFont = new Font( "SansSerif", Font.PLAIN, 100 );
    Font stringFont2 = new Font("SansSerif", Font.PLAIN, 50);
    Font scoreFont = new Font("SansSerif", Font.PLAIN, 30);
    public Board()
    {

        getInput();
        JFrame easel = new JFrame();
        easel.setSize(myWindowWidth, myWindowHeight);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        easel.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        easel.add(this);
        easel.setVisible(true);
        clearBoard();
        addSquare();
        desterilize();
    }

    public void moveUp(){
        for(int x = 0; x<5; x++){
            for (int row = 1; row<board.length; row++){
                for (int col = 0; col<board.length; col++){
                    if(!sterile(row,col)){
                        combine(row,col,row-1,col);

                    }
                    if (isOccupied(row-1,col)==false && isOccupied(row,col)==true){
                        board[row-1][col] = board[row][col];
                        occupiedSquares[row-1][col] = true;
                        empty(row,col);
                    }

                }
            }
            repaint();
        }
        desterilize();
    }

    public void moveDown(){
        for(int x = 0; x<5; x++){
            for (int row = 2; row>=0; row--){
                for (int col = 0; col<board.length; col++){
                    if(!sterile(row,col)){
                        combine(row,col,row+1,col);

                    }
                    if (isOccupied(row+1,col)==false && isOccupied(row,col) == true){
                        board[row+1][col] = board[row][col];
                        occupiedSquares[row+1][col] = true;
                        empty(row,col);
                    }

                }
            }
            repaint();
        }
        desterilize();
    }

    public void moveLeft(){
        for (int x = 0; x<5; x++){
            for (int col = 1; col<board.length; col++){
                for (int row = 0; row<board.length; row++){
                    if(!sterile(row,col)){
                        combine(row,col,row,col-1);

                    }
                    if (isOccupied(row,col-1)==false && isOccupied(row,col)==true){
                        board[row][col-1] = board[row][col];

                        occupiedSquares[row][col-1] = true;
                        empty(row,col);
                    }

                }
            }
            repaint();
        }
        desterilize();
    }

    public void moveRight(){
        for (int x = 0; x<5; x++){
            for (int col = 2; col>=0; col--){
                for (int row = 0; row<board.length; row++){
                    if(!sterile(row,col)){
                        combine(row,col,row,col+1);

                    }
                    if (isOccupied(row,col+1)==false && isOccupied(row,col)==true){
                        board[row][col+1] = board[row][col];                    
                        occupiedSquares[row][col+1] = true;
                        empty(row,col);
                    }

                }
            }
            repaint();
        }
        desterilize();
    }

    //Adds a '2' or '4' tile randomly in a random, empty position
    public void addSquare(){
        int row = rand(4);
        int col = rand(4);
        while (isOccupied(row, col)){
            row = rand(4);
            col = rand(4);
        }

        occupiedSquares[row][col] = true;
        if(rand(2)==1){
            board[row][col]=new Square(row,col,1,tileType);
        }
        else {
            board[row][col]=new Square(row,col,2,tileType);
        }
        repaint();
    }

    //Combines two tiles if they match
    public void combine(int row, int col, int row2, int col2){
        if(isOccupied(row,col)&&isOccupied(row2,col2)){
            if (isSameRank(board[row][col], board[row2][col2])){
                board[row][col].upgrade();
                score+=Math.pow(2,board[row][col].getRank());
                empty(row2,col2);
                board[row2][col2] = board[row][col];

                empty(row,col);
                occupiedSquares[row2][col2]=true;
                sterilize(row2,col2);
            }

        }
    }

    //Clears a spot in the array of tiles
    public void empty(int row, int col){
        occupiedSquares[row][col] = false;
        board[row][col]=null;
    }

    //Returns if there are no more possible moves and the board is full
    public boolean checkForGameOver(){
        for(int row = 0; row<4; row++){
            for(int col = 0; col<4; col++){
                if (!occupiedSquares[row][col]){return false;}
            }
        }
        if (!possibleMoves("right")&&
        !possibleMoves("left")&&
        !possibleMoves("up")&&
        !possibleMoves("down")){
            gameOver = true;
            return true;
        }
        return false;
    }

    //Checks if there are any possible moves in each direction. Used by each of the move methods, as well as checking for a game over.
    public boolean possibleMoves(String direction){

        if (direction.equals("right")){
            for (int row = 0; row<4; row++){
                for(int col = 0; col<3;col++){
                    if (isOccupied(row,col+1)&&isOccupied(row,col)){
                        if (board[row][col+1].getRank()==board[row][col].getRank()){
                            return true;
                        }
                    }
                    if (isOccupied(row,col+1)==false&&isOccupied(row,col)){
                        return true;
                    }
                }
            }
            return false;
        }
        else if (direction.equals("left")){
            for (int row = 0; row<4; row++){
                for(int col = 3; col>0;col--){
                    if (isOccupied(row,col-1)&&isOccupied(row,col)){
                        if (board[row][col-1].getRank()==board[row][col].getRank()){
                            return true;
                        }
                    }
                    if (isOccupied(row,col-1)==false&&isOccupied(row,col)){
                        return true;
                    }
                }
            }
            return false;
        }
        else if (direction.equals("down")){
            for (int row = 0; row<3; row++){
                for(int col = 0; col<4;col++){
                    if (isOccupied(row+1,col)&&isOccupied(row,col)){
                        if (board[row+1][col].getRank()==board[row][col].getRank()){
                            return true;
                        }
                    }
                    if (isOccupied(row+1,col)==false&&isOccupied(row,col)){
                        return true;
                    }
                }
            }
            return false;
        }
        else { //Up
            for (int row = 3; row>0; row--){
                for(int col = 0; col<4;col++){
                    if (isOccupied(row-1,col)&&isOccupied(row,col)){
                        if (board[row-1][col].getRank()==board[row][col].getRank()){
                            return true;
                        }
                    }
                    if (isOccupied(row-1,col)==false&&isOccupied(row,col)){
                        return true;
                    }
                }
            }
            return false;
        }

    }

    //Restarts the game
    public void restart(){
        for(int x=0;x<4;x++){
            for(int y=0;y<4;y++){
                empty(x,y);
            }
        }
        score=0;
        addSquare();
        gameOver=false;
        win=false;
    }

    //Changes the theme
    public void changeTheme(int i){
        tileType=i;
        for(int x=0; x<4; x++){
            for(int y = 0; y<4; y++){
                if(occupiedSquares[x][y]){
                    board[x][y].setType(i);
                }
            }
        }
    }

    
    //Allows user to pick a theme before the game starts
    public void getInput () {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println(" Type 1 if you would like standard tiles, \n 2 for expanding brain, \n and 3 for stickers.");
        int n = reader.nextInt(); 
        tileType = n;
        reader.close();
    }

    public void paintComponent(Graphics g)
    {
        //Draws the score box and current score
        g.setColor(Color.orange);
        g.fillRect(10,10,300,30);
        g.setColor(Color.black);
        g.setFont(scoreFont);
        g.drawString(""+score,20,35);
        g.drawString("Press 'r' to restart, 'q' to quit, '1' for",30,480);
        g.drawString("numbers, '2' for brains, '3' for stickers",30,510);
        //Draws the grid lines
        for(int x = 0; x<5; x++){

            g.drawLine((100*x)+50,50,(100*x)+50,450);
        }
        for(int x = 0; x<5; x++){

            g.drawLine(50, 100*x+50, 450, 100*x+50);
        }

        //Draws in empty squares
        for (int x = 0; x<board.length; x++){
            for (int y = 0; y<board.length; y++){
                if (isOccupied(x,y)==false){
                    g.setColor(Color.white);
                    g.fillRect((y*100)+50,(x*100)+50,100,100);
                    g.setColor(Color.black);
                    g.drawRect((y*100)+50,(x*100)+50,100,100);

                }
            }
        }
        g.setFont(stringFont2);

        //Goes through the Array, board, and draws the correct picture in its corresponding tile
        for (int x = 0; x<board.length; x++){
            for (int y = 0; y<board.length; y++){
                if (isOccupied(x,y)==false){
                    g.setColor(Color.white);
                    g.fillRect((y*100)+50,(x*100)+50,100,100);
                }
                if (isOccupied(x,y)==true){
                    if(board[x][y].getRank()==11 && win==false){
                        win = true;
                        g.drawString("You win! Keep", 150,200);
                        g.drawString("playing to continue!", 150,250);
                    }
                    fileName = board[x][y].assignImage();

                    try {   
                        //Where it says Recycling Zone, change it to your username or else the images won't show up.
                        BufferedImage image = ImageIO.read(new File(""+userName+"\\Dropbox\\2048 Project\\images\\" + fileName)); 
                        g.drawImage(image, 100*y+50, 100*x+50, null);
                        repaint();
                    } 
                    catch (IOException e) 
                    { 
                        //Not handled. 
                    } 
                }
            }
        }
        g.setColor(Color.black);

        //Draws borders for the squares
        for(int x = 0; x<5; x++){           
            g.drawLine((100*x)+50,50,(100*x)+50,450);
        }
        for(int x = 0; x<5; x++){      
            g.drawLine(50, 100*x+50, 450, 100*x+50);
        }

        //Game over screen, displays score
        g.setFont(stringFont);
        if (gameOver){
            g.setColor(Color.orange);
            g.fillRect(80,110,320,270);
            g.setColor(Color.red);
            g.drawString("GAME", 100,200);
            g.drawString("OVER", 100,300);
            g.setFont(scoreFont);
            g.drawString("Your score was "+score+"!",100,325);
            g.drawString("Press 'r' to play again",100,355);
        }

    }

    public void keyTyped(KeyEvent e){}

    public void keyPressed(KeyEvent e){}

    public void keyReleased(KeyEvent e){
        //Press 'r' to restart
        if(e.getKeyCode()==82){
            restart();
        }   
        //Press 'q' to quit
        if(e.getKeyCode()==81){
            System.exit(0);
        }
        //Change theme to numbers
        if(e.getKeyCode()==49){
            changeTheme(1);
        }
        //Change theme to expanding brain meme
        if(e.getKeyCode()==50){
            changeTheme(2);
        }
        //Change theme to friend stickers
        if(e.getKeyCode()==51){
            changeTheme(3);
        }
        //If there are possible moves, shifts tiles in the direction of the key pressed and adds a random '2' or '4' square somewhere
        {
            if(!checkForGameOver()){
                if (possibleMoves("up")&&e.getKeyCode()==KeyEvent.VK_UP){
                    moveUp();
                    addSquare();
                    repaint();}
                if (possibleMoves("down")&&e.getKeyCode()==KeyEvent.VK_DOWN){moveDown();
                    addSquare();
                    repaint();}
                if (possibleMoves("left")&&e.getKeyCode()==KeyEvent.VK_LEFT){moveLeft();
                    addSquare();
                    repaint();}
                if (possibleMoves("right")&&e.getKeyCode()==KeyEvent.VK_RIGHT){moveRight();
                    addSquare();
                    repaint();}
            }
            else {repaint();}
        }
    }

    //Clears the board
    public void clearBoard(){
        for (int row = 0; row<4; row++){
            for(int col = 0; col<4; col++){
                occupiedSquares[row][col] = false;
            }
        }
    }

    /*
     * These next three methods are helpers utilized along with the method combine in order to help correctky combine squares; they keep squares from combining
     * twice during the same turn.
     */
    public void sterilize(int x, int y){
        sterileSquares[x][y]=true;
    }

    public boolean sterile(int x, int y){
        return sterileSquares[x][y];
    }

    public void desterilize(){
        for(int row = 0; row<4; row++){
            for (int col = 0; col<4; col++){
                sterileSquares[row][col]=false;
            }
        }
    }
    //Helper methods
    public boolean isSameRank(Square a, Square b){
        return (a.getRank()==b.getRank());       
    }

    public boolean isOccupied(int row, int col){
        return occupiedSquares[row][col];
    }

    public int rand(int n){
        Random r = new Random(); 
        return r.nextInt(n);
    }
}
