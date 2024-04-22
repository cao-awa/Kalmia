import com.alibaba.fastjson2.JSONObject;
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

                    JSONObject json = JSONObject.parse(message);

                    if (json.getString("post_name")
                            .equals("status_notice") &&
                            json.getJSONObject("data")
                                .getString("status")
                                .equals("status.kalmia.handshake.hello")) {
                        send("""
                                     {
                                         "post_type": "login",
                                         "post_name": "login_with_password",
                                         "time": 1697986847811,
                                         "data": {
                                             "uid": 1,
                                             "pwd": "123456"
                                         }
                                     }
                                     """);

                        send("""
                                     {
                                         "post_type": "message",
                                         "post_name": "select_message",
                                         "time": 1697986847811,
                                         "data": {
                                             "session_identity": "25burfp0j7epb0hqfcuw340g",
                                             "from": 0,
                                             "to": 114514
                                         }
                                     }
                                     """);
                    }
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
                                    "time": 1697986847811,
                                    "data": {
                                        "cipher": "awa",
                                        "identity": "aaaaaabbbbbbbwwwwwwdddddd",
                                        "server_host": "127.0.0.1",
                                        "server_port": 54321,
                                        "data_save": true
                                    }
                                }
                                """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
