import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Square{

    public int row;
    public int col;
    private int rank;
    public int imageType;
    public Square(int x, int y, int r, int tileType)
    {
        row=x;
        col=y;
        rank=r;
        imageType = tileType;
        
    }
    public int getRank(){
        return rank;
    }
    //Changes theme
    public void setType(int i){
        imageType = i;
    }
    //When tiles combine, they increase in rank
    public void upgrade(){
        rank++;
    }
    //X position in grid
    public int getX(){
        return row;
    }
    //Y position in grid
    public int getY(){
        return col;
    }
    
    //Assigns images based on theme
    public String assignImage (){
        double num = Math.pow(2,rank);
        if (imageType == 1) {
            if (rank<6) {
                return "Numbers_" + (int)num + ".png";
            } else {
                return "Numbers_" + (int)num + ".jpg";
            }
        } else if (imageType == 2) {
            return "Brain_" + (int)num + ".jpg";
        } else {
            return "Sticker_" + (int)num + ".png";
        }
    }
}