package xyz.blushyes.demo.guarder;

class GuarderObject{
    private Object response;

    public synchronized Object getResponse() {
        if (response == null){  // 如果response为空，说明此时还没有人传递资源
            try {
                // 既然没有资源，那么我就一直等着资源送过来
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }

    public synchronized Object getResponse(long timeout) {
        long begin = System.currentTimeMillis();
        long passTime = 0;
        while (response == null){
            try {
                long remaining = timeout - passTime;

                // 如果剩余时间小于等于0，则不继续等待
                if (remaining <= 0 ){
                    break;
                }

                // 还要等待剩余时间
                this.wait(remaining);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            passTime = System.currentTimeMillis() - begin;
        }
        return response;
    }

    // 思考：如果这个函数不加锁会怎么样？
    // 抛出IllegalMonitorStateException异常: current thread is not owner
    // 也就是说当前线程没有拿到锁，也就没有通知休息室人的资格
    public synchronized void setResponse(Object response) {
        this.response = response;
        // 通知所有休息的线程，值已经写入了，也就是有资源了，快来拿
        this.notifyAll();
    }
}