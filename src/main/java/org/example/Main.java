package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Задача 1
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age",};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "C:/Users/Юрий/IdeaProjects/NetologyCSV_1/data.json");
        // Задача 2
        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2, "C:/Users/Юрий/IdeaProjects/NetologyCSV_1/data2.json");
        // Задача 3
        String json3 = readString("data.json");
        List<Employee> list3 = jsonToList(json3);
        list3.forEach(System.out::println);
    }

    public static List parseCSV(String[] cm, String name) {
        List list = null;
        try (CSVReader reader = new CSVReader(new FileReader(name))) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setColumnMapping(cm);
            strategy.setType(Employee.class);
            CsvToBean csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String s, String path) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Employee> parseXML(String name) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(name));
            //
            Node root = doc.getDocumentElement();
            //
            List<List<String>> list1 = read(root);
            for (List<String> list2 : list1) {
                Employee employee = new Employee(
                        Integer.parseInt(list2.get(0)),
                        list2.get(1),
                        list2.get(2),
                        list2.get(3),
                        Integer.parseInt(list2.get(4))
                );
                list.add(employee);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private static List<List<String>> read(Node node) {
        List<List<String>> list = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        // для выведения в консоль
        //  System.out.println("Корневой элемент: " + node.getNodeName());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                // для выведения в консоль
                //  System.out.println("Текущий узел: " + node_.getNodeName());
                Element element = (Element) node_;
                NodeList map2 = element.getChildNodes();
                List<String> arr = new ArrayList<>();
                for (int a = 0; a < map2.getLength(); a++) {
                    if (Node.ELEMENT_NODE == map2.item(a).getNodeType()) {
                        // для выведения в консоль
                        // String attrName = map2.item(a).getNodeName();
                        // String attrValue = map2.item(a).getTextContent();
                        // System.out.println("Атрибут: " + attrName + "; значение: " + attrValue);
                        arr.add(map2.item(a).getTextContent());
                    }
                }
                list.add(arr);
            }
        }
        return list;
    }

    public static String readString(String path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return sb.toString();
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> result = new ArrayList<>();
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        try {
            JSONArray array = (JSONArray) parser.parse(json);
            for (Object o : array) {
                Employee employee = gson.fromJson(String.valueOf(o), Employee.class);
                result.add(employee);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


}