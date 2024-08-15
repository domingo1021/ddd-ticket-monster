package com.ticket.monolithticketmonster.presentation.dto;

public record WsSubscriptionMessage(String action, Data data) {

  public record Data(String type) {}
}
