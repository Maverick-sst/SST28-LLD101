package designs.flyweight;

public class GameEngine {
    public static void main(String[] args) {
        GameAsset asset1 = GameFlyweightFactory.get(GameObjectType.BUILDING); // intrinsic
        GameObject object1 = new GameObject(10, 5, 1);   // extrinsic
        asset1.render(object1);


        GameAsset asset2 = GameFlyweightFactory.get(GameObjectType.BUILDING);
        GameObject object2 = new GameObject(5, 5, 1);
        asset2.render(object2);

        System.out.println( object1.hashCode() == object2.hashCode()); // extrinsic
        System.out.println(asset1 == asset2);  // intrinsic

    }
}
