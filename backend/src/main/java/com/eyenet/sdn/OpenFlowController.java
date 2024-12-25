package com.eyenet.sdn;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OpenFlowController {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenFlowController.class);
    
    @Value("${openflow.port:6653}")
    private int port;
    
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Map<ChannelId, Channel> switchConnections;
    
    public OpenFlowController() {
        this.switchConnections = new ConcurrentHashMap<>();
    }
    
    @PostConstruct
    public void init() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                            .addLast(new LengthFieldBasedFrameDecoder(65535, 2, 2, -4, 0))
                            .addLast(new OpenFlowMessageDecoder())
                            .addLast(new OpenFlowMessageHandler(switchConnections));
                    }
                });
            
            Channel channel = b.bind(port).sync().channel();
            logger.info("OpenFlow controller started on port {}", port);
            
        } catch (Exception e) {
            logger.error("Failed to start OpenFlow controller", e);
            shutdown();
        }
    }
    
    @PreDestroy
    public void shutdown() {
        for (Channel channel : switchConnections.values()) {
            channel.close();
        }
        switchConnections.clear();
        
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        logger.info("OpenFlow controller stopped");
    }
}
