import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.InputStream;

public class Slate extends JFrame{
  
  private JTextArea area = new JTextArea(35,110);
  private JFileChooser dialog = new JFileChooser(System.getProperty("User.dir"));
  private String currentFile = "untitled";
  private boolean changed = false;
  //dark
  private Color bg = new Color(35,35,35);
  private Color bg2 = new Color(35,200,139);
  private Color bg3 = new Color(244,172,183);
  private Color fg = new Color(240,240,240);
  //light
  //private Color bg = new Color(249,249,249);
  //private Color fg = new Color(145,220,255);
  
  private Font mainFont;
  private boolean ctrlHeld = false;
  
  public static void main(String[] args){
    new Slate();
  }
  
  private KeyListener k1 = new KeyAdapter(){
    public void keyPressed(KeyEvent e){
      if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
        saveFileAs();
      }
      if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O){
       if(dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
        readInFile(dialog.getSelectedFile().getAbsolutePath());
      }
      }
      if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W){
        System.exit(0);
      }
      changed = true;
    }
    
    public void keyReleased(KeyEvent e){
    
    }
  };
  
  
  private void saveFileAs(){
    if(dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
      saveFile(dialog.getSelectedFile().getAbsolutePath());
  }
  
  private void saveOld(){
    if(changed){
      if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile + " ?","Save", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
        saveFile(currentFile);
    }
  }
  
  private void readInFile(String fileName){
    try{
      FileReader r = new FileReader(fileName);
      area.read(r,null);
      r.close();
      currentFile = fileName;
      setTitle(currentFile);
      changed = false;
    }
    catch(IOException e){
      JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
    }
  }
  
  private void saveFile(String fileName){
    try{
      FileWriter w = new FileWriter(fileName);
      area.write(w);
      w.close();
      currentFile = fileName;
      setTitle(currentFile);
      changed = false;
    }
    catch(IOException e){
    }
  }
  
  public Slate(){
    
    try{
      
      Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File("Anonymous Pro B.ttf")).deriveFont(Font.PLAIN, 22);
      mainFont = newFont;
    }catch(Exception e){
      System.out.println("SHIT BROKE");
    }
    
    area.setFont(mainFont);
    
    JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //makes the scrollbars invisible while still allowing scrollwheel/touchpad scrolling use
    scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
    scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    scroll.setBorder(BorderFactory.createEmptyBorder());
    area.setMargin(new Insets(44, 44, 0, 0));
    area.setWrapStyleWord(true);
    area.setBackground(bg);
    area.setForeground(fg);
    area.setCaretColor(fg);
    
    area.setLayout(new GridBagLayout());
    
    
    area.setTabSize(4);
    scroll.setWheelScrollingEnabled(true);
    JButton jb = new JButton("                 ");
    jb.setBackground(bg2);
    jb.setFocusPainted(false);
    jb.setBorderPainted(false);
    add(jb,BorderLayout.WEST);
    
    add(scroll,BorderLayout.CENTER);
    
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setUndecorated(true);
    pack();
    setLocationRelativeTo(null);
    area.addKeyListener(k1);
    setTitle(currentFile);
    setVisible(true);
  }
}