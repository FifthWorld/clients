/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations.beans;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import org.sola.clients.beans.AbstractBindingBean;

/**
 *
 * @author Elton Manoku
 */
public class SpatialBulkMoveBean extends AbstractBindingBean {

    public static final String PROPERTY_SOURCE = "source";
    public static final String PROPERTY_DESTINATION = "destination";
    private SpatialSourceBean source = new SpatialSourceShapefileBean();
    private SpatialDestinationBean destination = new SpatialDestinationCadastreObjectBean();

    @NotNull(message = "Source must be present")
    public SpatialSourceBean getSource() {
        return source;
    }

    public void setSource(SpatialSourceBean source) {
        SpatialSourceBean old = this.source;
        this.source = source;
        propertySupport.firePropertyChange(PROPERTY_SOURCE, old, source);
    }

    @NotNull(message = "Destination must be present")
    public SpatialDestinationBean getDestination() {
        return destination;
    }

    public void setDestination(SpatialDestinationBean value) {
        SpatialDestinationBean old = this.destination;
        this.destination = value;
        propertySupport.firePropertyChange(PROPERTY_DESTINATION, old, value);
    }

    public List<SpatialUnitTemporaryBean> getBeans() {
        return getDestination().getBeans(getSource());
    }

    public TransactionBulkOperationSpatial sendToServer() {
        TransactionBulkOperationSpatial transaction = new TransactionBulkOperationSpatial();
        if (getDestination().getClass().equals(SpatialDestinationCadastreObjectBean.class)) {
            transaction.setGenerateFirstPart(true);
        }
        transaction.setSpatialUnitTemporaryList(getBeans());
        transaction.save();
        return transaction;
    }

    @Override
    public <T extends AbstractBindingBean> Set<ConstraintViolation<T>> validate(
            boolean showMessage, Class<?>... group) {
        Set<ConstraintViolation<T>> violations = super.validate(showMessage, group);
        if (getSource() != null) {
            violations.addAll((Collection) getSource().validate(showMessage, group));
        }
        if (getDestination() != null) {
            violations.addAll((Collection) getDestination().validate(showMessage, group));
        }
        return violations;
    }
}
