package com.godwintech.gttravels.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godwintech.gttravels.enums.GenderRestriction;
import com.godwintech.gttravels.enums.SeatLayoutType;
import com.godwintech.gttravels.enums.SeatStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    private boolean bookable;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    @JsonIgnore
    private Bus bus;

    @Column(name = "row_number")
    private Integer rowNumber;

    @Column(name = "column_number")
    private Integer columnNumber;

    private String deck;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_layout_type")
    private SeatLayoutType seatLayoutType = SeatLayoutType.SEATER;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_restriction")
    private GenderRestriction genderRestriction = GenderRestriction.GENERAL;

    private Long lockedBy;

    private LocalDateTime lockedAt;

    public Seat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public SeatLayoutType getSeatLayoutType() {
        return seatLayoutType;
    }

    public void setSeatLayoutType(SeatLayoutType seatLayoutType) {
        this.seatLayoutType = seatLayoutType;
    }

    public GenderRestriction getGenderRestriction() {
        return genderRestriction;
    }

    public void setGenderRestriction(GenderRestriction genderRestriction) {
        this.genderRestriction = genderRestriction;
    }

    public Long getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Long lockedBy) {
        this.lockedBy = lockedBy;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }
}
