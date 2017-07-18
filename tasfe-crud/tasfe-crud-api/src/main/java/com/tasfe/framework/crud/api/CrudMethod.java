package com.tasfe.framework.crud.api;

/**
 * Created by Lait on 2017/7/10.
 */
public enum CrudMethod {
    IN, INS, UPD, UPDS, DEL, DELS, GET, GETS, LIST, FIND,COUNT;

    public static CrudMethod get(String crudMethod) {
        if (crudMethod != null) {
            try {
                return Enum.valueOf(CrudMethod.class, crudMethod.trim());
            } catch (IllegalArgumentException ex) {
            }
        }
        return null;
    }
}
