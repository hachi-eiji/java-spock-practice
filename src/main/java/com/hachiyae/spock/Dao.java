package com.hachiyae.spock;

import org.springframework.stereotype.Service;

@Service
public class Dao {
    private static DaoFactory factory;
    public static DaoFactory getFactory() {
        return factory;
    }
}
