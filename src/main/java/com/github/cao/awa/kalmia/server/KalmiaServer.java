package com.github.cao.awa.kalmia.server;

import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.io.server.KalmiaServerNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.session.manage.SessionManager;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.key.ec.EcServerKeyPair;
import com.github.cao.awa.kalmia.user.manage.UserManager;

public class KalmiaServer {
    private final KalmiaServerNetworkIo networkIo;
    private final MessageManager messageManager;
    private final UserManager userManager;
    private final SessionManager sessionManager;
    private boolean started;

    public boolean isStarted() {
        return this.started;
    }

    public UserManager userManager() {
        return this.userManager;
    }

    public MessageManager messageManager() {
        return this.messageManager;
    }

    public SessionManager sessionManager() {
        return this.sessionManager;
    }

    public KalmiaServer() {
        try {
            this.networkIo = new KalmiaServerNetworkIo(this);

            this.messageManager = new MessageManager("data/msg");
            this.userManager = new UserManager("data/usr");
            this.sessionManager = new SessionManager("data/session");

            // TODO
            // Test only
            this.userManager.set(1,
                                 new DefaultUser(TimeUtil.millions(),
                                                 "123456",
                                                 new EcServerKeyPair(
                                                         Mathematics.toBytes(
                                                                 "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vno5s6a3805phsxo2h3alhfv4j82g4rmnma75r2z455u3q3vwa6bnnhhuasjqn7t6ej56cg6kv4x21osmu14m09om1ofhzb1xqf6d69z8dnophc8o34h0az66852dfa8yth8oxz2t6yf3c0lhjhxbpml2dh0y5yg7ueendizsw3dslncr2ur6edjix8fnaifeygo6dhdcj5hd6",
                                                                 36
                                                         ),
                                                         Mathematics.toBytes(
                                                                 "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq360weg0c0t5vwpgs8tbpxngxghfsebzbnnyjs84lq09jlz549yznbms2axywvzbbl4ah7vzkgxknv0f2rmlvjw0i8hhzkkbhflbk3z24scim36jx3exfl87qh1m876ckxf4ixw0vcgacr9zjdhnh35gyv0mhme6k77ee15uz504gbioal8oq493vu6ucfg95nfir7kzfze3oczf2gn00ur9t8gaalmcdcrfw2b9i336sqltk996voz4nj4pfn2muutcmctl9j2uk3kjzsi40w9xo2mk6d5siu9n0czmvxoyc0f7zbtau9qtf9k8t5p57fyaxy74eiofe",
                                                                 36
                                                         )
                                                 )
                                 )
            );
            this.userManager.set(2,
                                 new DefaultUser(TimeUtil.millions(),
                                                 "123456",
                                                 new EcServerKeyPair(
                                                         Mathematics.toBytes(
                                                                 "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vno770wd3gwjvq7j1vhqq5l0yjlphkwkoc5936p7v6hjiuq9b93ofolymv9mxe4kee14drgy0qk1kjiwraoce92m7nft6kjg6xvr0pyqleuu09cqztiak54igleplgbx39w5v0fbu0l3hdynfzc0e2jl1d87s1f911i3s66v1g7p6lxfiau55ha9qleq4t6zzvnkxuzhq0mpvq",
                                                                 36
                                                         ),
                                                         Mathematics.toBytes(
                                                                 "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq27g2a8wwiltv7fesakmgiurg9t2loln9vt6sfprcvl5xxr4gkfij6u4zat2vo8ag7c1yi7jgjn1yyezkd2asu9sf7y3ossc6u65ljqfgvpeam9ihffm0w70ttuhh6n83zo7xv4sn30dohkonikim1cqavifey60n9fem41vyinj833e0nvz4o0hkq94fk1ko3qjf5xsioxcqnlmbay7pzalfofdblxci1at7wpg0v9tvdxdkgvfnj2c6jrwuc2r2kvjehnrdzdpb3m752123oifbnf9mnuzcheknrr2oinn1y9vs8bcnjn7ppsfqz51x12x1oae0qr9i",
                                                                 36
                                                         )
                                                 )
                                 )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startup() throws Exception {
        setupNetwork();
    }

    public void setupNetwork() throws Exception {
        UnsolvedPacketFactor.register();

        this.started = true;

        this.networkIo.start(12345);
    }

    public boolean useEpoll() {
        return true;
    }
}