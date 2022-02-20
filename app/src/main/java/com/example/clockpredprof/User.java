package com.example.clockpredprof;

/**
 * Класс продукции со свойствами <b>maker</b> и <b>price</b>.
 * @autor Попов Кирилл
 */
public class User {
    /** Поле имени пользователя */
    String name;
    /** Поле почты пользователя */
    String mail;
    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param name - имя
     * @param mail - почта
     */
    User(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }
}
