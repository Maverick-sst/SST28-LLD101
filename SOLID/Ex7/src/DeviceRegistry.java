import java.util.*;

public class DeviceRegistry {
    private final java.util.List<SmartClassroomDevice> devices = new ArrayList<>();

    public void add(SmartClassroomDevice d) { devices.add(d); }

    public <T>T getFirstOfType(Class<T> capability) {
        for (SmartClassroomDevice d : devices) {
            if(capability.isInstance(d)){
                return capability.cast(d);
            }      
        }
        throw new IllegalStateException("Missing: No device with capability " + capability.getSimpleName() );
    }

    public <T> List<T> getAllByCapability(Class<T> capability){
         List<T> res = new ArrayList<>();
         for(SmartClassroomDevice d : devices){
            if(capability.isInstance(d)){
                res.add(capability.cast(d));
            }
         }
         return res;

    }
}
