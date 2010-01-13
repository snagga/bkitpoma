package com.bkitmobile.poma.database.server;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
    private static final EntityManagerFactory emfInstance =
        Persistence.createEntityManagerFactory("myPersistenceUnit");

    private EMF() {}

    public static EntityManagerFactory get() {
        return emfInstance;
    }
}