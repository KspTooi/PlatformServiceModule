package com.ksptooi.uac;

import org.h2.tools.Recover;

import java.sql.SQLException;

public class RecoverDatabase {

    public static void main(String[] args) throws SQLException {


        Recover.execute("./","uac_db");


    }

}
