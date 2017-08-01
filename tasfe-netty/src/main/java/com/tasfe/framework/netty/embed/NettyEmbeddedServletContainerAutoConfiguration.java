package com.tasfe.framework.netty.embed;

import io.netty.bootstrap.Bootstrap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration.BeanPostProcessorsRegistrar;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for an embedded servlet containers.
 *
 * @author lait.zhang@gmail.com
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnWebApplication
@Import(BeanPostProcessorsRegistrar.class)
public class NettyEmbeddedServletContainerAutoConfiguration {
    @Configuration
    @ConditionalOnClass({Bootstrap.class})
    @ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
    public static class EmbeddedNetty {
        @Bean
        public NettyEmbeddedServletContainerFactory nettyEmbeddedServletContainerFactory() {
            return new NettyEmbeddedServletContainerFactory();
        }
    }
}
