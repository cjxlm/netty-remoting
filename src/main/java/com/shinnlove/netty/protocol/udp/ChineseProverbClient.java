/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.shinnlove.netty.protocol.udp;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

/**
 * 使用UDP协议广播查询的netty客户端。
 *
 * @author shinnlove.jinsheng
 * @version $Id: ChineseProverbClient.java, v 0.1 2018-06-29 下午1:20 shinnlove.jinsheng Exp $$
 */
public class ChineseProverbClient {

    public void run(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            // 通道使用`NioDatagramChannel`类
            b.group(group).channel(NioDatagramChannel.class)
            // 广播类型
                .option(ChannelOption.SO_BROADCAST, true)
                // 增加处理器
                .handler(new ChineseProverbClientHandler());

            Channel ch = b.bind(0).sync().channel();

            // 向网段内的所有机器广播UDP消息
            ch.writeAndFlush(
                new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询?", CharsetUtil.UTF_8),
                    new InetSocketAddress("255.255.255.255", port))).sync();

            if (!ch.closeFuture().await(15000)) {
                System.out.println("查询超时!");
            }
        } finally {
            // 优雅关闭线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new ChineseProverbClient().run(port);
    }

}