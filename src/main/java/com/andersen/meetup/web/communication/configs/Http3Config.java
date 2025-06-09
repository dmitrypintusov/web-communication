package com.andersen.meetup.web.communication.configs;

import java.time.Duration;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.http.Http3SslContextSpec;
import reactor.netty.http.HttpProtocol;

@Component
public class Http3Config implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

  @Override
  public void customize(NettyReactiveWebServerFactory factory) {
    factory.addServerCustomizers(server -> {
      SslBundle sslBundle = factory.getSslBundles().getBundle("server-http3");
      Http3SslContextSpec sslContextSpec =
          Http3SslContextSpec.forServer(sslBundle.getManagers().getKeyManagerFactory(), sslBundle.getKey().getPassword());

      return server
          .protocol(HttpProtocol.HTTP3)
          .secure(spec -> spec.sslContext(sslContextSpec))
          .http3Settings(spec -> spec
              .idleTimeout(Duration.ofSeconds(5))
              .maxData(10_000_000)
              .maxStreamDataBidirectionalRemote(1_000_000)
              .maxStreamsBidirectional(100));
    });
  }
}
