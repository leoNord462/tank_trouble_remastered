import javax.swing.ImageIcon;

public class BiggerBullets extends PowerUp{

    public BiggerBullets(GamePanel gPanel, int xCoord, int yCoord) {
        super(gPanel, xCoord, yCoord, (new ImageIcon("assets/entities/powerup/bullet.png")).getImage());
    }

    @Override
    void activateEffect(Tank player) {
        player.bulletAmplifier++;
    }
    
}