public class AirConditioner implements SmartClassroomDevice ,Power, Temperature {
    @Override public void powerOn() { /* ok */ }
    @Override public void powerOff() { System.out.println("AC OFF"); }
    @Override public void setTemperatureC(int c) { System.out.println("AC set to " + c + "C"); }
}
