package designs.flyweight;

import java.util.HashMap;
import java.util.Map;

import designs.flyweight.assets.Building;
import designs.flyweight.assets.Bullet;
import designs.flyweight.assets.Grass;
import designs.flyweight.assets.Tree;

public class GameFlyweightFactory {
    private static final Map<GameObjectType, GameAsset> cache = new HashMap<>();
    public static GameAsset get(GameObjectType type){
        if(cache.containsKey(type))return cache.get(type);

        GameAsset asset = switch(type){
            case TREE -> new Tree();
            case BUILDING -> new Building();
            case BULLET -> new Bullet();
            case GRASS -> new Grass();
        };
        cache.put(type, asset);
        return asset;
    }
}
