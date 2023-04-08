package iam.mfa.grpc.client.config;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import iam.mfa.grpc.client.resolvers.MultipleAddressNameResolver;
import io.grpc.EquivalentAddressGroup;
import io.grpc.ManagedChannel;
import io.grpc.NameResolverRegistry;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 16:03
 */
@Configuration
public class ClientConfig {

    @Value("#{'${grpc.client.targets}'.split(',')}")
    private List<String> targets;


    @PostConstruct
    public void initNameResolver() {
        final var addresses = targets.stream()
                .map(target -> {
                    final var splitTarget = target.split(":");
                    final var host = splitTarget[0];
                    final var port = splitTarget[1];
                    return new EquivalentAddressGroup(new InetSocketAddress(host, Integer.parseInt(port)));
                })
                .toList();
        final var nameResolverFactory = MultipleAddressNameResolver.of(addresses);
        NameResolverRegistry.getDefaultRegistry().register(nameResolverFactory);
    }


    @Bean
    public ManagedChannel channel() {
        return NettyChannelBuilder.forTarget("localhost")
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }

}
