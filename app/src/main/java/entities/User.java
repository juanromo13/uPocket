package entities;

public class User {
    private String uNombre;

    public User() {  }

    public User(String uNombre) { this.uNombre = uNombre; }

    public String getuNombre() { return uNombre; }

    public void setuNombre(String uNombre) { this.uNombre = uNombre; }
}
