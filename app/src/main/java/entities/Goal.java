package entities;

public class Goal {
    private String meta;
    private int precio;
    private String uId;

    public Goal(String deseo, int precio, String uId) {
        this.meta = deseo;
        this.precio = precio;
        this.uId = uId;
    }

    public String getMeta() { return meta; }

    public void setMeta(String meta) { this.meta = meta; }

    public int getPrecio() { return precio; }

    public void setPrecio(int precio) { this.precio = precio; }

    public String getuId() { return uId; }

    public void setuId(String uId) { this.uId = uId; }
}
