package designs.flyweight.assets;

import designs.flyweight.GameAsset;
import designs.flyweight.GameObject;

public class Bullet implements GameAsset{
    private final String image;
    public Bullet(){
        this.image = "bullet";
    }
    @Override
    public void render(GameObject object) {
        System.out.println(
            "RENDERING BULLET AT :"+ 
            object.getX() + "," + object.getY() 
            + " weight: " + object.getWeight()
        );
    }
}
