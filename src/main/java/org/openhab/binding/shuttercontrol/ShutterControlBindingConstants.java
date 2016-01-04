/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.shuttercontrol;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link ShutterControlBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Marcel Zillekens - Initial contribution
 */
public class ShutterControlBindingConstants {

    public static final String BINDING_ID = "shuttercontrol";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_BRIDGE = new ThingTypeUID(BINDING_ID, "ShutterControlBridge");
    public final static ThingTypeUID THING_TYPE_SHUTTER = new ThingTypeUID(BINDING_ID, "Shutter");

    // List of all Channel ids
    public final static String CHANNEL_CONTROL = "control";

    // List of all bridge parameters
    public final static String BRIDGE_PARAMETER_IDENTIFIER = "naming";
    public final static String BRIDGE_PARAMETER_HOST = "host";
    public final static String BRIDGE_PARAMETER_PORT = "port";

    // List of all shutter parameters
    public final static String SHUTTER_PARAMETER_ID = "shutterId";
}
