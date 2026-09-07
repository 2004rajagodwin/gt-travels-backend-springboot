package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.SeatStatus;

public class SeatResponse {

    private Long seatId;
    private String seatNumber;
    private boolean bookable;
    private SeatStatus status;
    private Integer row;
    private Integer column;
    private String deck;
    private String seatLayoutType;
    private String genderRestriction;

    public SeatResponse() {
    }

    public SeatResponse(Long seatId, String seatNumber, boolean bookable, SeatStatus status) {
        this(seatId, seatNumber, bookable, status, null, null, null, null, null);
    }

    public SeatResponse(Long seatId, String seatNumber, boolean bookable, SeatStatus status,
                        Integer row, Integer column, String deck,
                        String seatLayoutType, String genderRestriction) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.bookable = bookable;
        this.status = status;
        this.row = row;
        this.column = column;
        this.deck = deck;
        this.seatLayoutType = seatLayoutType;
        this.genderRestriction = genderRestriction;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBookable() {
        return bookable;
    }

    public void setBookable(boolean bookable) {
        this.bookable = bookable;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public String getSeatLayoutType() {
        return seatLayoutType;
    }

    public void setSeatLayoutType(String seatLayoutType) {
        this.seatLayoutType = seatLayoutType;
    }

    public String getGenderRestriction() {
        return genderRestriction;
    }

    public void setGenderRestriction(String genderRestriction) {
        this.genderRestriction = genderRestriction;
    }
}
