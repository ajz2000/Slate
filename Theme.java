import java.awt.*;

public class Theme{
  private Color bg;
  private Color bg2;
  private Color bg3;
  private Color fg;
  
  public Theme(Color bg, Color bg2, Color bg3, Color fg){
    this.bg =bg;
    this.bg2 = bg2;
    this.bg3 = bg3;
    this.fg = fg;
  }
  
  public Color getBG(){
    return bg;
  }
  public Color getBG2(){
    return bg2;
  }
  public Color getBG3(){
    return bg3;
  }
  public Color getFG(){
    return fg;
  }
}