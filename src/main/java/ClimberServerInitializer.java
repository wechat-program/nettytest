import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author wujianchuan 2019/11/12
 * @version 1.0
 */
public class ClimberServerInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) {
        channel.pipeline().addLast(new StringDecoder());
        channel.pipeline().addLast(new HelloWorldHandler());
    }
}
