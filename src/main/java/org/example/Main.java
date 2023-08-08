package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main <inputFilePath> <Search term>");
            return;
        }

        String inputFilePath = args[0];

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File inputFile = new File(inputFilePath);
            JsonNode jsonNode = objectMapper.readTree(inputFile);
            JsonNode entries = jsonNode.get("entries");
            List<ApiInfo> apiInfoList = convertJsonNodeToList(entries,ApiInfo.class);

            String searchString = args[1];
            System.out.println("Searching.. " + searchString);
            for (ApiInfo apiInfo : apiInfoList) {
                if (apiInfo.getApi().contains(searchString) || apiInfo.getDescription().contains(searchString)) {
                    System.out.println("API: " + apiInfo.getApi());
                    System.out.println("URL: " + apiInfo.getLink());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility function to convert JsonNode to ApiInfo Pojo
     * @param jsonNode
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> List<T> convertJsonNodeToList(JsonNode jsonNode, Class<T> clazz) {
        List<T> pojoList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        if (jsonNode.isArray()) {
            for (JsonNode elementNode : jsonNode) {
                T pojo = objectMapper.convertValue(elementNode, clazz);
                pojoList.add(pojo);
            }
        }

        return pojoList;
    }
}
