package designs.flyweight.assets;

import designs.flyweight.GameAsset;
import designs.flyweight.GameObject;

public class Building implements GameAsset{
    private final String image;
    public Building(){
        this.image = "building";
    }
    @Override
    public void render(GameObject object) {
        System.out.println(
            "RENDERING BUILDING AT :"+ 
            object.getX() + "," + object.getY() 
            + " weight: " + object.getWeight()
        );
    }
}
