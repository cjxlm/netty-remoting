/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.shinnlove.netty.codec.serializable.netty;

import com.shinnlove.netty.codec.pojo.SubscribeReq;
import com.shinnlove.netty.codec.pojo.SubscribeResp;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * netty服务端请求对象处理器，msg直接是具体的对象。
 *
 * @author shinnlove.jinsheng
 * @version $Id: SubReqServerHandler.java, v 0.1 2018-06-29 下午1:55 shinnlove.jinsheng Exp $$
 */
@Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {

    /**
     * 服务端通道可读时。
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq req = (SubscribeReq) msg;
        if ("Lilinfeng".equalsIgnoreCase(req.getUserName())) {
            System.out.println("Service accept client subscrib req : [" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    private SubscribeResp resp(int subReqID) {
        SubscribeResp resp = new SubscribeResp();
        resp.setSubReqID(subReqID);
        resp.setRespCode(0);
        resp.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }

}