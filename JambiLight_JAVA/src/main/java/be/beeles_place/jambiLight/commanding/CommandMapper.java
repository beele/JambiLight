package be.beeles_place.jambiLight.commanding;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.commands.PersistentCommand;
import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CommandMapper {

    //Singleton instance variable.
    private static CommandMapper instance;

    //Guava eventbus instance.
    private final EventBus eventBus;

    //Internal state.
    private Map<Class, Class> commandMap;
    private List<PersistentCommand> persistentCommands;

    //Injectables.
    private final SettingsModel settings;
    private final ColorModel model;

    /**
     * Call this method to initialize the CommandMapper logic.
     * <p>
     *     Calling any other method before this method will result in an CommandMapperException being thrown.
     * </p>
     *
     * @param settings The SettingsModel class instance to inject in the ICommand classes.
     * @param model The ColorModel class instance to inject in the ICommand classes.
     * @return The created CommandMapper instance.
     * @throws CommandMapperException When calling this method for any time after the first time.
     */
    public static CommandMapper init(SettingsModel settings, ColorModel model) throws CommandMapperException {
        if(instance == null) {
            instance = new CommandMapper(settings, model);
            return instance;
        } else {
            throw new CommandMapperException("CommandMapper init() already called! Please use getInstance() after calling init()");
        }
    }

    /**
     * Shortcut method to retrieve the CommandMapper instance after the init() method was called.
     * <p>
     *     Calling this method before the init() method was called will result in a CommandMapperException being thrown.
     * </p>
     *
     * @return The created CommandMapper instance.
     * @throws CommandMapperException When calling this method before the init() method has been called.
     */
    public static CommandMapper getInstance() throws CommandMapperException {
        if(instance == null) {
            throw new CommandMapperException("CommandMapper init() not called before calling getInstance()! Call init() before calling getInstance()");
        } else {
            return instance;
        }
    }

    /**
     * Private constructor.
     * <p>
     *     This will do the following:
     *     <li>
     *         <ul>Sets the injectables to their correct values (if any)</ul>
     *         <ul>Create a new Guava eventbus instance</ul>
     *         <ul>Create a new HashMap to store the commands and event relations in.</ul>
     *     </li>
     * </p>
     *
     * @param settings The SettingsModel class instance to inject in the ICommand classes.
     * @param model The ColorModel class instance to inject in the ICommand classes.
     */
    private CommandMapper(SettingsModel settings, ColorModel model) {
        this.settings = settings;
        this.model = model;

        eventBus = EventbusWrapper.getInstance();
        eventBus.register(this);
        commandMap = new HashMap<>();
        persistentCommands = new ArrayList<>();
    }

    /**
     * Maps the given commandClass to the given eventClass.
     * <p>
     *     When an event of the given type is dispatched, the command of the given type will be created and executed.
     *     After execution the command is disposed.
     * </p>
     *
     * @param commandClass The class to create and execute. This should always extend ICommand.
     * @param eventClass The class of the event you wish to dispatch for the command to be executed. This should always extend BaseEvent.
     */
    public void mapCommand(Class<? extends ICommand> commandClass, Class<? extends BaseEvent> eventClass) {
        commandMap.put(eventClass, commandClass);
    }

    /**
     * Maps the given commandClass to the given eventClass.
     * <p>
     *     When an event of the given type is dispatched, the command of the given type will be created and started.
     *     It will keep running (if implemented that way) until the given eventClass is dispatched again. After the event is dispatched a second
     *     time the command will be stopped and is disposed.
     * </p>
     *
     * @param commandClass The class to create and execute. This should always extend ICommand.
     * @param toggleEventClass The class of the event you wish to dispatch for the command to be executed. This should always extend BaseEvent.
     */
    public void mapPersistentCommand(Class<? extends PersistentCommand> commandClass, Class<? extends BaseEvent> toggleEventClass) {
        commandMap.put(toggleEventClass, commandClass);
    }

    /**
     * Removed any previously made mapping for the given commandClass.
     * <p>
     *     This will not stop a PersistentCommand instance that is currently running!
     *     Stop it before unmapping!
     * </p>
     *
     * @param commandClass The class to remove the mapping for. If no mapping is found, nothing will happen.
     */
    public void unmapCommand(Class<?> commandClass) {
        for (Map.Entry<Class, Class> commandEntry : commandMap.entrySet()) {
            if (commandEntry.getValue().equals(commandClass)) {
                commandMap.remove(commandEntry.getKey());
                break;
            }
        }
    }

    /**
     * Removes all previously made mappings.
     * <p>
     *     This will not stop any PersistentCommand instances that are currently running!
     *     Stop them before unmapping!
     * </p>
     */
    public void clearCommandMap() {
        commandMap = new HashMap<>();
    }

    /**
     * Dispatches an event to the internal eventbus.
     *
     * @param event The event to dispatch.
     */
    public void dispatchEvent(BaseEvent event) {
        eventBus.post(event);
    }

    @Subscribe
    @SuppressWarnings("unchecked")
    public void onEventReceived(BaseEvent event) {
        System.out.println("Event received => " + event.getClass().toString());

        for (Class eventClass : commandMap.keySet()) {
            if (eventClass.equals(event.getClass())) {
                Class commandClass = commandMap.get(eventClass);
                ICommand command;

                if(commandClass != null) {
                    System.out.println("Command for event found => " + commandClass.toString());
                } else {
                    System.out.println("No matching command found!");
                    return;
                }

                try {
                    //If the commandClass has a superclass it means it extends from the PersistentCommand!
                    if(PersistentCommand.class.isAssignableFrom(commandClass)) {

                        PersistentCommand matchedCommand = null;
                        //Loop over all saved persistent commands.
                        for (PersistentCommand persistentCommand : persistentCommands) {
                            if(persistentCommand.getClass().getName().equals(commandClass.getName())) {
                                matchedCommand = persistentCommand;
                            }
                        }

                        if(matchedCommand != null) {
                            //Stop the matched command and remove it from the list of persistent commands.
                            matchedCommand.stop();
                            persistentCommands.remove(matchedCommand);
                        } else {
                            //Create a new instance of the commandClass and start execution.
                            command = (PersistentCommand) commandClass.newInstance();
                            ((PersistentCommand) command).start(settings, model, event);
                            persistentCommands.add((PersistentCommand) command);
                        }

                    } else {
                        command = (ICommand) commandClass.newInstance();
                        command.execute(settings, model, event);
                    }

                    if(event.getCallback() != null) {
                        event.getCallback().run();
                    }
                } catch (Exception e) {
                    //TODO: Do error callback here and work with different exception types!
                    System.out.println("Cannot create Command instance => " + e.getMessage());
                }

                return;
            }
        }
    }

    //Getters & setters:
    public EventBus getEventBus() {
        return eventBus;
    }

    public Map<Class, Class> getCommandMap() {
        return commandMap;
    }
}