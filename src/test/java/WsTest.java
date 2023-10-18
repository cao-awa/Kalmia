import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WsTest {
    public static void main(String[] args) {
        try {
            WebSocketClient client = new WebSocketClient(new URI("ws://127.0.0.1:12345")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }

                @Override
                public void onMessage(String message) {
                    System.out.println(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {

                }
            };

            client.connect();

            while (client.getReadyState() != ReadyState.OPEN) {
                System.out.println(client.getReadyState());

                Thread.sleep(1000);
            }

            client.send("""
                                {
                                    "post_type": "meta",
                                    "post_name": "proxy_connect",
                                    "data": {
                                        "cipher": "awa"
                                    }
                                }
                                """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
