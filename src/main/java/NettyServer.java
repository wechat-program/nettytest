import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author wujianchuan 2019/11/12
 * @version 1.0
 */
public class NettyServer {
    public static void main(String[] args) {
        int port = 8080;
        new NettyServer().bind(port);
    }

    private void bind(int port) {
        // 主线程组，用于接收客户端的链接，但不做任何处理
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 定义从线程组，主线程组会把任务转给从线程组进行处理
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 服务启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置主从线程模型
        serverBootstrap.group(bossGroup, workerGroup);
        // 设置NIO的双向通道
        serverBootstrap.channel(NioServerSocketChannel.class);
        // 设置chanel初始化器
        serverBootstrap.childHandler(new ClimberServerInitializer());

        try {
            // 绑定端口，并设置为同步方式
            ChannelFuture future = serverBootstrap.bind(port).sync();

            System.out.println(Thread.currentThread().getName() + ",服务器开始监听端口，等待客户端连接.........");

            // 获取某个客户端所对应的chanel，关闭并设置同步方式
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 使用一种优雅的方式进行关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    private class ClimberServerInitializer extends ChannelInitializer<NioSocketChannel> {
        @Override
        protected void initChannel(NioSocketChannel channel) {
            channel.pipeline().addLast(new CustomServerHandler());
        }
    }
}
