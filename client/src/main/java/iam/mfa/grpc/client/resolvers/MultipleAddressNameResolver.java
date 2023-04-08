package iam.mfa.grpc.client.resolvers;

import java.net.URI;
import java.util.List;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import lombok.RequiredArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 07.04.2023 21:54
 */
@RequiredArgsConstructor(staticName = "of")
public class MultipleAddressNameResolver extends NameResolverProvider {
    private final List<EquivalentAddressGroup> addresses;

    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        return new NameResolver() {
            @Override
            public String getServiceAuthority() {
                return "noAuthority";
            }


            @Override
            public void start(Listener2 listener) {
                listener.onResult(ResolutionResult.newBuilder().setAddresses(addresses).setAttributes(Attributes.EMPTY).build());
            }

            @Override
            public void shutdown() {

            }
        };
    }

    @Override
    public String getDefaultScheme() {
        return "multiTarget";
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }
}
