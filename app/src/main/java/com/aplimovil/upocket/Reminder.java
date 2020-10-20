package com.aplimovil.upocket;

public class Reminder {
    private String recordatorio;
    private String valorRecordatorio;

    public Reminder(String recordatorio, String valorRecordatorio) {
        this.recordatorio = recordatorio;
        this.valorRecordatorio = valorRecordatorio;
    }

    public String getRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(String recordatorio) {
        this.recordatorio = recordatorio;
    }

    public String  getValorRecordatorio() {
        return valorRecordatorio;
    }

    public void setValorRecordatorio(String valorRecordatorio) {
        this.valorRecordatorio = valorRecordatorio;
    }
}
