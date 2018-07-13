package yanglas.socket.nio;

import yanglas.socket.nio.handler.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeNIOServer {
    public static void main(String[] args) throws IOException {
        int port = 8088;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }


}
