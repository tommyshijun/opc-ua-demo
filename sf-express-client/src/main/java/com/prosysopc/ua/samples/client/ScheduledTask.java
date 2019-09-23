package com.prosysopc.ua.samples.client;

import com.prosysopc.ua.DataTypeConversionException;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaVariable;
import com.prosysopc.ua.stack.builtintypes.DataValue;
import com.prosysopc.ua.stack.builtintypes.NodeId;
import com.prosysopc.ua.stack.builtintypes.UnsignedInteger;
import com.prosysopc.ua.stack.core.Attributes;
import com.prosysopc.ua.stack.core.Identifiers;

import java.util.TimerTask;

/**
 * TODO: Add description
 *
 * @author 816856
 * @date 23/09/2019
 */
public class ScheduledTask extends TimerTask {

    private UaClient client;
    private int maxTasks = 10;
    private int currentTask = 1;

    ScheduledTask(UaClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Current task : " + currentTask++);
        try {
            DataValue value = client.readValue(Identifiers.Server_ServerStatus_State);
            System.out.println(value);
//            write(Identifiers.Server_ServerStatus_State);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (maxTasks == currentTask) {
            client.disconnect();
            System.out.println("Application Terminates");
            System.exit(0);
        }
    }

    /**
     * @param nodeId
     * @throws StatusException
     * @throws AddressSpaceException
     * @throws ServiceException
     */
    protected void write(NodeId nodeId) throws ServiceException, AddressSpaceException, StatusException {
        UnsignedInteger attributeId = Attributes.Value;

        UaNode node = client.getAddressSpace().getNode(nodeId);
        System.out.println("Writing to node " + nodeId + " - " + node.getDisplayName().getText());

        // Find the DataType if setting Value - for other properties you must
        // find the correct data type yourself
        UaDataType dataType = null;
        if (attributeId.equals(Attributes.Value) && (node instanceof UaVariable)) {
            UaVariable v = (UaVariable) node;
            dataType = v.getDataType();
            System.out.println("DataType: " + dataType.getDisplayName().getText());
        }

        String value = "abcdef";
        try {
            boolean status = client.writeAttribute(nodeId, attributeId, value, true);
            if (status) {
                System.out.println("OK");
            } else {
                System.out.println("OK (completes asynchronously)");
            }
        } catch (ServiceException | DataTypeConversionException | StatusException e) {
            e.printStackTrace();
        }

    }

}
