package xyz.blushyes.demo.guarder;

import xyz.blushyes.util.ThreadUtil;

public class GuarderObjectDemo1 {
    public static void main(String[] args) {
        GuarderObject guarderObject = new GuarderObject();

        new Thread(()->{
            System.out.println("thread 1: waiting for setting...");
            System.out.println("thread 1: I get: " + guarderObject.getResponse());
        }).start();

        new Thread(()->{
            ThreadUtil.stop(3);
            guarderObject.setResponse("resource");
            System.out.println("thread 2: setting completed.");
        }).start();
    }
}