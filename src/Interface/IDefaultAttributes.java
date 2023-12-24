/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import javax.swing.ImageIcon;

/**
 *
 * @author F776
 */
public interface IDefaultAttributes {

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    ImageIcon POKEBALL_IMAGE = new ImageIcon(classLoader.getResource("images/default.png"));
    ImageIcon STATUS_MOVE = new ImageIcon(classLoader.getResource("images/moveCategories/Status.png"));
    ImageIcon SPECIAL_MOVE = new ImageIcon(classLoader.getResource("images/moveCategories/Special.png"));
    ImageIcon PHYSICAL_MOVE = new ImageIcon(classLoader.getResource("images/moveCategories/Physical.png"));
    ImageIcon GREAT_TUSK_ICON = new ImageIcon(classLoader.getResource("images/pokedex/great_tusk_icon.png"));

    String PROGRESS_MOVES = "Loading Moves";
    String PROGRESS_NATURES = "Loading Natures";
    String PROGRESS_POKEMON = "Loading Pokémon";
    String PROGRESS_TYPES = "Loading Types";
    String PROGRESS_ABILITIES = "Loading Abilities";
    String PROGRESS_ITEMS = "Loading Items";
    String PROGRESS_TYPINGS = "Loading Pokémon Typings";
    String PROGRESS_POKEMON_ABILITIES = "Loading Pokémon Abilities";
    String PROGRESS_POKEMON_IMAGES = "Loading Pokémon Icons";
    String PROGRESS_TYPE_IMAGES = "Loading Type Icons";
    String PROGRESS_COMPONENTS = "Loading Components";

}
