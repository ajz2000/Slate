import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.InputStream;
import java.util.ArrayList;

public class Slate extends JFrame{
  
  //private JTextArea area = new JTextArea(35,110);
  private JScrollPane editScrollPane;
  private JScrollPane menuScrollPane;
  private JPanel editPanel = new JPanel(new CardLayout());
  private JPanel menuPanel = new JPanel();
  private LayoutManager menuLayout = new BoxLayout(menuPanel, BoxLayout.Y_AXIS);
  private ArrayList<Integer> activeNumbers = new ArrayList<Integer>();
  private ArrayList<JButton> buttonList = new ArrayList<JButton>();
  private boolean fullscreen = false;
  //this is the number used when creating a card
  private int editSpaces = 0;
  //this is the total number of editspaces;
  private int numberOfEditSpaces = 0;
  private int currentCard = 1;

  //Colours
  //bg, bg2, bg3, fg
//  private Theme dark = new Theme(new Color(35,35,35),new Color(35,200,139),new Color(70,70,70),new Color(240,240,240));
//  private Theme light = new Theme(new Color(249,249,249),new Color(145,220,255),new Color(200,200,200),new Color(35,35,35));
//  private Theme flow = new Theme(new Color(101,97,118),new Color(1,111,185),new Color(248,241,255),new Color(222,205,245));
 // private Theme currentTheme = dark;
  private Theme currentTheme;
  private ArrayList<Theme> themes = new ArrayList<Theme>();
  
  private Font mainFont;

  private DragBar dragBar;
  private int fontSize = 20;
  
  public static void main(String[] args){
    new Slate();
  }
  
  public Slate(){
    //dark
    themes.add(new Theme(new Color(35,35,35),new Color(35,200,139),new Color(70,70,70),new Color(240,240,240)));
    //light
    themes.add(new Theme(new Color(249,249,249),new Color(145,220,255),new Color(200,200,200),new Color(35,35,35)));
    //abs
    themes.add(new Theme(new Color(0,0,0),new Color(255,0,0),new Color(70,70,70),new Color(255,255,255)));
    //terminal
    themes.add(new Theme(new Color(40,40,40),new Color(12,160,12),new Color(12,70,12),new Color(12,100,12)));
    //beige
    themes.add(new Theme(new Color(219,202,182),new Color(221,115,115),new Color(220,220,220),new Color(255,255,255)));
    //light 2
    themes.add(new Theme(new Color(249,249,249),new Color(105,162,176),new Color(200,200,200),new Color(221,115,115)));
    //light 2.5
    themes.add(new Theme(new Color(249,249,249),new Color(101,145,87),new Color(200,200,200),new Color(221,115,115)));  
    currentTheme = themes.get(0);
    try{
      Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File("Anonymous Pro B.ttf")).deriveFont(Font.PLAIN, fontSize);
      mainFont = newFont;
    }catch(Exception e){
      System.out.println("Font Loading Error");
    }
    
    
    dragBar = new DragBar();
    dragBar.setComponent(this);
    dragBar.setBackground(currentTheme.getBG());
    dragBar.setPreferredSize(new Dimension(1248,44));
    add(dragBar, BorderLayout.NORTH);
    
