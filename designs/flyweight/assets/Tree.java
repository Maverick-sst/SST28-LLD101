package designs.flyweight.assets;

import designs.flyweight.GameAsset;
import designs.flyweight.GameObject;

public class Tree implements GameAsset{
    private final String image;
    public Tree(){
        this.image = "tree";
    }
    @Override
    public void render(GameObject object) {
        System.out.println(
            "RENDERING TREE AT :"+ 
            object.getX() + "," + object.getY() 
            + " weight: " + object.getWeight()
        );
    }
}
