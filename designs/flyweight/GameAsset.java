package designs.flyweight;
// flyable interface ----> which merges the intrinsic + with extrinsic.
public interface GameAsset {
    void render(GameObject object);
}
