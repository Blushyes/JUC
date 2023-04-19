package xyz.blushyes.demo;

/**
 * 采用面向对象的方式优化一下
 */
class Room{
    private int count = 0;

    public void increase(){
        synchronized (this) {
            count++;
        }
    }

    public void decrease(){
        synchronized (this) {
            count--;
        }
    }

    public int getCount() {
        return count;
    }
}

public class Demo5 {
    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();

        Thread thread1 = new Thread(()->{
            for (int i = 0; i < 100000; ++i){
                room.increase();
            }
        });

        Thread thread2 = new Thread(()->{
            for (int i = 0; i < 100000; ++i){
                room.decrease();
            }
        });

        thread1.join();
        thread2.join();

        System.out.println(room.getCount()); // 0
    }
}
