package utils;

import java.util.ArrayList;
import java.util.List;

public class ByteArraysUtils {
    public static List<Byte> toList(byte[] byteArray) {
        List<Byte> byteList = new ArrayList<>();
        for(byte temp: byteArray) {
            byteList.add(temp);
        }
        return byteList;
    }

    public static byte[] toArray(List<Byte> byteList) {
        byte[] byteArray = new byte[byteList.size()];
        for(int i = 0; i < byteArray.length; i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }
}
