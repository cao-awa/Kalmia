import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.server.bootstrap.network.KalmiaServerBootstrapNetworkConfig;
import com.github.cao.awa.kalmia.config.template.server.bootstarp.network.KalmiaServerBootstrapNetworkConfigTemplate;
import com.github.cao.awa.kalmia.env.KalmiaEnv;

public class ConfigTest {
    public static void main(String[] args) {
        KalmiaEnv.CONFIG_FRAMEWORK.work();

        JSONObject json = JSONObject.parse("""
                                                   {
                                                     "metadata": {
                                                       "version": 0
                                                     },
                                                     "bind_host": "123456789",
                                                     "bind_port": 12345,
                                                     "use_epoll": false
                                                   }
                                                   """);

        ConfigEntry<KalmiaServerBootstrapNetworkConfig> entry = KalmiaEnv.CONFIG_FRAMEWORK.createEntry("test",
                                                                                                       KalmiaServerBootstrapNetworkConfig.class,
                                                                                                       json,
                                                                                                       KalmiaServerBootstrapNetworkConfigTemplate.class
        );

        System.out.println(entry.get().bindHost.get());
    }
}
