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
      SaveAs.setEnabled(true);
      }
      if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W){
        System.exit(0);
      }
      changed = true;
      Save.setEnabled(true);
      SaveAs.setEnabled(true);
    }
    
    public void keyReleased(KeyEvent e){
    
    }
  };
  
  Action Open = new AbstractAction("Open", new ImageIcon("open.gif")){
    public void actionPerformed(ActionEvent e){
      saveOld();
      if(dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
        readInFile(dialog.getSelectedFile().getAbsolutePath());
      }
      SaveAs.setEnabled(true);
    }
  };
  
  Action Save = new AbstractAction("Save", new ImageIcon("save.gif")){
    public void actionPerformed(ActionEvent e){
      if(!currentFile.equals("Untitled"))
        saveFile(currentFile);
      else
        saveFileAs();
    }
  };
  
  Action SaveAs = new AbstractAction("Save as..."){
    public void actionPerformed(ActionEvent e){
      saveFileAs();
    }
  };
  
  Action Quit = new AbstractAction("Quit"){
    public void actionPerformed(ActionEvent e){
      saveOld();
      System.exit(0);
    }
  };
  
  ActionMap m = area.getActionMap();
  Action Cut = m.get(DefaultEditorKit.cutAction);
  Action Copy = m.get(DefaultEditorKit.copyAction);
  Action Paste = m.get(DefaultEditorKit.pasteAction);
  
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
      Toolkit.getDefaultToolkit().beep();
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
      Save.setEnabled(false);
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
    //this was a test that did not work
    //scroll.getViewport().setBackground(Color.BLACK);
    scroll.setWheelScrollingEnabled(true);
    JButton jb = new JButton("france");
    jb.setBackground(bg2);
    jb.setFocusPainted(false);
    jb.setBorderPainted(false);
    add(jb,BorderLayout.WEST);
    add(scroll,BorderLayout.CENTER);
    
//    JMenuBar JMB = new JMenuBar();
//    setJMenuBar(JMB);
//    JMenu file = new JMenu("File");
//    JMenu edit = new JMenu("Edit");
//    JMB.add(file);
//    JMB.add(edit);
//    
//    //file.add(New);
//    file.add(Open);
//    file.add(Save);
//    file.add(Quit);
//    file.add(SaveAs);
//    file.addSeparator();
//    
//    for(int i = 0; i < 4; i++)
//      file.getItem(i).setIcon(null);
//    
//    edit.add(Cut);
//    edit.add(Copy);
//    edit.add(Paste);
//    
//    edit.getItem(0).setText("Cut Out");
//    edit.getItem(1).setText("Copy");
//    edit.getItem(2).setText("Paste");
//    
//    JToolBar tool = new JToolBar();
//    add(tool,BorderLayout.NORTH);
//    //tool.add(New);
//    tool.add(Open);
//    tool.add(Save);
//    tool.addSeparator();
//    
//    JButton cut = tool.add(Cut);
//    JButton cop = tool.add(Copy);
//    JButton pas = tool.add(Paste);
//    
//    cut.setText(null);
//    cop.setText(null);
//    pas.setText(null);
//    cut.setIcon(new ImageIcon("cut.gif"));
//    cop.setIcon(new ImageIcon("copy.gif"));
//    pas.setIcon(new ImageIcon("paste.gif"));
    
    Save.setEnabled(false);
    SaveAs.setEnabled(false);
    
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setUndecorated(true);
    pack();
    setLocationRelativeTo(null);
    area.addKeyListener(k1);
    setTitle(currentFile);
    setVisible(true);
  }
}