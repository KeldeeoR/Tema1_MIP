package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ConfigLoader {

    /**
     * Încarcă config.json și returnează un obiect AppConfig.
     * Aruncă excepții dacă fișierul lipsește sau este corupt.
     */
    public static AppConfig load(String path) throws FileNotFoundException, JsonSyntaxException {

        File f = new File(path);

        // 1. Verificăm dacă fișierul există
        if (!f.exists()) {
            throw new FileNotFoundException("Fisierul de configurare '" + path + "' nu exista.");
        }

        // 2. Parsăm JSON-ul (poate arunca JsonSyntaxException)
        Gson gson = new Gson();
        FileReader reader = new FileReader(f);

        return gson.fromJson(reader, AppConfig.class);
    }
}
