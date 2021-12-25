/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.interfaces.call_back_interface;

import java.sql.ResultSet;

@FunctionalInterface
public interface CallBackDataBase {
    public abstract void executeSQL(ResultSet res);
}
