import javax.swing.*;

public class MenuButton extends JButton{
  
  private int respectiveCard;
  
  public MenuButton(String text){
  super(text);
  }
  
  public void setCard(int card){
    respectiveCard = card;
  }
  
  public int getCard(){
    return respectiveCard;
  }
  
  public void removeFrom(JPanel jPanel){
    jPanel.remove(this);
    jPanel.revalidate();
    jPanel.repaint();
  }
}