package src;
import java.util.ArrayList;
import java.lang.Math;
import processing.core.*;

@SuppressWarnings("serial")
public class DiskClicker extends PApplet { 
	
    // Keep track of current score
    int score = 0;
    
    // Canvas size
    final int canvasWidth  = 500;
    final int canvasHeight = 500;
    

    int currentTime = millis()/1000;
    int timeout = 30;
    
    //Declare ArrayList
    ArrayList<Disk> myArrList = new ArrayList<Disk>();

    // setup() runs one time at the beginning of your program
    @Override()
    public void setup() {
        size(canvasWidth, canvasHeight);
        smooth();
        // Create a disks
        System.out.println("Ready to create disks:");

        for  (int i = 0; i < 4; i++) {
        	int shapeWidth = (int)(Math.random()*80+10);
        	int shapeHeight = (int)(Math.random()*80+10);

            myArrList.add(new Disk(random(0, 255), random(0, 255), random(0, 255),
                                   shapeWidth/2,shapeHeight/2));
            //print to check
            System.out.println("Created disk #" + (i + 1));

            System.out.println("   ArrayList Size: " + myArrList.size());
        }
       // System.out.println("Size: " + myArrList.size());
    }
    
    // draw() is called repeatedly in an infinite loop.
    // By default, it is called about 60 times per second.
    @Override()
    public void draw() {
        // Erase the background, if you don't, the previous shape(s) will 
        // still be displayed
        eraseBackground();
        for (int i = 0; i < 4; i++) {
            Disk entity = myArrList.get(i);
        	entity.drawShape();
        	entity.displayPointValue();
        	entity.calcCoords();
        }
        
        mousePressed();
        // Display player's score 

        currentTime = millis()/1000;
        text(30-currentTime, 30,30);
        text("Your Score: "+ this.score,150,30);
        if (timeout==currentTime) {
        	System.out.println("Time is up! Game over!");
        	System.out.println("Your final score is: "+ this.score + "!");
        	System.exit(0);
        }
    }

    public void eraseBackground(){      
        // White background:
        //background(255);
        PImage pic = loadImage("src/space.png");
        background(pic);

    }

    // mousePressed() is a PApplet method that you will override.
    // This method is called from PApplet one time when the mouse is pressed.
    @Override()
    public void mouseClicked() {
        // Draw a circle wherever the mouse is
        int mouseWidth  = 5;
        int mouseHeight = 5;
        fill(0, 255, 0);
        ellipse(mouseX, mouseY, mouseWidth, mouseHeight);

        // Check whether the click occurred within range of the shape
        for (int i = 0; i < 4; i++) {
        	//compares hitbox area around mouse (square) with hitbox area around disk (rectangle)
            Disk entity = myArrList.get(i);
            if (this.mouseX<=entity.x+entity.xHit+20) {
            	if (this.mouseX>=entity.x-entity.xHit-20) {
            		if (this.mouseY<=entity.y+entity.yHit+20) {
            			if (this.mouseY>entity.y-entity.yHit-20) {
                    		// Update score:
                    		score += entity.pointValue;
                    		System.out.println("DBG:  HIT!");
                            System.out.println("Score: " + this.score);
            			}
            		}
            	}
            }

        }
    }

    // Create a Disk class that you will use to create one or more disks with each
    // disk having a color, speed, position, etc.
    class Disk {
        // Size of disk
        // Point value of disk
        // Position of disk - keep track of x and y position of disk
        // Horizontal speed of disk
        /////////// Add:
        // Shape size
        int shapeWidth  = 80;
        int shapeHeight = 50;

        // Shape value
        final static int defaultValue = 10;
        int pointValue = defaultValue * (int)(Math.random()*20+1);

        // Keep track of ball's x and y position
        float x = random(0+shapeWidth/2,500-shapeWidth/2);
        float y = random(0+shapeHeight/2,500-shapeHeight/2);

        
        // Horizontal speed
        float xSpeed = random(4, 10);
        
        // Vertical speed
        float ySpeed = random (4,10);
        
        
        int xHit = shapeWidth/2;
        int yHit = shapeHeight/2;

        // It's hard to click a precise position, to make it easier, 
        // require the click to be somewhere on the shape
        int targetRange = Math.round((min(shapeWidth, shapeHeight)) / 2);

        float red = random(0,255);
        float green = random (0,255);
        float blue = random (0,255);

        // It's hard to click a precise pixel on a disk, to make it easier we can
        // allow the user to click somewhere on the disk.
        // You might want to make the scoring space be a rectangle fitted tightly
        // to the disk - it's easier than calculating a rounded boundary.


        // The constructor could be extended to accept other disk characteristics
        
    	/*
    	//// out: Disk(float red, float green, float blue, int pointValue, int shapeWidth, 
    	int shapeHeight, float xSpeed, float ySpeed, float x, float y, int xHit, 
    	int yHit) {
    	*/
    	    
        Disk(float red, float green, float blue, int xHit, int yHit) {
            
            this.red   = red;
            this.green = green;
            this.blue  = blue;
            this.xHit = xHit;
            this.yHit = yHit;
        }
        

        //Disk() { idk what this does
            //this(0, 0, 255);
        //}

        public void calcCoords() {      
            // Compute the x position where the shape will be drawn
        	this.x+=this.xSpeed;
        	this.y+=this.ySpeed;
            // If the x position is off right side of the canvas, reverse direction of 
            // movement:
            if (this.x < (0+shapeWidth/2)) {
                // Log a debug message:
                System.out.println("DBG:<---  Change direction, go left because x = " + this.x + "\n");

                // Recalculate:
                this.xSpeed = -1 * this.xSpeed;
            }
            // If the x position is off left side of the canvas, reverse direction of 
            // movement:
            if (this.x > (500-shapeWidth/2)) {
                // Log a debug message:
                System.out.println("DBG: ---> Change direction, go right because x = " + this.x + "\n");
               
                // Recalculate:
                this.xSpeed = -1 * this.xSpeed;
            } 
            
            if (this.y < (0+shapeHeight/2)) {
                // Log a debug message:
                System.out.println("DBG: change direction, go up because y = " + this.y + "\n");

                // Recalculate:
                this.ySpeed = -1 * this.ySpeed;
            }
            if (this.y > (500-shapeHeight/2)) {
                // Log a debug message:
                System.out.println("DBG: Change direction, go down because y = " + this.y + "\n");
               
                // Recalculate:
                this.ySpeed = -1 * this.ySpeed;
            } 
            
        }

        public void drawShape(){
            // Select color, then draw the shape at computed x, y location
            fill(this.red, this.green, this.blue);
            ellipse(this.x, this.y, this.shapeWidth,this.shapeHeight);
        }

        public void displayPointValue(){
            // Draw the text at computed x, y location
            textSize(20);
            fill(255, 255, 255);
            textAlign(CENTER);
            text(this.pointValue, x,y);
        }
    }

    public static void main(String[] passedArgs) {
		DiskClicker mySketch = new DiskClicker();
		String[] processingArgs = {"App"};
		PApplet.runSketch(processingArgs, mySketch);
    }
}
