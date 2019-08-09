package net.edt.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class SymbolsGenerator {

    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    public static final String DIGITS = "0123456789";
    public static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static Random random = new SecureRandom();

    private SymbolsGenerator() { }

    public static String generate(int length, char[] symbols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(symbols[random.nextInt(symbols.length)]);
        }
        return sb.toString();
    }

}
