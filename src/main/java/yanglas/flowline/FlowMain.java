package yanglas.flowline;

public class FlowMain {
    public static void main(String[] args){
        /**
         * 实现并启动线程有两种方法
         * 1、写一个类继承自Thread类，重写run方法。用start方法启动线程
         * 2、写一个类实现Runnable接口，实现run方法。用new Thread(Runnable target).start()方法来启动
         * 1.start（）方法来启动线程，真正实现了多线程运行。
         * 这时无需等待run方法体代码执行完毕，可以直接继续执行下面的代码；
         * 通过调用Thread类的start()方法来启动一个线程， 这时此线程是处于就绪状态， 并没有运行。
         * 然后通过此Thread类调用方法run()来完成其运行操作的，
         * 这里方法run()称为线程体，它包含了要执行的这个线程的内容， Run方法运行结束， 此线程终止。然后CPU再调度其它线程。
         * 2.run（）方法当作普通方法的方式调用。程序还是要顺序执行，要等待run方法体执行完毕后，才可继续执行下面的代码；
         * 程序中只有主线程——这一个线程， 其程序执行路径还是只有一条， 这样就没有达到写线程的目的。
         */
        new Thread(new Div()).start();
        new Thread(new Multyply()).start();
        new Thread(new Plus()).start();

        for(int i=1;i<100;i++){
            for(int j=1;j<100;j++){
                Msg msg = new Msg();
                msg.setI(i);
                msg.setJ(j);
                msg.setDesc("(("+i+"+"+j+")*"+i+")/2");
               // System.out.println(msg.toString());
                Plus.blockingDeque.add(msg);
            }
        }
    }
}
