import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class DragBar extends JPanel{
   private MouseEvent pressed;
  private Point location;
  private Component toDrag;
  private boolean isDraggable = true;
  
  public DragBar(){
          super();    
    
        addMouseListener(new MouseListener(){
      
      @Override
      public void mouseClicked(MouseEvent e){
      }
      @Override
      public void mouseEntered(MouseEvent e) {}  
      @Override
      public void mouseExited(MouseEvent e) {}  
      @Override
      public void mousePressed(MouseEvent e) {
        pressed = e;
      }  
      @Override
      public void mouseReleased(MouseEvent e) {}  
    });
        addMouseMotionListener(new MouseMotionListener(){
      @Override
      public void mouseMoved(MouseEvent e){
        
      }
      @Override
      public void mouseDragged(MouseEvent e){
        if(isDraggable){
        location = toDrag.getLocation(location);
        int x = location.x - pressed.getX() + e.getX();
        int y = location.y - pressed.getY() + e.getY();
        toDrag.setLocation(x, y);
        }
      }
    });

  }
  public void setComponent(Component toDrag){
    this.toDrag = toDrag;
  }
  
  public void toggleDraggable(){
    isDraggable = !isDraggable;
  }
}