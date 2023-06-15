package Kasper.BeansDriver.DataStructures;

public class BeansMain {
    public static void main(String[] args) {
        KasperBean bean = new KasperBean("localhost", "root", "");
        bean.useNode("facebook").useCollection("users").getKey("hello");
    }
}
