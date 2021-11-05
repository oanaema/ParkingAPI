package com.mystartup.parkingApi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Classe utilitaire qui a le but de parser un json resultat d'un search des parkings et de trouver la liste des parkings
 */
public class JsonParsing {

    public static  void parse(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //Parse le fichier json avec les donnes de test.
        String json = new String(Files.readAllBytes(Paths.get("parkingsTempsReel.json")));

        Map<String, Object> map = mapper.readValue(json, Map.class);

        ArrayList<Object> container = new ArrayList<>();
        //Essaie de trouver la liste de parkings en cherchent un mot cle.
        List<?> value = find(map, "geometry", container);
    }

    private static List<?> find(Map<String, Object> map, String search, ArrayList<Object> container) {
        int i = 0;
        for (Object s : map.values()) {
            i++;
            if (s instanceof ArrayList) {
                if(((ArrayList<?>) s).contains(search))
                    container.addAll((Collection<?>) s);
                return (List<?>) s;
            }
        }
        return Collections.emptyList();
    }
}
