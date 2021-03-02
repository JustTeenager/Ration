package com.ration.qcode.application.MainPack.validator;

/**
 * Created on 13.07.2017.
 *
 * A class that checks the data entered by the user.
 *
 * @author Влад.
 */

public class Validator {

    public Validator(){}

    /**
     * method that checks name product entered by the user.
     * @param name - name product entered by the user.
     * @return true - name valid, false - name not valid.
     */
    public boolean validetName(String name){
        if(!name.equals(null)){
            if(!name.equals("")){
                return name.length() >= 2;
            }
        }
        return false;
    }

    /**
     * method that checks FA product entered by the user.
     * @param fa - FA product entered by the user.
     * @return true - FA valid, false - FA not valid.
     */
    public boolean validetFA(String fa){
        if(!fa.equals(null)){
            return !fa.equals("");
        }
        return false;
    }

    /**
     * method that checks Kilokalorii product entered by the user.
     * @param kkal - Kilokalorii product entered by the user.
     * @return true - Kilokalorii valid, false - Kilokalorii not valid.
     */
    public boolean validetKkal(String kkal){
        if(!kkal.equals(null)){
            return !kkal.equals("");
        }
        return false;
    }

    /**
     * method that checks Belki product entered by the user.
     * @param belki - Belki product entered by the user.
     * @return true - Belki valid, false - Belki not valid.
     */

    public boolean validetBelki(String belki){
        if(!belki.equals(null)){
            return !belki.equals("");
        }
        return false;
    }

    /**
     * method that checks Uglevodi product entered by the user.
     * @param uglevod - Uglevodi product entered by the user.
     * @return true - Uglevodi valid, false - Uglevodi not valid.
     */
    public boolean validetUglevod(String uglevod){
        if(!uglevod.equals(null)){
            return !uglevod.equals("");
        }
        return false;
    }

    /**
     * method that checks Jiry product entered by the user.
     * @param jiry - Jiry product entered by the user.
     * @return true - Jiry valid, false - Jiry not valid.
     */
    public boolean validetJiry(String jiry){
        if(!jiry.equals(null)){
            return !jiry.equals("");
        }
        return false;
    }

}
