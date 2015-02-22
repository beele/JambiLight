package be.beeles_place.jambiLight.utils.commanding;

import be.beeles_place.jambiLight.utils.commanding.commands.ICommand;
import be.beeles_place.jambiLight.utils.commanding.events.IEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class CommandMapper {

    private final EventBus eventBus;
    private Map<Class, Class> commandMap;

    public CommandMapper() {
        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);
        commandMap = new HashMap<Class, Class>();
    }

    public void mapCommand(Class<? extends ICommand> commandClass, Class<? extends IEvent> eventClass) {
        commandMap.put(eventClass, commandClass);
    }

    public void unMapCommand(Class<?> commandClass) {
        for (Map.Entry<Class, Class> commandEntry : commandMap.entrySet()) {
            if (commandEntry.getValue().equals(commandClass)) {
                commandMap.remove(commandEntry.getKey());
                break;
            }
        }
    }

    public void clearCommandMap() {
        commandMap = new HashMap<Class, Class>();
    }

    @Subscribe
    public void onEventReceived(IEvent event) {
        System.out.println("Event received => " + event.getClass().toString());
        for (Class eventClass : commandMap.keySet()) {
            if (eventClass.equals(event.getClass())) {
                Class commandClass = commandMap.get(eventClass);
                try {
                    ICommand command = (ICommand) commandClass.newInstance();
                    command.execute();
                } catch (Exception e) {
                    System.out.println("Cannot create Command instance => " + e.getMessage());
                }
                break;
            }
        }
    }
}