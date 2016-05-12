/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.guntram.gamecubetexturedisplay;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author gbl
 */
public class JCanvas extends JPanel {
    int width;
    int height;
    byte[] buffer;
    byte[] palette;
    private BufferedImage canvas;
    
    public JCanvas(int width, int height) {
        this.width=width;
        this.height=height;
        buffer=new byte[width*height];
        palette=null;
        setPreferredSize(new Dimension(width*3, height*3));
    }
    
    public byte[] getBuffer() {
        return buffer;
    }
    
    public void setPalette(byte[] rgb565, int startpos) {
        final int redbits=5, grnbits=6, blubits=5;
        palette=new byte[256*3];
        int posRgb=0, posPal=0;
        
        while (posPal<256*3 && posRgb+startpos<rgb565.length) {
            int word;
            word=  rgb565[startpos + posRgb++]&0xff;
            word|= (rgb565[startpos + posRgb++]&0xff)<<8;
            palette[posPal+0]=(byte) ((word & ((1<<redbits)-1))<<(8-redbits));  word>>>=redbits;
            palette[posPal+1]=(byte) ((word & ((1<<grnbits)-1))<<(8-grnbits));  word>>>=grnbits;
            palette[posPal+2]=(byte) ((word & ((1<<blubits)-1))<<(8-blubits));  word>>>=blubits;
            posPal+=3;
        }
    }
    
    public void fireBufferChanged() {
        canvas = new BufferedImage(width*3, height*3, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //int pxl=(x+y)&0xff;
                int pxl=(buffer[y*width+x]&0xff);
                int rgb;
                if (palette==null) {
                    rgb=(pxl<<24) | (pxl<<16) | (pxl<<8) | pxl;
                } else {
                    rgb=((palette[pxl*3]&0xff)<<16) | ((palette[pxl*3+1]&0xff)<<8) | ((palette[pxl*3+2]&0xff)<<0);
                }
                for (int n=0; n<3; n++)
                    for (int m=0; m<3; m++)
                        canvas.setRGB(x*3+n, y*3+m, rgb);
            }
        }
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }
}
