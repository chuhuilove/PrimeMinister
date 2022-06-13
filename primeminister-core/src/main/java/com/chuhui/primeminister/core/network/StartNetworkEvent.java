package com.chuhui.primeminister.core.network;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * StartNettyEvent
 *
 * @author: cyzi
 * @Date: 6/4/22
 * @Description:
 */
public class StartNetworkEvent extends ApplicationContextEvent {
    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public StartNetworkEvent(ApplicationContext source) {
        super(source);
    }
}
