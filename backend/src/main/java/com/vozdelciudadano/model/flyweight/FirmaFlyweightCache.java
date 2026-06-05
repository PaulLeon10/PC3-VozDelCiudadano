package com.vozdelciudadano.model.flyweight;

import java.util.HashMap;
import java.util.Map;


public class FirmaFlyweightCache {
    private static final Map<Long,MetadataPropuestaFlyweight> cache = new HashMap<>();

    public static MetadataPropuestaFlyweight obtenerFlyweight(Long propuestaId, String titulo){
        if(!cache.containsKey(propuestaId)){
            cache.put(propuestaId, new MetadataPropuestaFlyweight(propuestaId, titulo));
        }
        return cache.get(propuestaId);
    }
}
