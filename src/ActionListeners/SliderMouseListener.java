/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ActionListeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import soundManagement.SoundPlayer;

/**
 *
 * @author F776
 */
public class SliderMouseListener extends MouseAdapter {

    private final JSlider[] statSliders;

    /**
     * This method retrieves a list of sliders to store in this.statSliders and
     * use in the following methods.
     *
     * @param statSliders a list of JSLiders
     */
    public SliderMouseListener(JSlider[] statSliders) {
        this.statSliders = statSliders;
    }

    /**
     * Adjusts the values of JSlider components based on the sum of their
     * values. If the total sum of the slider values exceeds 508, it reduces the
     * value of the currently adjusted slider to ensure the total sum does not
     * surpass the limit.
     *
     * @param e The MouseEvent that triggered the adjustment.
     */
    private void handleSliderAdjustment(MouseEvent e) {
        int sum = 0;
        // Calculate the sum of the sliders' values
        for (JSlider slider : statSliders) {
            sum += slider.getValue();
        }

        if (sum > 508) {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            int diff = sum - 508;
            int reducedValue = value - diff;

            if (reducedValue < 0) {
                // The slider's value should not go below 0
                source.setValue(0);
            } else {
                source.setValue(reducedValue);
            }
        }
    }

    /**
     * Invokes handleSliderAdjustment(e) for when the mouse is pressed.
     *
     * @param e The MouseEvent instance containing information about the mouse
     * press event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        handleSliderAdjustment(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Code to be executed when the mouse is clicked
        handleSliderAdjustment(e);
    }

    /**
     * Invokes handleSliderAdjustment(e) for when the mouse is dragged.
     *
     * @param e The MouseEvent instance containing information about the mouse
     * drag event.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        handleSliderAdjustment(e);
    }

    /**
     * Invoked when a mouse button is released on a slider. Rounds the value of
     * the JSlider to the nearest multiple of 4 when the mouse is released.
     *
     * @param e The MouseEvent instance containing information about the mouse
     * release event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // When the mouse is released, round the value to the nearest multiple of 4
        SoundPlayer.optionSelected();
        JSlider source = (JSlider) e.getSource();
        int value = source.getValue();
        int roundedValue = (value + 2) / 4 * 4;
        source.setValue(roundedValue);
        handleSliderAdjustment(e);
    }
}
