package com.jasonpyau.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Patch {
    
    private Patch() {}

    private static List<String> getNullFields(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        HashSet<String> nullFields = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String field = propertyDescriptor.getName();
            if (beanWrapper.getPropertyValue(field) == null) {
                nullFields.add(field);
            }
        }
        return new ArrayList<>(nullFields);
    }

    public static void merge(Object source, Object target, String... ignoreProperties) {
        HashSet<String> set = new HashSet<>();
        set.addAll(Arrays.asList(ignoreProperties));
        set.addAll(getNullFields(source));
        String[] ignored = new String[set.size()];
        BeanUtils.copyProperties(source, target, set.toArray(ignored));
    }
}
