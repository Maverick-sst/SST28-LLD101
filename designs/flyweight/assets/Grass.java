package designs.flyweight.assets;

import designs.flyweight.GameAsset;
import designs.flyweight.GameObject;

public class Grass implements GameAsset {
    private final String image;
    public Grass(){
        this.image = "grass";
    }
    @Override
    public void render(GameObject object) {
        System.out.println(
            "RENDERING GRASS AT :"+ 
            object.getX() + "," + object.getY() 
            + " weight: " + object.getWeight()
        );
    }
}
