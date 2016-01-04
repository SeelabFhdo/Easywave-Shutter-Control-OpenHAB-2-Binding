/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.shuttercontrol.internal;

import static org.openhab.binding.shuttercontrol.ShutterControlBindingConstants.*;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.shuttercontrol.handler.ShutterControlBridgeHandler;
import org.openhab.binding.shuttercontrol.handler.ShutterControlHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * The {@link ShutterControlHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Marcel Zillekens - Initial contribution
 */
public class ShutterControlHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Sets
            .union(ShutterControlBridgeHandler.SUPPORTED_THING_TYPES, ShutterControlHandler.SUPPORTED_THING_TYPES);
    private Logger logger = LoggerFactory.getLogger(ShutterControlHandlerFactory.class);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    public Thing createThing(ThingTypeUID thingTypeUID, Configuration configuration, ThingUID thingUID,
            ThingUID bridgeUID) {
        if (ShutterControlBridgeHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            ThingUID shutterBridgeUID = getBridgeThingUID(thingTypeUID, thingUID, configuration);
            return super.createThing(thingTypeUID, configuration, shutterBridgeUID, null);
        }
        if (ShutterControlHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            ThingUID shutterUID = getShutterUID(thingTypeUID, thingUID, configuration, bridgeUID);
            return super.createThing(thingTypeUID, configuration, shutterUID, bridgeUID);
        }
        throw new IllegalArgumentException("The thing type " + thingTypeUID + " is not supported by the binding.");
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        if (ShutterControlBridgeHandler.SUPPORTED_THING_TYPES.contains(thing.getThingTypeUID())) {
            return new ShutterControlBridgeHandler((Bridge) thing);
        } else if (ShutterControlHandler.SUPPORTED_THING_TYPES.contains(thing.getThingTypeUID())) {
            return new ShutterControlHandler(thing);
        }
        return null;
    }

    private ThingUID getBridgeThingUID(ThingTypeUID thingTypeUID, ThingUID thingUID, Configuration configuration) {
        if (thingUID == null) {
            String idNumber = (String) configuration.get(BRIDGE_PARAMETER_IDENTIFIER);
            thingUID = new ThingUID(thingTypeUID, idNumber);
        }
        return thingUID;
    }

    private ThingUID getShutterUID(ThingTypeUID thingTypeUID, ThingUID thingUID, Configuration configuration,
            ThingUID bridgeUID) {
        String shutterId = ((BigDecimal) configuration.get(SHUTTER_PARAMETER_ID)).toString();
        if (thingUID == null) {
            thingUID = new ThingUID(thingTypeUID, shutterId, bridgeUID.getId());
        }
        return thingUID;
    }
}
