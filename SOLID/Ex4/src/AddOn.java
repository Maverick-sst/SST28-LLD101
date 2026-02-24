public enum AddOn {
    MESS(1000.0),
    LAUNDRY(500.0),
    GYM(300.0);

    private final double amount;
    AddOn(double amount){
        this.amount=amount;
    }

    public double price(){
        return this.amount;
    }
}
