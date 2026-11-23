package com.subastaweb.application;

public interface ImageStorage {

    /** Guarda los bytes y devuelve el nombre del archivo almacenado. */
    String store(byte[] content, String extension);

    void delete(String name);
}
