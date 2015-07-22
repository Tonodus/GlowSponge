package net.glowstone.service.event;

import com.google.common.eventbus.Subscribe;
import net.glowstone.GlowGame;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventHandler;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.service.event.EventManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class GlowEventManager implements EventManager {
    private final GlowGame game;
    private final List<Listener> listeners;
    private final Comparator<Listener> orderComparator;

    public GlowEventManager(GlowGame game) {
        this.game = game;
        listeners = new ArrayList<>();
        orderComparator = new OrderComparator();
    }

    @Override
    public void register(Object plugin, Object obj) {
        for (Method method : obj.getClass().getMethods()) {
            Subscribe ann = method.getAnnotation(Subscribe.class);
            if (ann != null) {
                if (method.getParameterCount() == 1) {
                    if (Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        listeners.add(new Listener(plugin, (Class<? extends Event>) method.getParameterTypes()[0], method, obj));
                    }
                }
            }
        }
    }

    @Override
    public <T extends Event> void register(Object plugin, Class<T> eventClass, EventHandler<? super T> handler) {
        register(plugin, eventClass, Order.DEFAULT, handler);
    }

    @Override
    public <T extends Event> void register(Object plugin, Class<T> eventClass, Order order, EventHandler<? super T> handler) {
        listeners.add(new Listener(plugin, eventClass, order, handler));
    }

    @Override
    public void unregister(Object obj) {
        Iterator<Listener> it = listeners.iterator();
        while (it.hasNext()) {
            if (it.next().isHandler(obj)) {
                it.remove();
            }
        }
    }

    @Override
    public void unregisterPlugin(Object plugin) {
        Iterator<Listener> it = listeners.iterator();
        while (it.hasNext()) {
            if (it.next().getPlugin() == plugin) {
                it.remove();
            }
        }
    }

    @Override
    public boolean post(Event event) {
        broadcast(event);
        if (event instanceof Cancellable) {
            return ((Cancellable) event).isCancelled();
        }
        return false;
    }

    public <T extends Event> T broadcast(T event) {
        Collection<Listener> toCall = new TreeSet<>(orderComparator);

        Class eventClass = event.getClass();
        for (Listener listener : listeners) {
            if (listener.getEventType().isAssignableFrom(eventClass)) {
                toCall.add(listener);
            }
        }

        for (Listener listener : toCall) {
            listener.post(event);
        }

        return event;
    }

    private static final class Listener {
        private final Object plugin;
        private final Class<? extends Event> eventType;
        private final Order order;

        private final Method method;
        private final Object handlerObj;
        private final EventHandler handler;

        public Listener(Object plugin, Class<? extends Event> eventType, Method method, Object handler) {
            this.plugin = plugin;
            this.eventType = eventType;
            this.order = Order.DEFAULT;
            this.method = method;
            this.handlerObj = handler;
            this.handler = null;
        }

        public <T extends Event> Listener(Object plugin, Class<T> eventType, Order order, EventHandler<? super T> handler) {
            this.plugin = plugin;
            this.eventType = eventType;
            this.order = order;
            this.method = null;
            this.handlerObj = null;
            this.handler = handler;
        }

        public boolean isHandler(Object obj) {
            return handlerObj == obj;
        }

        public Object getPlugin() {
            return plugin;
        }

        public Class<? extends Event> getEventType() {
            return eventType;
        }

        public <T extends Event> void post(T event) {
            if (handler != null) {
                try {
                    handler.handle(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    method.invoke(handlerObj, event);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public Order getOrder() {
            return order;
        }
    }

    private class OrderComparator implements Comparator<Listener> {
        @Override
        public int compare(Listener o1, Listener o2) {
            return o1.getOrder().compareTo(o2.getOrder());
        }
    }
}
