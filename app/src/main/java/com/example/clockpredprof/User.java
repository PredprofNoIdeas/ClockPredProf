package com.example.clockpredprof;

/**
 * Класс продукции со свойствами <b>maker</b> и <b>price</b>.
 * @autor Пустовалов Данил
 */
public class User {
    /** Поле имени пользователя */
    String name;
    /** Поле почты пользователя */
    String city;
    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param name - имя
     * @param city - почта
     */
    User(String name, String city) {
        this.name = name;
        this.city = city;
    }
}
