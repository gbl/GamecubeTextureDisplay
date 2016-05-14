/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.guntram.gamecubetexturedisplay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamecubeTextureDisplay implements ActionListener , MouseMotionListener {

    JCanvas canvas=null;
    JPanel frame;
    JLabel imgnoLabel;
    JLabel sizeLabel;
    JLabel mouseLabel1, mouseLabel2;
    int imageNumber=0;
    int width=32, height=32;
    
    GamecubeTextureReader texReader = null;

    public void loadImage(long filePos, int w, int h) {
        byte[] palette=texReader.getPalette();
        if (canvas != null)
            frame.remove(canvas);
        canvas=new JCanvas(w, h);
        canvas.setPalette(palette, 0);
//        try {
            texReader.readImage(filePos, canvas.getBuffer(), w, h);
//        } catch (IOException ex) {
//            Arrays.fill(canvas.getBuffer(), (byte)0);
//        }
        canvas.fireBufferChanged();
//        canvas.addMouseMotionListener(this);
        frame.add(canvas);
        frame.setSize(new Dimension(w, h));
        frame.revalidate();
        JComponent x=frame;
        while (x.getParent()!=null && !x.getParent().getClass().getSimpleName().equals("JDialog")) {
            x=(JComponent) x.getParent();
            // System.out.println(x);
        }
        if (x!=null && x.getParent()!=null)
            ((JDialog)x.getParent()).pack();
        
        imgnoLabel.setText(Long.toHexString(filePos));
        sizeLabel.setText(""+w+"/"+h);
    }

    
    public static void main(String[] args) {
        new GamecubeTextureDisplay().doStuff(args);
    }

    public void doStuff(String[] args) {
        String filename="AFS73DB.tmp_0.bin";

        frame=new JPanel();
        frame.setLayout(new BorderLayout());
        JPanel controls=new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        frame.add(controls, BorderLayout.LINE_START);
        JButton prevImage=new JButton("<--");
        prevImage.setActionCommand("prev");
        prevImage.addActionListener(this);
        JButton nextImage=new JButton("-->");
        nextImage.setActionCommand("next");
        nextImage.addActionListener(this);
        imgnoLabel=new JLabel();
        sizeLabel=new JLabel();
        mouseLabel1=new JLabel();
        mouseLabel2=new JLabel();
        controls.add(prevImage);
        controls.add(nextImage);
        controls.add(imgnoLabel);
        controls.add(sizeLabel);
        controls.add(mouseLabel1);
        controls.add(mouseLabel2);

        if (args.length==4) {
            try {
                filename=args[0];
                width=Integer.parseInt(args[1]);
                height=Integer.parseInt(args[2]);
                imageNumber=Integer.parseInt(args[3]);
            } catch (Exception ex) {
                System.err.println("Cannot Parse arguments");
                System.err.println("Usage: ImageViewer filename imageindex");
                System.exit(1);
            }
        }
        
        try {
            texReader = new GamecubeTextureReader(filename);
        } catch (IOException e) {
            System.err.println("Cannot read Image");
            System.exit(1);
        }
        
        loadImage(imageNumber*(width*height)+0x240, width, height);

        JOptionPane.showMessageDialog(null, frame, 
            "Decoded image",
            JOptionPane.PLAIN_MESSAGE);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if ("prev".equals(e.getActionCommand())) {
//            do {
                if (--imageNumber<=0)
                    imageNumber=0 /* texReader.getImageCount()-1 */;
//            } while (texReader.getImageWidth(imageNumber)==0);
            loadImage(imageNumber*(width*height)+0x240, width, height);
        }
        if ("next".equals(e.getActionCommand())) {
//            do {
//                if (++imageNumber>=datReader.getImageCount())
//                    imageNumber=0;
//            } while (datReader.getImageWidth(imageNumber)==0);
            ++imageNumber;
            loadImage(imageNumber*(width*height)+0x240, width, height);
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        mouseLabel1.setText("x="+e.getX()/3);
        mouseLabel2.setText("y="+e.getY()/3);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseLabel1.setText("x="+e.getX()/3);
        mouseLabel2.setText("y="+e.getY()/3);
    }

}
