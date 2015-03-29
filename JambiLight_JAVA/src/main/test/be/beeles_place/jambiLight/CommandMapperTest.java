package be.beeles_place.jambiLight;

import be.beeles_place.jambiLight.mocks.MockedCommand;
import be.beeles_place.jambiLight.mocks.MockedPayload;
import be.beeles_place.jambiLight.mocks.MockedPersistentCommand;
import be.beeles_place.jambiLight.utils.commanding.CommandMapper;
import be.beeles_place.jambiLight.utils.commanding.CommandMapperException;
import be.beeles_place.jambiLight.utils.commanding.commands.ICommand;
import be.beeles_place.jambiLight.utils.commanding.commands.PersistentCommand;
import be.beeles_place.jambiLight.utils.commanding.events.BaseEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.fail;

public class CommandMapperTest {

    private CommandMapper mapper;

    private MockedPayload payload;
    private BaseEvent mockedEvent;
    private BaseEvent mockedPersistentEvent;

    private ICommand<MockedPayload> mockedCommand;
    private PersistentCommand<MockedPayload> mockedPersistentCommand;

    @Before
    public void init() {
        payload = new MockedPayload();
        mockedEvent = new BaseEvent<String>() {};
        mockedPersistentEvent = new BaseEvent<String>() {};

        mockedEvent.setPayload(payload);
        mockedPersistentEvent.setPayload(payload);

        mockedCommand = new MockedCommand();
        mockedPersistentCommand = new MockedPersistentCommand();
    }

    @Test
    public void tester() {
        boolean fail = true;

        try {
            mapper = CommandMapper.getInstance();
        } catch (CommandMapperException e) {
            fail = false;
        }

        if(fail) {
            fail("Calling the getInstance() method before the init() method should throw an exception!");
        }

        //Continue test sequence!
        try {
            mapper = CommandMapper.init(null, null);
        } catch (CommandMapperException e) {
            fail("Cannot init CommandMapper instance!");
        }

        Assert.assertNotNull("CommandMap should not be null!", mapper.getCommandMap());

        mapper.mapCommand(mockedCommand.getClass(), mockedEvent.getClass());

        Assert.assertEquals("There should only be one entry in the entry set of the CommandMap", 1, mapper.getCommandMap().entrySet().size());

        mapper.mapPersistentCommand(mockedPersistentCommand.getClass(), mockedPersistentEvent.getClass());

        Assert.assertEquals("There should be two entries in the entry set of the CommandMap", 2, mapper.getCommandMap().entrySet().size());

        mapper.clearCommandMap();

        Assert.assertEquals("There should be no entries in the entry set of the CommandMap", 0, mapper.getCommandMap().entrySet().size());

        mapper.mapCommand(mockedCommand.getClass(), mockedEvent.getClass());
        mapper.mapPersistentCommand(mockedPersistentCommand.getClass(), mockedPersistentEvent.getClass());

        mapper.unmapCommand(mockedPersistentCommand.getClass());

        Assert.assertEquals("There should only be one entry in the entry set of the CommandMap", 1, mapper.getCommandMap().entrySet().size());

        mapper.mapPersistentCommand(mockedPersistentCommand.getClass(), mockedPersistentEvent.getClass());

        try {
            Method m = mapper.getClass().getDeclaredMethod("onEventReceived", BaseEvent.class);
            m.setAccessible(true);

            m.invoke(mapper, mockedEvent);
            Assert.assertEquals("Count should be 1", 1, payload.commandExecutionCounter);
            Assert.assertEquals("Count should be 0", 0, payload.persistentCommandExecutionCounter);

            m.invoke(mapper, mockedPersistentEvent);
            Assert.assertEquals("Count should be 1", 1, payload.commandExecutionCounter);
            Assert.assertEquals("Count should be 1", 1, payload.persistentCommandExecutionCounter);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}