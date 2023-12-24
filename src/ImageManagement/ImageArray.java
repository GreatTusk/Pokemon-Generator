/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageManagement;

import Interface.IDefaultAttributes;
import java.awt.MediaTracker;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import modelo.Pokemon;

/**
 *
 * @author F776
 */
public abstract class ImageArray implements IDefaultAttributes {

    private final HashMap<Integer, ImageIcon> pokemonList = new HashMap<>();
    ;
    private final HashMap<Integer, ImageIcon> typeList = new HashMap<>();

    ;
    
    public ImageArray() {
    }

    /**
     * This method sets all ImageIcons for the 18 Pokémon types.
     */
    public void setTypeIconArray() {
        for (int id = 1; id <= 18; id++) {
            String imagePath = "images/typing/" + id + ".png";
            ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
            this.typeList.put(id, image);
        }
    }

    /**
     * This method sets all ImageIcons for all Pokémon.
     *
     * @HashMap key = the Pokémon id.
     * @HashMap value = the Pokémon image.
     * @param p the array list with all Pokémon and their attributes.
     */
    public void setPokemonIconArray(ArrayList<Pokemon> p) {
        for (Pokemon pokemon : p) {
            int pokemonId = pokemon.getId();
            String pokemonImagePath = "images/" + pokemonId + ".png";

            // Use ClassLoader to load resources from within JAR
            ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource(pokemonImagePath));

            // If the image is not found, you can set a default image or handle it as needed
            if (image.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.out.println(pokemonImagePath + " not found");
                // Set a default image if the file doesn't exist
                image = POKEBALL_IMAGE;
            }

            this.pokemonList.put(pokemonId, image);
        }
    }

    public HashMap<Integer, ImageIcon> getPokemonList() {
        return pokemonList;
    }

    public HashMap<Integer, ImageIcon> getTypeList() {
        return typeList;
    }

}
