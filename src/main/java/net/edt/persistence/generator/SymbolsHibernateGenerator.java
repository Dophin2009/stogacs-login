package net.edt.persistence.generator;

import net.edt.util.SymbolsGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class SymbolsHibernateGenerator implements IdentifierGenerator, Configurable {

    private int length;
    private char[] symbols;

    public SymbolsHibernateGenerator() { }

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
        return SymbolsGenerator.generate(length, symbols);
    }

}
