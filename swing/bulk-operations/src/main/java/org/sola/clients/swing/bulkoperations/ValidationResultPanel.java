/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations;

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.validation.ValidationResultBean;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.renderers.TableCellTextAreaRenderer;
import org.sola.clients.swing.ui.renderers.ViolationCellRenderer;

/**
 *
 * @author Elton Manoku
 */
public class ValidationResultPanel extends ContentPanel {

    private static String PANEL_NAME = "VALIDATION_PANEL";    
    
    private ObservableList<ValidationResultBean> validationResultList =
            new SolaObservableList<ValidationResultBean>();
    /**
     * Creates new form ValidationResultPanel
     */
    public ValidationResultPanel() {
        initComponents();
        setName(PANEL_NAME);
    }

    public ObservableList<ValidationResultBean> getValidationResultList() {
        return validationResultList;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableValidations = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        setHeaderPanel(headerPanel);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/bulkoperations/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("ValidationResultPanel.headerPanel.titleText")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${validationResultList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tableValidations);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${feedback}"));
        columnBinding.setColumnName("Feedback");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${severity}"));
        columnBinding.setColumnName("Severity");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${successful}"));
        columnBinding.setColumnName("Successful");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane1.setViewportView(tableValidations);
        tableValidations.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ValidationResultPanel.tableValidations.columnModel.title0")); // NOI18N
        tableValidations.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableValidations.getColumnModel().getColumn(1).setMaxWidth(100);
        tableValidations.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ValidationResultPanel.tableValidations.columnModel.title1")); // NOI18N
        tableValidations.getColumnModel().getColumn(1).setCellRenderer(new TableCellTextAreaRenderer());
        tableValidations.getColumnModel().getColumn(2).setPreferredWidth(45);
        tableValidations.getColumnModel().getColumn(2).setMaxWidth(45);
        tableValidations.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("ValidationResultPanel.tableValidations.columnModel.title2")); // NOI18N
        tableValidations.getColumnModel().getColumn(2).setCellRenderer(new ViolationCellRenderer());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableValidations;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
