package util;

import models.PropertyForm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class PropertyFormUtil {
    public static String getPropertyFilterQuery(final PropertyForm propertyForm) {
        final StringBuilder sb = new StringBuilder("Select * FROM property WHERE property_status = 'ACTIVE'");
        final Map<String, Object> columnMethodMap = new LinkedHashMap<>();

        if (!Objects.isNull(propertyForm.getPropertyType())) {
            sb.append(String.format(" AND property_type = '%s'", propertyForm.getPropertyType()));
        }
        if (!Objects.isNull(propertyForm.getCityQuadrant())) {
            sb.append(String.format(" AND city_quadrant = '%s'", propertyForm.getCityQuadrant()));
        }
        if (!Objects.isNull(propertyForm.getNumberOfBathrooms())) {
            sb.append(String.format(" AND number_of_bathrooms = %d", propertyForm.getNumberOfBathrooms()));
        }
        if (!Objects.isNull(propertyForm.getNumberOfBedrooms())) {
            sb.append(String.format(" AND number_of_bedrooms = %d", propertyForm.getNumberOfBedrooms()));
        }

        final String query = sb.toString();
        System.out.println("DEBUG: query = " + query);
        return query;
    }
}
