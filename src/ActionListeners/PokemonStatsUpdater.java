/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ActionListeners;

import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modelo.Pokemon;

/**
 *
 * @author F776
 */
public class PokemonStatsUpdater {

    private final ArrayList<Pokemon> listaPokemon;
    private final JComboBox cbo;
    private final JLabel finalStatLabel;
    private final JSpinner ivSpinner;
    private final JSlider slider;
    private final int level;

    public PokemonStatsUpdater(ArrayList<Pokemon> listaPokemon,
            JComboBox cbo, JLabel finalStatLabel,
            JSpinner ivSpinner, JSlider slider,
            int level) {
        this.listaPokemon = listaPokemon;
        this.cbo = cbo;
        this.finalStatLabel = finalStatLabel;
        this.ivSpinner = ivSpinner;
        this.slider = slider;
        this.level = level;

        // Add change listeners to slider and spinner
        addSliderChangeListener();
        addSpinnerChangeListener();
    }

    private void addSliderChangeListener() {
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateStats();
            }
        });
    }

    private void addSpinnerChangeListener() {
        ivSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateStats();
            }
        });
    }

    private void updateStats() {
        for (Pokemon p : listaPokemon) {
            if (p.getName().equals(cbo.getSelectedItem().toString())) {
                finalStatLabel.setText(String.valueOf(
                        calculateModifiedStat(p.getHp(), (int) ivSpinner.getValue(), slider.getValue(), level)));
                break;
            }
        }
    }

    private int calculateModifiedStat(int baseStat, int IV, int EV, int level) {
        return (int) Math.floor((Math.floor(2 * baseStat + IV + Math.floor(EV / 4)) * level / 100 + 5));
    }
}
