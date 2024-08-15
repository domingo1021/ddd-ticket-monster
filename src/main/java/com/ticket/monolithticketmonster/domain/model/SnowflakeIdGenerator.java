package com.ticket.monolithticketmonster.domain.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SnowflakeIdGenerator {

  private static final long EPOCH = 1609459200000L; // Start time: January 1, 2021
  private static final long MACHINE_ID_BITS =
      10L; // Increased to 10 bits to accommodate more unique IDs
  private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
  private static SnowflakeIdGenerator instance;
  private final long machineId;
  private final long startupTimestamp;
  private long id;

  public SnowflakeIdGenerator(int port) throws UnknownHostException {
    this.machineId = getMachineId(port);
    this.startupTimestamp = System.currentTimeMillis();
    if (machineId > MAX_MACHINE_ID) {
      throw new IllegalArgumentException("Machine ID is too large.");
    }
    this.id = this.getId();
  }

  private long getMachineId(int port) throws UnknownHostException {
    InetAddress ip = InetAddress.getLocalHost();
    String hostAddress = ip.getHostAddress();

    // Combine the hash of the IP address and the port number
    int ipHash = hostAddress.hashCode() & 0xFFFF; // Limit to 16 bits
    int portHash = port & 0xFFFF; // Limit to 16 bits

    long combinedHash = ((long) ipHash << 16) | (portHash & 0xFFFF);

    return combinedHash % (MAX_MACHINE_ID + 1); // Ensure it fits within the machine ID bits
  }

  public long getId() {
    if (id == 0) {
      this.id = ((startupTimestamp - EPOCH) << (MACHINE_ID_BITS)) | machineId;
    }
    return this.id;
  }
}
