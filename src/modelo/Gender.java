/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author F776
 */
public class Gender implements Serializable {

    private int id;
    private String name;

    public Gender() {
        this.id = 0;
    }

    public Gender(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Gender(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Gender getGenderFromSymbol(String symbol) {
        switch (symbol) {
            case "M":
                return new Gender(1, "Male"); // Replace with your actual male gender value
            case "F":
                return new Gender(2, "Female"); // Replace with your actual female gender value
            default:
                return new Gender(3, "Other"); // Replace with your default or other gender value
        }
    }

}
