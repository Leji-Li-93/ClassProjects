package shapes;

import java.awt.*;

public enum MyColor {
    Red(255, 0, 0),
    FireBrick(178,34,34),
    IndianRed(205,92,92),
    LightCoral(240,128,128),
    LightPink(255,182,193),
    Pink(255,192,203),
    Green(0,255,0	),
    LightGreen(144,238,144),
    PaleGreen(152,251,152),
    OliveDrab(85,107,47),
    MediumAquamarine(0,250,154),
    Turquoise(64,224,208),
    Blue(0,0,255),
    DarkBlue(0,0,139),
    RoyalBlue(65,105,225),
    SkyBlue(135,206,235),
    Azure(240,255,255),
    White(255,255,255),
    Black(0,0,0),
    Gray(128,128,128),
    LightGray(211,211,211),
    Yellow(	255,255,0);


    private int r, g, b;

    private MyColor(){
        this(0, 0,0);
    }

    private MyColor(int r, int g, int b){
        setColor(r, g, b);
    }

    public Color toAWTColor(){
        return new Color(r, g, b);
    }

    public javafx.scene.paint.Color toFXPaintColor(){
        return javafx.scene.paint.Color.rgb(r, g, b);
    }

    public void setColor(int hex){
        this.r = (hex & 0xFF0000) >> 16;
        this.g = (hex & 0xFF00) >> 8;
        this.b = hex & 0xFF;
    }

    public void setColor(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getHexColor(){
        return ((0xFF0000 & (r << 16)) | (0x00FF00 & (g << 8)) |b);
    }
}
