package com.github.cao.awa.kalmia.client.ui;

import com.github.cao.awa.apricot.annotations.Unsupported;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.status.RequestState;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import javax.swing.*;

@Unsupported
public class KalmiagramUi {
    private static final JFrame frame = new JFrame();
    private static final JList<Long> sessionsList = new JList<>();
    private static final JPanel loginPanel = loginPanel();
    private static final JPanel messagingPanel = messagingPanel();

    public static void startUi() {
        frame.setSize(1000,
                      600
        );

        frame.add(loginPanel);
        frame.add(messagingPanel);

        frame.setVisible(true);
    }

    public static JPanel messagingPanel() {
        JPanel messagingPanel = new JPanel();
        messagingPanel.setLayout(null);

        sessionsList.setBounds(0,
                               0,
                               200,
                               600
        );
        messagingPanel.add(sessionsList);

        JList<String> messageList = new JList<>();
        messageList.setBounds(200,
                              0,
                              4000,
                              600
        );
        messagingPanel.add(messageList);

        sessionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sessionsList.addListSelectionListener(event -> {
            JList<Long> list = EntrustEnvironment.cast(event.getSource());
            assert list != null;
            long selectedSession = list.getSelectedValue();

            long curSeq = Kalmia.CLIENT.curMsgSeq(selectedSession,
                                                  true
            );

            messageList.setListData(Kalmia.CLIENT.getMessages(selectedSession,
                                                              Math.max(0,
                                                                       curSeq - 15
                                                              ),
                                                              curSeq,
                                                              true
                                          )
                                                 .stream()
                                                 .map(message -> {
                                                     return message.sender() + ": " + message.coverContent();
                                                 })
                                                 .toArray(String[] :: new));
        });

        return messagingPanel;
    }

    public static void setSessionsData(Long[] sessions) {
        sessionsList.setListData(sessions);
    }

    public static JPanel loginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setSize(1000,
                           600
        );

        JTextArea userid = new JTextArea();
        userid.setBounds(100,
                         250,
                         200,
                         30
        );
        loginPanel.add(userid);

        JTextArea password = new JTextArea();
        password.setBounds(100,
                           300,
                           200,
                           30
        );
        loginPanel.add(password);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(100,
                              400,
                              100,
                              50
        );
        loginPanel.add(loginButton);

        loginButton.addActionListener(event -> {
            try {
                byte[] receipt = BytesRandomIdentifier.create(16);

                System.out.println(Mathematics.radix(receipt,
                                                     36
                ));

                KalmiaEnv.awaitManager.await(receipt,
                                             () -> {
                                                 Kalmia.CLIENT.router()
                                                              .send(new LoginWithPasswordPacket(Long.parseLong(userid.getText()),
                                                                                                password.getText()
                                                              ).receipt(receipt));
                                             }
                );

                if (Kalmia.CLIENT.router()
                                 .getStates() == RequestState.AUTHED) {
                    loginPanel.setVisible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return loginPanel;
    }

    public static void main(String[] args) {
        startUi();
    }
}