    editPanel.setBackground(Color.BLACK);
    editScrollPane = new JScrollPane(editPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    editScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    //add(editScrollPane,BorderLayout.EAST);
    add(editScrollPane, BorderLayout.CENTER);
    menuPanel.setLayout(menuLayout);
    menuPanel.setBackground(currentTheme.getBG());
    //menuPanel.setPreferredSize(new Dimension(150,300));
    menuScrollPane = new JScrollPane(menuPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    menuPanel.add(Box.createRigidArea(new Dimension(0,fontSize)));
    menuScrollPane.setPreferredSize(new Dimension(fontSize*9,300));
    menuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    add(menuScrollPane,BorderLayout.WEST);
    addEditSpace("       ");  
    
    //makes the scrollbars invisible while still allowing scrollwheel/touchpad scrolling use
    editScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
    editScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    editScrollPane.setBorder(BorderFactory.createEmptyBorder());
    editScrollPane.setWheelScrollingEnabled(true);
    menuScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
    menuScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    menuScrollPane.setBorder(BorderFactory.createEmptyBorder());
    menuScrollPane.setWheelScrollingEnabled(true);
    
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setUndecorated(true);
    pack();
    setLocationRelativeTo(null);
    setTitle("Slate");
    setVisible(true);  
    
  }
  
  
  public void addEditSpace(String name){
    editSpaces++;
    numberOfEditSpaces++;
    activeNumbers.add(editSpaces);
    MenuButton jb = new MenuButton(name);
    jb.setMaximumSize(new Dimension(200,fontSize*2));
    jb.setMinimumSize(new Dimension(200,fontSize*2));
    jb.setPreferredSize(new Dimension(200,fontSize*2));
    jb.setCard(editSpaces);
    jb.setBackground(currentTheme.getBG());
    //You should change this to make it more in line with the editor font
    jb.setFont(mainFont.deriveFont(fontSize));
    jb.setHorizontalAlignment(SwingConstants.LEFT);
    jb.setForeground(currentTheme.getBG3());
    if(editSpaces>1)
      jb.setText("untitled");
    jb.setFocusPainted(false);
    jb.setBorderPainted(false);
    
    jb.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout)editPanel.getLayout();
        cardLayout.show(editPanel,Integer.toString(jb.getCard()));
        currentCard = jb.getCard();
        for (int i = 0; i< buttonList.size(); i++){
          buttonList.get(i).setForeground(currentTheme.getBG3());
        }
        jb.setForeground(currentTheme.getBG2());
      }
    });
    
    buttonList.add(jb);
    EditSpace es = new EditSpace(this,80,30);
    editPanel.add(es,Integer.toString(editSpaces));
    menuPanel.add(jb);
    es.setMenuButton(jb);
    menuPanel.revalidate();
    CardLayout cardLayout = (CardLayout)editPanel.getLayout();
    //The error is happening here
    cardLayout.show(editPanel,Integer.toString(editSpaces));
    //this gives me the same error
    //jb.doClick();
    //the error still occurs when a try catch is put in
    es.grabFocus();
  }
  
  public int getNumberOfEditSpaces(){
    return numberOfEditSpaces;
  }
  public void toggleFullscreen(){
    fullscreen = !fullscreen;
    dragBar.toggleDraggable();
  }
  public boolean getFullscreen(){
    return fullscreen;
  }
  public ArrayList getActiveNumbers(){
    return activeNumbers;
  }
  public void decreaseNumberOfEditSpaces(){
    numberOfEditSpaces--;
  }
  public CardLayout getEditPanelLayout(){
    return (CardLayout)editPanel.getLayout();
  }
  public JPanel getEditPanel(){
    return editPanel;
  }
  public JPanel getMenuPanel(){
    return menuPanel;
  }
  public Color getFG(){
    return currentTheme.getFG();
  }
  public Color getBG(){
    return currentTheme.getBG();
  }
  public Color getBG2(){
    return currentTheme.getBG2();
  }
  public Color getBG3(){
    return currentTheme.getBG3();
  }
  public Font getMainFont(){
    return mainFont;
  }
  public void swapTheme(){
    if(themes.indexOf(currentTheme)== themes.size()-1){
      currentTheme = themes.get(0);
    } else{
    currentTheme = themes.get(themes.indexOf(currentTheme)+1);
    }
    menuPanel.setBackground(currentTheme.getBG());
    dragBar.setBackground(currentTheme.getBG());
    for (int i = 0; i < buttonList.size(); i++){
    buttonList.get(i).setBackground(currentTheme.getBG());
    buttonList.get(i).setForeground(currentTheme.getBG3());
    }
  }
  public int getFontSize(){
    return fontSize;
  }
}