package com.danilwh.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends Canvas {
    private static final int WIDTH = 360;
    private static final int HEIGHT = 225;
    private static final int SCALE = 3;
    private String title = "Game";
    private boolean running = false;
    private BufferStrategy bs = null;
    private Graphics g = null;
    private Render render;
    private JFrame frame = new JFrame(title);
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
    
    public Main() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        this.render = new Render(WIDTH, HEIGHT, pixels);
    }
    
    private void start() {
        this.running = true;
        this.init();
        
        new Thread(new Runnable() {
            public void run() {
                // get the current system jvm time in nanoseconds.
                long jvmLastTime = System.nanoTime();
                // get the current system jvm time in milliseconds. (for showing in the screen title).
                long timer = System.currentTimeMillis();
                // specify the period of time for which only one update must occur. 
                double jvmPartTime = 1_000_000_000.0 / 60.0;
                // keep the difference of time between last update time and time at the moment of
                // the cycle iteration. 
                double delta = 0.0;
                int updates = 0;
                
                while (running) {
                    // keep the time of the current moment of the cycle iteration.
                    long jvmNow = System.nanoTime();
                    // calculate the difference of time between last update time and current time.
                    delta += jvmNow - jvmLastTime;
                    // set the last time update to the current.
                    jvmLastTime = jvmNow;
                    
                    if (delta >= jvmPartTime) {
                        update();
                        render();
                        
                        updates++;
                        delta = 0;
                    }
                    
                    if (System.currentTimeMillis() - timer > 1000) {
                        timer += 1000;
                        frame.setTitle(title + " | " + "Updates: " + updates);
                        updates = 0;
                    }
                }
            }
        }).start();
    }
    
    private void update() {
    	this.render.generation();
    }
    
    private void render() {
        if (this.bs == null) {
            createBufferStrategy(3);
            this.bs = getBufferStrategy();
        }
        // clear the array of pixels before we render something.
        this.render.clear();
        // fill the array of pixels with numbers each of that represents a color.
        this.render.render();
        this.g = this.bs.getDrawGraphics();
        this.g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
        this.g.dispose();
        this.bufferSwap();
    }
    
    private void bufferSwap() {
        this.bs.show();
    }
    
    private void init() {
        /*** creates the window. ***/
        
        this.frame.setResizable(false);
        this.frame.add(this);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new Main().start();
    }
}
