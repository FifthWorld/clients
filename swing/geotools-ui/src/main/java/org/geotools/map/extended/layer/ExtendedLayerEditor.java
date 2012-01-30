/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.map.extended.layer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import java.util.ArrayList;
import java.util.List;
import org.geotools.feature.CollectionEvent;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.Geometries;
import org.opengis.feature.simple.SimpleFeature;
import org.geotools.swing.extended.util.Messaging;

/**
 * The layer is used to handle the editing of features. Features in this layer can be added, removed
 * and also the vertexes can be modified and snapped. <br/>
 * It adds an extra {@see ExtendedLayerGraphics}
 * to show vertexes of the features.
 * 
 * @author Elton Manoku
 */
public class ExtendedLayerEditor extends ExtendedLayerGraphics {

    private static String VERTICES_LAYER_NAME_POSTFIX = " vertices";
    private static String VERTICES_LAYER_STYLE_RESOURCE = "editor_vertices.xml";
    
    private ExtendedLayerGraphics verticesLayer = null;
    /**
     * The list of {@see VertexInformation} for the features in this layer
     */
    protected List<VertexInformation> vertexList = new ArrayList<VertexInformation>();

    /**
     * It creates an instance of this layer.
     * @param name Name of the layer. It should be unique.
     * @param geometryType The type of geometry.
     * @param styleResource The resource where the style is found
     * @param extraFieldsFormat If there are more fields than geometry, are defined here in 
     * the format as accepted by DataUtility of Geotools.
     * @throws Exception 
     */
    public ExtendedLayerEditor(
            String name,
            Geometries geometryType,
            String styleResource,
            String extraFieldsFormat) throws Exception {
        this(name, geometryType, styleResource, VERTICES_LAYER_STYLE_RESOURCE, extraFieldsFormat);
    }

    
    public ExtendedLayerEditor(
            String name,
            Geometries geometryType,
            String styleResource,
            String styleResourceForVertexes,
            String extraFieldsFormat) throws Exception {
        super(name, geometryType, styleResource, extraFieldsFormat);
        this.verticesLayer = new ExtendedLayerGraphics(
                name + VERTICES_LAYER_NAME_POSTFIX,
                Geometries.POINT, 
                styleResourceForVertexes);
        this.getMapLayers().addAll(this.verticesLayer.getMapLayers());
    }

    @Override
    public SimpleFeature addFeature(String fid, byte[] geomAsBytes,
            java.util.HashMap<String, Object> fieldsWithValues) throws Exception {
        return this.addFeature(fid, wkbReader.read(geomAsBytes), fieldsWithValues);
    }

    /**
     * For description see the same method being overridden. <br/>
     * Additionally, it populates the list of vertexes 
     * @param fid
     * @param geom
     * @param fieldsWithValues
     * @return
     * @throws Exception 
     */
    @Override
    public SimpleFeature addFeature(String fid, com.vividsolutions.jts.geom.Geometry geom,
            java.util.HashMap<String, Object> fieldsWithValues) throws Exception {

        SimpleFeature featureAdded = super.addFeature(fid, geom, fieldsWithValues);

        com.vividsolutions.jts.geom.CoordinateList coordinates =
                new CoordinateList(geom.getCoordinates(), false);

        Coordinate coordinate;
        for (Object coordinateObj : coordinates) {
            coordinate = (Coordinate) coordinateObj;
            Point vertex = this.verticesLayer.getGeometryFactory().createPoint(coordinate);

            this.vertexList.add(new VertexInformation(
                    coordinate, featureAdded, this.verticesLayer.addFeature(null, vertex, null)));
        }

        this.getMapControl().refresh();
        return featureAdded;
    }

    /**
     * For description see the same method being overridden. <br/>
     * Additionally removes all related vertexes with the feature being removed.
     * @param fid
     * @return 
     */
    @Override
    public SimpleFeature removeFeature(String fid) {
        SimpleFeature result = super.removeFeature(fid);
        if (result != null) {
            List<VertexInformation> vertexesToRemove = new ArrayList<VertexInformation>();
            for (VertexInformation vertexInfo : this.vertexList) {
                if (vertexInfo.getFeature().equals(result)) {
                    vertexesToRemove.add(vertexInfo);
                    this.verticesLayer.removeFeature(vertexInfo.getVertexFeature().getID());
                }
            }
            this.vertexList.removeAll(vertexesToRemove);
            this.getMapControl().refresh();
        }
        return result;
    }

    /**
     * For description see the same method being overridden. <br/>
     * Additionally, removes also the vertexes.
     */
    @Override
    public void removeFeatures() throws Exception {
        super.removeFeatures();
        this.verticesLayer.removeFeatures();
        this.getMapControl().refresh();
    }

    /**
     * Gets the Vertices layer
     */
    public ExtendedLayerGraphics getVerticesLayer() {
        return verticesLayer;
    }

    /**
     * It searches for a vertex in the layer within the distance. It returns the first found vertex.
     * @param fromPosition Position from where to search for. Normally is the mouse position in the map
     * @param distance The distance to search around the position
     * @return 
     */
    public VertexInformation getFirstVertexWithinDistance(
            DirectPosition2D fromPosition, double distance) {
        Coordinate coordinateToFind = new Coordinate(fromPosition.x, fromPosition.y);
        for (VertexInformation vertexInformation : this.vertexList) {
            if (vertexInformation.getVertex().distance(coordinateToFind) <= distance) {
                return vertexInformation;
            }
        }
        return null;
    }

    /**
     * It changes the position of a vertex.
     * @param vertexInformation Vertex to change
     * @param newPosition The new position of the vertex.
     * @return 
     */
    public SimpleFeature changeVertex(
            VertexInformation vertexInformation, DirectPosition2D newPosition) {
        double currentX = vertexInformation.getVertex().x;
        double currentY = vertexInformation.getVertex().y;
        vertexInformation.getVertex().x = newPosition.x;
        vertexInformation.getVertex().y = newPosition.y;
        SimpleFeature featureChanged = vertexInformation.getFeature();
        if (this.validateGeometry((Geometry) featureChanged.getDefaultGeometry())) {
            ((Geometry) featureChanged.getDefaultGeometry()).geometryChanged();
            this.getFeatureCollection().notifyListeners(
                    vertexInformation.getFeature(), CollectionEvent.FEATURES_CHANGED);
            this.getMapControl().refresh();
        } else {
            featureChanged = null;
            vertexInformation.getVertex().x = currentX;
            vertexInformation.getVertex().y = currentY;
            Messaging.getInstance().show(
                    Messaging.Ids.DRAWINGTOOL_GEOMETRY_NOT_VALID_ERROR.toString());
        }
        return featureChanged;
    }

    /**
     * It checks if the geometry to be used for the feature is valid.
     * @param geom
     * @return 
     */
    private boolean validateGeometry(Geometry geom) {
        boolean result = true;
        result = geom.isSimple() && geom.isValid();
        return result;
    }
}