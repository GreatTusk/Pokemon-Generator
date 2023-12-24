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
public class PokemonPaste implements Serializable {

    private int id;

    private String nickname;

    private int level;

    private boolean isShiny;

    private int hpEv;

    private int atkEv;

    private int defEv;

    private int spatkEv;

    private int spdefEv;

    private int spdEv;

    private int hpIv;

    private int atkIv;

    private int defIv;

    private int spatkIv;

    private int spdefIv;

    private int spdIv;

    private Ability ability;

    private Gender gender;

    private Item item;

    private Move move1;

    private Move move2;

    private Move move3;

    private Move move4;

    private Nature nature;

    private Pokemon pokemon;

    private PokemonType teraType;
    private int hp, atk, def, spatk, spdef, spd;
    private static int nextId = 0;

    public PokemonPaste() {
        this.id = nextId++;
    }

    public PokemonPaste(int id, String nickname, int level, boolean isShiny, int hpEv, int atkEv, int defEv, int spatkEv, int spdefEv, int spdEv, int hpIv, int atkIv, int defIv, int spatkIv, int spdefIv, int spdIv, Ability ability, Gender gender, Item item, Move move1, Move move2, Move move3, Move move4, Nature nature, Pokemon pokemon, PokemonType teraType, int hp, int atk, int def, int spatk, int spdef, int spd) {
        this.id = id;
        this.nickname = nickname;
        this.level = level;
        this.isShiny = isShiny;
        this.hpEv = hpEv;
        this.atkEv = atkEv;
        this.defEv = defEv;
        this.spatkEv = spatkEv;
        this.spdefEv = spdefEv;
        this.spdEv = spdEv;
        this.hpIv = hpIv;
        this.atkIv = atkIv;
        this.defIv = defIv;
        this.spatkIv = spatkIv;
        this.spdefIv = spdefIv;
        this.spdIv = spdIv;
        this.ability = ability;
        this.gender = gender;
        this.item = item;
        this.move1 = move1;
        this.move2 = move2;
        this.move3 = move3;
        this.move4 = move4;
        this.nature = nature;
        this.pokemon = pokemon;
        this.teraType = teraType;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spatk = spatk;
        this.spdef = spdef;
        this.spd = spd;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getSpatk() {
        return spatk;
    }

    public void setSpatk(int spatk) {
        this.spatk = spatk;
    }

    public int getSpdef() {
        return spdef;
    }

    public void setSpdef(int spdef) {
        this.spdef = spdef;
    }

    public int getSpd() {
        return spd;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getIsShiny() {
        return isShiny;
    }

    public void setIsShiny(boolean isShiny) {
        this.isShiny = isShiny;
    }

    public int getHpEv() {
        return hpEv;
    }

    public void setHpEv(int hpEv) {
        this.hpEv = hpEv;
    }

    public int getAtkEv() {
        return atkEv;
    }

    public void setAtkEv(int atkEv) {
        this.atkEv = atkEv;
    }

    public int getDefEv() {
        return defEv;
    }

    public void setDefEv(int defEv) {
        this.defEv = defEv;
    }

    public int getSpatkEv() {
        return spatkEv;
    }

    public void setSpatkEv(int spatkEv) {
        this.spatkEv = spatkEv;
    }

    public int getSpdefEv() {
        return spdefEv;
    }

    public void setSpdefEv(int spdefEv) {
        this.spdefEv = spdefEv;
    }

    public int getSpdEv() {
        return spdEv;
    }

    public void setSpdEv(int spdEv) {
        this.spdEv = spdEv;
    }

    public int getHpIv() {
        return hpIv;
    }

    public void setHpIv(int hpIv) {
        this.hpIv = hpIv;
    }

    public int getAtkIv() {
        return atkIv;
    }

    public void setAtkIv(int atkIv) {
        this.atkIv = atkIv;
    }

    public int getDefIv() {
        return defIv;
    }

    public void setDefIv(int defIv) {
        this.defIv = defIv;
    }

    public int getSpatkIv() {
        return spatkIv;
    }

    public void setSpatkIv(int spatkIv) {
        this.spatkIv = spatkIv;
    }

    public int getSpdefIv() {
        return spdefIv;
    }

    public void setSpdefIv(int spdefIv) {
        this.spdefIv = spdefIv;
    }

    public int getSpdIv() {
        return spdIv;
    }

    public void setSpdIv(int spdIv) {
        this.spdIv = spdIv;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Move getMove1() {
        return move1;
    }

    public void setMove1(Move move1) {
        this.move1 = move1;
    }

    public Move getMove2() {
        return move2;
    }

    public void setMove2(Move move2) {
        this.move2 = move2;
    }

    public Move getMove3() {
        return move3;
    }

    public void setMove3(Move move3) {
        this.move3 = move3;
    }

    public Move getMove4() {
        return move4;
    }

    public void setMove4(Move move4) {
        this.move4 = move4;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public PokemonType getTeraType() {
        return teraType;
    }

    public void setTeraType(PokemonType teraType) {
        this.teraType = teraType;
    }

}
