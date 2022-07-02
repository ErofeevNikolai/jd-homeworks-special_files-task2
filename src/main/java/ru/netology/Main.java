package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
    }

    /*
    Метод возвращаю список объектов их XML
    */
    private static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> listEmploye = new ArrayList<>();

        // 1. Создаем парсер для считывания переданного в метод файла и загружаем его в оперативную память
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        // 2. получаем корневой узел документа.
        Node root = doc.getDocumentElement();
        // 3. получаем список дочерних узлов корневого узла.
        NodeList nodeList = root.getChildNodes();
        // 4. Пробегаем по всем дочерним узлам
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            // 5. Если тип дочернего узла является Элементом по создаем объект получая его значения полей из узла
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                Employee employee = new Employee(
                        Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent()),
                        element.getElementsByTagName("firstName").item(0).getTextContent(),
                        element.getElementsByTagName("lastName").item(0).getTextContent(),
                        element.getElementsByTagName("country").item(0).getTextContent(),
                        Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent())
                );
                listEmploye.add(employee);
            }
        }
        return listEmploye;
    }


    /*
    Метод записывающий список сотрудников Json файла
     */
    private static String listToJson(List<Employee> list) {

        /*
        1. Создаем билдер
        2. С помощью бидлера создаем объект типа
        3. Для преобразования списка объектов Json определяем тип списка (получаем название полей для сопоставления).
        4. Создаем строку в которую сериализуем объект gson с помощью метода toJson()
        5. Создаем файл Json, в которую записываем строку
         */
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        try (FileWriter file = new FileWriter("new_data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}