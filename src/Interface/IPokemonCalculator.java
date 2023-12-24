/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

/**
 *
 * @author F776
 */
public interface IPokemonCalculator {

    // Constants for nature values
    double HINDERING_NATURE = 0.9;
    double NEUTRAL_NATURE = 1.0;
    double BENEFICIAL_NATURE = 1.1;
    //

    int calculateModifiedStat(int baseStat, int IV, int EV, int level);

    int calculateHpStat(int baseStat, int IV, int EV, int level);
}
