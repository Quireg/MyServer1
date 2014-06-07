package beans;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPService {


    public MessageBuilder getMessageBuilder() {
        return messageBuilder;
    }

    public void setMessageBuilder(MessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    private MessageBuilder messageBuilder;
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start(){
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    String s = "";
                    ServerSocket socket = new ServerSocket(81);
                    Socket ss = socket.accept();
                    Scanner scanner = new Scanner(ss.getInputStream());
                    while (scanner.hasNext()){
                        s = s + scanner.next();
                    }
                    System.out.println(s);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("Socket is running");
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void stop(){

    }
}
