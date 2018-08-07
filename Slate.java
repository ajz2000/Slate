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
  //dark
  private Theme dark = new Theme(new Color(35,35,35),new Color(35,200,139),new Color(70,70,70),new Color(240,240,240));
  private Theme light = new Theme(new Color(249,249,249),new Color(145,220,255),new Color(200,200,200),new Color(35,35,35));
  private Theme currentTheme = dark;
  
  private Font mainFont;
  
  public static void main(String[] args){
    new Slate();
  }
  
  public Slate(){
    
    try{
      Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File("Anonymous Pro B.ttf")).deriveFont(Font.PLAIN, 22);
      mainFont = newFont;
    }catch(Exception e){
      System.out.println("SHIT BROKE");
    }
    
    
    
    
    editPanel.setBackground(Color.BLACK);
    editScrollPane = new JScrollPane(editPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    editScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    //add(editScrollPane,BorderLayout.EAST);
    add(editScrollPane, BorderLayout.CENTER);
    menuPanel.setLayout(menuLayout);
    menuPanel.setBackground(currentTheme.getBG());
    //menuPanel.setPreferredSize(new Dimension(150,300));
    menuScrollPane = new JScrollPane(menuPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    menuPanel.add(Box.createRigidArea(new Dimension(0,22)));
    menuScrollPane.setPreferredSize(new Dimension(200,300));
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
    jb.setMaximumSize(new Dimension(200,44));
    jb.setMinimumSize(new Dimension(200,44));
    jb.setPreferredSize(new Dimension(200,44));
    jb.setCard(editSpaces);
    jb.setBackground(currentTheme.getBG());
    jb.setFont(mainFont.deriveFont(20f));
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
    cardLayout.show(editPanel,Integer.toString(editSpaces));
    es.grabFocus();
  }
  
  public int getNumberOfEditSpaces(){
    return numberOfEditSpaces;
  }
  public void toggleFullscreen(){
    fullscreen = !fullscreen;
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
    if (currentTheme == dark){
      currentTheme = light;
    }
    else{
      currentTheme = dark;
    }
    menuPanel.setBackground(currentTheme.getBG());
    for (int i = 0; i < buttonList.size(); i++){
    buttonList.get(i).setBackground(currentTheme.getBG());
    buttonList.get(i).setForeground(currentTheme.getBG3());
    }
  }
}