package util;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.*;

public class HibernateUtil {
public static final SessionFactory sessionFactory;
static {
try { // Cr�ation de la SessionFactory � partir de hibernate.cfg.xml
	StandardServiceRegistry standardRegistry = new
StandardServiceRegistryBuilder().configure().build();
sessionFactory = new
Configuration().buildSessionFactory(standardRegistry);
} catch (Throwable ex) {
System.err.println("Initial SessionFactory creation failed." + ex);throw new ExceptionInInitializerError(ex);
}
}
public static SessionFactory getSessionFactory() {
return sessionFactory;
}
}