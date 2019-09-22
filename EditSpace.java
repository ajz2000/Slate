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
import javax.swing.undo.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditSpace extends JTextArea{
  
  private String currentFile = "untitled";  
  private boolean changed = false; 
  private Slate slate;
  private JFileChooser dialog = new JFileChooser(System.getProperty("User.dir"));
  private MenuButton menuButton;
  private UndoManager manager;
  private String lastWord;
  private EditSpace thisEditSpace;
    
  public EditSpace(Slate slate, int col, int row){
    
    super(row,col);
    thisEditSpace = this;
    
    getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                  getLastWord();
                  System.out.println(lastWord);
                  userActionCheck();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                  getLastWord();
                  System.out.println(lastWord);
                  userActionCheck();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                  getLastWord();
                  System.out.println(lastWord);
                  userActionCheck();
                }
                
                public void getLastWord(){
                  //this looks incredibly goody but it actually works
                  //find a better solution when you have time
                  try{
                    int startOfWord = Utilities.getWordStart(thisEditSpace,thisEditSpace.getCaretPosition());
                    int endOfWord = Utilities.getWordEnd(thisEditSpace,thisEditSpace.getCaretPosition());
                    lastWord = thisEditSpace.getDocument().getText(startOfWord, endOfWord - startOfWord);
                  } catch (Exception e){
                    try{
                    int startOfWord = Utilities.getWordStart(thisEditSpace,thisEditSpace.getCaretPosition() - 1);
                    int endOfWord = Utilities.getWordEnd(thisEditSpace,thisEditSpace.getCaretPosition() - 1);
                    lastWord = thisEditSpace.getDocument().getText(startOfWord, endOfWord - startOfWord);
                    }catch(Exception f){}
                  }
                }
    });
        
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
        setBackground(slate.getBG());
        setForeground(slate.getFG());
        setCaretColor(slate.getFG());
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
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F){
          if(!slate.getFullscreen()){
            slate.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
          } else{
            slate.setSize(1248,748);
            revalidate();
          }
          slate.toggleFullscreen();
          slate.setLocationRelativeTo(null);
        }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
          if(e.isShiftDown()){
            if(manager.canRedo()){
              manager.redo();
            }
          } else{
            if(manager.canUndo()){
              manager.undo();
            }
          }
        }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Q){
        System.exit(0);
        }
        else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_T){
        slate.swapTheme();
        setBackground(slate.getBG());
        menuButton.setForeground(slate.getBG2());
        setForeground(slate.getFG());
        setCaretColor(slate.getFG());
        }
      }
    });
    
    
    this.slate = slate;
    setMargin(new Insets(slate.getFontSize()*2, slate.getFontSize()*2, slate.getFontSize()*2, slate.getFontSize()*2));
    setWrapStyleWord(true);
    setLineWrap(true);
    setBackground(slate.getBG());
    setForeground(slate.getFG());
    setCaretColor(slate.getFG());
    setFont(slate.getMainFont());
    setTabSize(4);
    grabFocus();
    
    manager = new UndoManager();
    getDocument().addUndoableEditListener(manager);
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
      getDocument().addUndoableEditListener(manager);
    }
    catch(IOException e){
      JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
    }
  }
  
  private void close(){
    slate.getEditPanelLayout().removeLayoutComponent(this);
    int positionInList = (int)slate.getActiveNumbers().indexOf(menuButton.getCard());
    int toDisplay;
    
    if(positionInList == 0){
      toDisplay = (int)slate.getActiveNumbers().get(1);
    } else
    {
      toDisplay = (int)slate.getActiveNumbers().get(positionInList -1);
    }
    slate.getEditPanelLayout().show(slate.getEditPanel(),Integer.toString(toDisplay));
    slate.getActiveNumbers().remove(slate.getActiveNumbers().indexOf(menuButton.getCard()));
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
  public void userActionCheck(){
    if(lastWord.equals("cmdexit")){
      if(slate.getNumberOfEditSpaces() == 1){
            System.exit(0);
          }
          else{
            close();
          }
    }
    else if(lastWord.equals("cmdtheme")){
      slate.swapTheme();
        setBackground(slate.getBG());
        menuButton.setForeground(slate.getBG2());
        setForeground(slate.getFG());
        setCaretColor(slate.getFG());
    }
  }
}