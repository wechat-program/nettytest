import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author wujianchuan 2019/11/12
 * @version 1.0
 */
public class CustomServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 收到客户端消息，自动触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] reg = new byte[buf.readableBytes()];
        buf.readBytes(reg);
        String body = new String(reg, "UTF-8");
        System.out.println(Thread.currentThread().getName() + ",The server receive  order : " + body);

        String respMsg = "I am Server，消息接收 success!";
        ByteBuf respByteBuf = Unpooled.copiedBuffer(respMsg.getBytes());
        ctx.write(respByteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*flush：将消息发送队列中的消息写入到 SocketChannel 中发送给对方，为了频繁的唤醒 Selector 进行消息发送
         * Netty 的 write 方法并不直接将消息写入 SocketChannel 中，调用 write 只是把待发送的消息放到发送缓存数组中，再通过调用 flush
         * 方法，将发送缓冲区的消息全部写入到 SocketChannel 中
         * */
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /*当发生异常时，关闭 ChannelHandlerContext，释放和它相关联的句柄等资源 */
        ctx.close();
    }
}
