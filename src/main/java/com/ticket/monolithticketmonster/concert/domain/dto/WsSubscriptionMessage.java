package com.ticket.monolithticketmonster.concert.domain.dto;

public record WsSubscriptionMessage(String action, Data data) {

  public record Data(String type) {}
}
