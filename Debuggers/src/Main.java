/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import Kasper.BeansDriver.DataStructures.CollectionReference;
import Kasper.BeansDriver.DataStructures.KasperBean;
import Parser.ParseProcessor;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Rufelle
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var tokens = ParseProcessor.tokenize("MATCH GET 'db.oo.dsds.fasdsasa' = 'this.is.not.a.path';");
        System.out.println(tokens);
    }


}
