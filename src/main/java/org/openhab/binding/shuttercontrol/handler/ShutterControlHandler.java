/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.shuttercontrol.handler;

import static org.openhab.binding.shuttercontrol.ShutterControlBindingConstants.*;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * The {@link ShutterControlHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Marcel Zillekens - Initial contribution
 */
public class ShutterControlHandler extends BaseThingHandler {

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES = Sets.newHashSet(THING_TYPE_SHUTTER);
    private Integer shutterId;
    private ShutterControlBridgeHandler bridgeHandler;
    private Logger logger = LoggerFactory.getLogger(ShutterControlHandler.class);

    public ShutterControlHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        bridgeHandler.executeCommand(channelUID, command, shutterId);
    }

    @Override
    public void initialize() {
        Configuration config = thing.getConfiguration();
        final Integer configurationShutterId = ((BigDecimal) config.get(SHUTTER_PARAMETER_ID)).intValue();
        logger.debug("Configuration shutter id: " + configurationShutterId);
        if (configurationShutterId != null) {
            shutterId = configurationShutterId;
            if (getBridgeHandler() != null) {
                ThingStatusInfo statusInfo = getBridge().getStatusInfo();
                updateStatus(statusInfo.getStatus(), statusInfo.getStatusDetail(), statusInfo.getDescription());
            }
        }
    }

    @Override
    public void dispose() {
    }

    private synchronized ShutterControlBridgeHandler getBridgeHandler() {
        if (bridgeHandler == null) {
            Bridge bridge = getBridge();
            if (bridge == null) {
                return null;
            }
            ThingHandler handler = bridge.getHandler();
            if (handler instanceof ShutterControlBridgeHandler) {
                bridgeHandler = (ShutterControlBridgeHandler) handler;
                // bridgeHandler.addShutterListener(this);
            } else {
                return null;
            }
        }
        return bridgeHandler;
    }
}