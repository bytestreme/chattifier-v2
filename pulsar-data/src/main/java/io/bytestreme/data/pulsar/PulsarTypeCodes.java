package io.bytestreme.data.pulsar;

public interface PulsarTypeCodes {
    interface InputEventType {
        int MESSAGE_IN = -1;
        int MESSAGE_EDIT = -2;
    }
    interface OutputEventType {
        int MESSAGE_OUT = 1;
        int NOK = 2;
        int OK = 4;
    }
}
