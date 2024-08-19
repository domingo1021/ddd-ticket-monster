package com.ticket.monolithticketmonster.user.application.exception;

public class ConstantExceptionCode {
  public static final int GENERAL_BAD_REQUEST = 40000;
  public static final int TICKET_UNAVAILABLE = 40001;
  public static final int GENERAL_AUTH_FAILED = 40100;
  public static final int JWT_AUTH_FAILED = 40101;
  public static final int OAUTH_USER_NO_PWD = 40102;
  public static final int USER_ALREADY_EXIST = 40103;
  public static final int NOT_FOUND = 40400;
  public static final int INTERNAL_SERVER_ERROR = 50000;
}
