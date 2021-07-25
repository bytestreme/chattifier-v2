package io.bytestreme.data.pulsar;

public interface PulsarTypeCodes {
    interface InputEventType {
        int MESSAGE_IN = -2;
        int MESSAGE_EDIT = -4;
        int ARCHIVE_IN = -8;
        int PERSIST_REQUEST_IN = -16;
    }

    interface OutputEventType {
        int MESSAGE_OUT = 2;
        int ARCHIVE_OUT = 8;
        int PERSIST_REQUEST_OUT = 16;

        int NOK = -65536;
        int OK = 0;
    }
}
