import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.InputStream;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.dnd.*;
import java.util.List;
import java.awt.datatransfer.DataFlavor;

public class EditSpace extends JTextArea{
  
  private String currentFile = "untitled";  
  private boolean changed = false; 
  private Slate slate;
  private JFileChooser dialog = new JFileChooser(System.getProperty("User.dir"));
  private MenuButton menuButton;
  
  public EditSpace(Slate slate, int col, int row){
    
    super(row,col);

      setDropTarget(new DropTarget() {
      public synchronized void drop(DropTargetDropEvent evt) {
        try {
          evt.acceptDrop(DnDConstants.ACTION_COPY);
          List<File> droppedFiles = (List<File>)
            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
          for (File file : droppedFiles) {
            readInFile(file.toString());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    
    addComponentListener ( new ComponentAdapter (){
      public void componentShown ( ComponentEvent e ){
        grabFocus();
      }
      
      public void componentHidden ( ComponentEvent e ){
        menuButton.setForeground(slate.getBG3());
      }
    } );
    
    addFocusListener(new FocusListener(){
      
      @Override
      public void focusGained(FocusEvent e){
        menuButton.setForeground(slate.getBG2());
      }
      
      @Override
      public void focusLost(FocusEvent e){
      }
    });
    
    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }
      @Override
      public void keyReleased(KeyEvent e) {
      }
      @Override
      public void keyPressed(KeyEvent e) {
        changed = true;
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W){
          if(slate.getNumberOfEditSpaces() == 1){
            System.exit(0);
          }
          else{
            close();
          }
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S && e.isShiftDown()){
          saveFileAs(); 
        }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
          if(currentFile != "untitled"){
            saveFile(currentFile);
          } else{
            saveFileAs();
          }
        }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O){
          if(dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            readInFile(dialog.getSelectedFile().getAbsolutePath());
            
          }
        }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N){
          if(currentFile == "untitled"){
            menuButton.setText("untitled");
            menuButton.setForeground(slate.getBG3());
          }
          slate.addEditSpace("       ");
        }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_M){
          slate.setMinimumSize(new Dimension(1920,1080));
          slate.setLocationRelativeTo(null);
      }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F){
          if(!slate.getFullscreen()){
            slate.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
          } else{
            slate.setSize(1248,748);
          }
          slate.toggleFullscreen();
        slate.setLocationRelativeTo(null);
        }
      }
    });
    
    
    this.slate = slate;
    setMargin(new Insets(44, 44, 44, 44));
    setWrapStyleWord(true);
    setLineWrap(true);
    setBackground(slate.getBG());
    setForeground(slate.getFG());
    setCaretColor(slate.getFG());
    setFont(slate.getMainFont());
    setTabSize(4);
    grabFocus();
  }
  
  private void saveFile(String fileName){
    try{
      FileWriter w = new FileWriter(fileName);
      write(w);
      w.close();
      currentFile = fileName;
      changed = false;
    }
    catch(IOException e){
    }
  }
  
  private void readInFile(String fileName){
    try{
      FileReader r = new FileReader(fileName);
      read(r,null);
      r.close();
      currentFile = fileName;
      changed = false;
      File f = new File(currentFile);
      if(currentFile.contains(".")){
      menuButton.setText(f.getName().substring(0, f.getName().indexOf('.')));
      }else{
        menuButton.setText(f.getName());
      }
    }
    catch(IOException e){
      JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
    }
  }
  
  private void close(){
    slate.getEditPanelLayout().removeLayoutComponent(this);
    slate.getActiveNumbers().remove(slate.getActiveNumbers().indexOf(menuButton.getCard()));
    int firstElement = (int)slate.getActiveNumbers().get(0);
    slate.getEditPanelLayout().show(slate.getEditPanel(),Integer.toString(firstElement));
    menuButton.removeFrom(slate.getMenuPanel());
    slate.decreaseNumberOfEditSpaces();  
  }
  
  private void saveFileAs(){
    if(dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
      saveFile(dialog.getSelectedFile().getAbsolutePath());
      File f = new File(currentFile);
      if(currentFile.contains(".")){
      menuButton.setText(f.getName().substring(0, f.getName().indexOf('.')));
      }else{
        menuButton.setText(f.getName());
      }
    }
  }  
  
  public void setMenuButton(MenuButton menuButton){
    //this stuff isn't in the constructor because I was too lazy to figure out super calling stuff
    this.menuButton = menuButton;
    menuButton.setForeground(slate.getBG2());
  }
  
}