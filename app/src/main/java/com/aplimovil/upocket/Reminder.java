package com.aplimovil.upocket;

public class Reminder {
    private String recordatorio;
    private int valorRecordatorio;

    public Reminder(String recordatorio, int valorRecordatorio) {
        this.recordatorio = recordatorio;
        this.valorRecordatorio = valorRecordatorio;
    }

    public String getRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(String recordatorio) {
        this.recordatorio = recordatorio;
    }

    public int getValorRecordatorio() {
        return valorRecordatorio;
    }

    public void setValorRecordatorio(int valorRecordatorio) {
        this.valorRecordatorio = valorRecordatorio;
    }
}
