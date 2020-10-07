package com.aplimovil.upocket;

public class Goal {
    private String meta;
    private String precio;
    private String restante;

    public Goal(String deseo, String precio, String restante) {
        this.meta = deseo;
        this.precio = precio;
        this.restante = restante;
    }

    public String getMeta() {
        return meta;
    }

    public String getPrecio() {
        return precio;
    }

    public String getRestante() {
        return restante;
    }

}
