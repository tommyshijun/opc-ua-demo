/**
 * Prosys OPC UA Java SDK
 * Copyright (c) Prosys OPC Ltd.
 * <http://www.prosysopc.com>
 * All rights reserved.
 */
package com.prosysopc.ua.samples.client;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.stack.builtintypes.DataValue;
import com.prosysopc.ua.stack.builtintypes.LocalizedText;
import com.prosysopc.ua.stack.builtintypes.UnsignedInteger;
import com.prosysopc.ua.stack.core.ApplicationDescription;
import com.prosysopc.ua.stack.core.ApplicationType;
import com.prosysopc.ua.stack.core.Attributes;
import com.prosysopc.ua.stack.core.Identifiers;
import com.prosysopc.ua.stack.transport.security.SecurityMode;
import com.prosysopc.ua.stack.utils.AttributesUtil;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Timer;

/**
 * A very minimal client application. Connects to the server and reads one variable. Works with a
 * non-secure connection.
 */
public class SfExpressClient {

  public static void main(String[] args) throws Exception {
    UaClient client = new UaClient("opc.tcp://localhost:52520/OPCUA/SampleConsoleServer");
    client.setSecurityMode(SecurityMode.NONE);
    initialize(client);
    client.connect();

    // Simulate request every 80 milliseconds
    ScheduledTask st = new ScheduledTask(client);
    Timer timer = new Timer();
    timer.schedule(st, 0, 80);
  }

  /**
   * Define a minimal ApplicationIdentity. If you use secure connections, you will also need to
   * define the application instance certificate and manage server certificates. See the
   * SampleConsoleClient.initialize() for a full example of that.
   */
  protected static void initialize(UaClient client) throws SecureIdentityException, IOException, UnknownHostException {
    // *** Application Description is sent to the server
    ApplicationDescription appDescription = new ApplicationDescription();
    appDescription.setApplicationName(new LocalizedText("SimpleClient", Locale.ENGLISH));
    // 'localhost' (all lower case) in the URI is converted to the actual
    // host name of the computer in which the application is run
    appDescription.setApplicationUri("urn:localhost:UA:SimpleClient");
    appDescription.setProductUri("urn:prosysopc.com:UA:SimpleClient");
    appDescription.setApplicationType(ApplicationType.Client);

    final ApplicationIdentity identity = new ApplicationIdentity();
    identity.setApplicationDescription(appDescription);
    client.setApplicationIdentity(identity);
  }

}
