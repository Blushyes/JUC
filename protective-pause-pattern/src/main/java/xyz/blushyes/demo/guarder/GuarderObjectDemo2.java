package xyz.blushyes.demo.guarder;

import xyz.blushyes.util.ThreadUtil;

public class GuarderObjectDemo2 {
    public static void main(String[] args) {
        for (int i = 0; i < 3; ++i){
            int finalI = i;
            new Thread(()->{
                System.out.println("I am " + Thread.currentThread().getName() + ", I am waiting for " + finalI);
                System.out.println(Thread.currentThread().getName() +  " get: " + GuarderPacking.createGuarderObject().getResponse(5000));
            }).start();
        }

        ThreadUtil.stop(1);

        for (Integer id : GuarderPacking.getKeySet()) {
            new Thread(()->{
                ThreadUtil.stop(3);
                GuarderPacking.getGuarderObject(id).setResponse("resource by " + Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + ": setting completed: " + id);
            }).start();
        }
    }
}