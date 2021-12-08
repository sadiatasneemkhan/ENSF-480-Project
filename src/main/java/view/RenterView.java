package view;

import controllers.PropertyController;
import models.Property;
import models.PropertyForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Collection;

public class RenterView {
    final private PropertyController propertyController = PropertyController.getOnlyInstance();

    public RenterView(final PropertyForm propertyForm) {
        final JFrame renterViewFrame = new JFrame("All Properties");
        renterViewFrame.setVisible(true);
        renterViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renterViewFrame.setSize(500,500);


        Collection<Property> properties = propertyController.getProperties(propertyForm);
        final JPanel panel = new JPanel();

        final DefaultTableModel tableModel = Property.getTable(properties);

        final JTable propertyTable = new JTable(tableModel);

        JScrollPane js = new JScrollPane(propertyTable);

        panel.add(propertyTable);
        panel.add(js);

        renterViewFrame.add(panel);
    }
}
