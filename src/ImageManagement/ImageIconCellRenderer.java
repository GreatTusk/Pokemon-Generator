package ImageManagement;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

public class ImageIconCellRenderer extends DefaultTableCellRenderer {

    @Override
    protected void setValue(Object value) {
        // Ensure the value is an ImageIcon
        if (value instanceof ImageIcon) {
            setIcon((ImageIcon) value);
            setText("");
        } else {
            super.setValue(value);
        }
    }
}
