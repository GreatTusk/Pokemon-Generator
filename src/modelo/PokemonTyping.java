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
public class PokemonTyping implements Serializable {

    private int pokemon;
    private int type1;
    private int type2;

    public PokemonTyping() {
    }

    public PokemonTyping(int pokemon, int type1, int type2) {
        this.pokemon = pokemon;
        this.type1 = type1;
        this.type2 = type2;
    }

    public int getPokemon() {
        return pokemon;
    }

    public void setPokemon(int pokemon) {
        this.pokemon = pokemon;
    }

    public int getType1() {
        return type1;
    }

    public void setType1(int type1) {
        this.type1 = type1;
    }

    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }

}
