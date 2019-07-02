package net.edt.persistence.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

public class AlphanumericGenerator implements IdentifierGenerator, Configurable {

    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    public static final String DIGITS = "0123456789";
    public static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private int length;
    private Random random;
    private char[] symbols;

    public AlphanumericGenerator() {
        this.random = new SecureRandom();
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) {
        length = Integer.parseInt(properties.getProperty("length"));
        if (length < 5) {
            throw new IllegalArgumentException("Length must be at least 5");
        }

        symbols = properties.getProperty("symbols").toCharArray();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o)
            throws HibernateException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(symbols[random.nextInt(symbols.length)]);
        }
        return sb.toString();
    }

}
