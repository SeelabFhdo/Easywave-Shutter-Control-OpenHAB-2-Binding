package org.openhab.binding.shuttercontrol.handler;

import static org.openhab.binding.shuttercontrol.ShutterControlBindingConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.StopMoveType;
import org.eclipse.smarthome.core.library.types.UpDownType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.shuttercontrol.ShutterCommandValue;
import org.openhab.binding.shuttercontrol.ShutterControlBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutterControlBridgeHandler extends BaseBridgeHandler {

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_BRIDGE);

    private String host;
    private Integer port;
    private Logger logger = LoggerFactory.getLogger(ShutterControlBridgeHandler.class);

    public ShutterControlBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void dispose() {
        // TODO: Disconnect
    }

    @Override
    public void initialize() {
        Configuration config = thing.getConfiguration();
        host = (String) config.get(ShutterControlBindingConstants.BRIDGE_PARAMETER_HOST);
        port = ((BigDecimal) config.get(ShutterControlBindingConstants.BRIDGE_PARAMETER_PORT)).intValue();
        // TODO: Connect to Bridge / Set status to online
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // Handled in ShutterControlHandler
    }

    public void executeCommand(ChannelUID channelUID, Command command, Integer shutterId) {
        if (channelUID.getId().equals(CHANNEL_CONTROL)) {
            Integer shutterCommandValue = ShutterCommandValue.STOP.ordinal();
            if (command instanceof UpDownType) {
                shutterCommandValue = command == UpDownType.UP ? ShutterCommandValue.UP.ordinal()
                        : ShutterCommandValue.DOWN.ordinal();
            } else if (command instanceof StopMoveType) {
                shutterCommandValue = ShutterCommandValue.STOP.ordinal();
            }

            // Send command to bridge via HTTP request
            try {
                URL url = buildBridgeCommandURL(shutterId, shutterCommandValue);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader((connection.getInputStream())));
                String output;
                while ((output = bufferedReader.readLine()) != null) {
                    logger.debug(output);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("Unknown channel UID.");
        }
    }

    private URL buildBridgeCommandURL(Integer shutterId, Integer shutterCommandValue) throws MalformedURLException {
        return new URL("http://" + host + ":" + port + "/shutter?params=" + shutterId + "," + shutterCommandValue);
    }
}
