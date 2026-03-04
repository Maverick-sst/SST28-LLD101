import java.util.List;

public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) { this.reg = reg; }

    public void startClass() {
        Power pj = reg.getFirstOfType(Power.class);
        pj.powerOn();
        ConnectionCheck ck= reg.getFirstOfType(ConnectionCheck.class);
        ck.connectInput("HDMI-1");

        Brightness bt = reg.getFirstOfType(Brightness.class);
        bt.setBrightness(60);

        Temperature temp = reg.getFirstOfType(Temperature.class);
        temp.setTemperatureC(24);

        AttendanceScannable scan = reg.getFirstOfType(AttendanceScannable.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        List<Power> res = reg.getAllByCapability(Power.class);
        for(Power p : res){
            p.powerOff();
        }
    }
}
