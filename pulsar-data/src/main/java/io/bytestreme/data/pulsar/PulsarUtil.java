package io.bytestreme.data.pulsar;

import java.util.UUID;
import java.util.function.Function;

public class PulsarUtil {
    private final static String NAME_ID_SEPARATOR = "__";

    public static String buildRandomNameForId(int id) {
        return UUID.randomUUID() + NAME_ID_SEPARATOR + id;
    }

    public static int getIdFromNameWithUUID(String name) {
        return Integer.parseInt(name.substring(name.indexOf(NAME_ID_SEPARATOR) + NAME_ID_SEPARATOR.length()));
    }

    public static Function<String, Integer> getIdFromNameWithUUID() {
        return PulsarUtil::getIdFromNameWithUUID;
    }

}
