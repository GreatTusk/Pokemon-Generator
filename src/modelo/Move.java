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
public class Move implements Serializable {

    private int id;
    private String name;
    private int power;
    private int accuracy;
    private int pp;
    private String effect;
    private int effectProb;
    private MoveCat idMoveCat;
    private PokemonType idType;

    public Move() {
        this.id = 0;
    }

    public Move(int id, String name, String effect) {
        this.id = id;
        this.name = name;
        this.effect = effect;
    }

    public Move(int id, String name) {
        this.id = id;
        this.name = name;
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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getEffectProb() {
        return effectProb;
    }

    public void setEffectProb(int effectProb) {
        this.effectProb = effectProb;
    }

    public MoveCat getIdMoveCat() {
        return idMoveCat;
    }

    public void setIdMoveCat(MoveCat idMoveCat) {
        this.idMoveCat = idMoveCat;
    }

    public PokemonType getIdType() {
        return idType;
    }

    public void setIdType(PokemonType idType) {
        this.idType = idType;
    }

}
