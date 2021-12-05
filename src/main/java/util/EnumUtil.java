package util;

import java.util.Arrays;

public class EnumUtil {
    public static String[] getStringValues (Enum<?>[] enums) {
        return (String[]) Arrays.stream(enums).map(Enum::toString).toArray();
    }
}
