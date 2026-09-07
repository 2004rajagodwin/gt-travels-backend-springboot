package com.godwintech.gttravels.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.IndianBusType;
import com.godwintech.gttravels.enums.SeatConfigurationType;
import com.godwintech.gttravels.enums.SeatLayoutType;
import com.godwintech.gttravels.enums.SeatStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String busNumber;

    @Column(name = "bus_name")
    private String busName;

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String busType;

    @Enumerated(EnumType.STRING)
    @Column(name = "bus_category")
    private IndianBusType busCategory;

    @Column(name = "ac_type")
    private String acType;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_configuration")
    private SeatConfigurationType seatConfiguration = SeatConfigurationType.SEATER;

    @Column(nullable = false)
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusStatus status = BusStatus.ACTIVE;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private User operator;

    @JsonIgnore
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusImage> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "bus_amenities",
            joinColumns = @JoinColumn(name = "bus_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    public Bus() {
    }

    public void initializeSeats() {
        seats.clear();

        if (totalSeats == null || totalSeats <= 0) {
            totalSeats = 40;
        }

        SeatConfigurationType config = seatConfiguration != null
                ? seatConfiguration
                : SeatConfigurationType.SEATER;

        if (config == SeatConfigurationType.SLEEPER
                || config == SeatConfigurationType.DOUBLE_SLEEPER
                || config == SeatConfigurationType.SEMI_SLEEPER) {
            initializeSleeperLayout(config);
        } else {
            initializeSeaterLayout();
        }
    }

    private void initializeSeaterLayout() {
        int pairs = totalSeats / 2;
        for (int row = 1; row <= pairs; row++) {
            seats.add(createSeat("L" + row, row, 1, SeatLayoutType.SEATER, null));
            seats.add(createSeat("R" + row, row, 2, SeatLayoutType.SEATER, null));
        }
        if (totalSeats % 2 != 0) {
            seats.add(createSeat("C1", pairs + 1, 1, SeatLayoutType.SEATER, null));
        }
    }

    private void initializeSleeperLayout(SeatConfigurationType config) {
        SeatLayoutType layoutType = config == SeatConfigurationType.SEMI_SLEEPER
                ? SeatLayoutType.SEMI_SLEEPER
                : config == SeatConfigurationType.DOUBLE_SLEEPER
                ? SeatLayoutType.DOUBLE_SLEEPER
                : SeatLayoutType.SLEEPER;

        int lowerCount = totalSeats / 2;
        for (int i = 1; i <= lowerCount; i++) {
            seats.add(createSeat("L" + i, i, 1, layoutType, "LOWER"));
        }
        for (int i = 1; i <= totalSeats - lowerCount; i++) {
            seats.add(createSeat("U" + i, i, 2, layoutType, "UPPER"));
        }
    }

    private Seat createSeat(String number, int row, int col, SeatLayoutType layoutType, String deck) {
        Seat seat = new Seat();
        seat.setSeatNumber(number);
        seat.setBookable(true);
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setBus(this);
        seat.setRowNumber(row);
        seat.setColumnNumber(col);
        seat.setSeatLayoutType(layoutType);
        if (deck != null) {
            seat.setDeck(deck);
        }
        return seat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public IndianBusType getBusCategory() {
        return busCategory;
    }

    public void setBusCategory(IndianBusType busCategory) {
        this.busCategory = busCategory;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public SeatConfigurationType getSeatConfiguration() {
        return seatConfiguration;
    }

    public void setSeatConfiguration(SeatConfigurationType seatConfiguration) {
        this.seatConfiguration = seatConfiguration;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public BusStatus getStatus() {
        return status;
    }

    public void setStatus(BusStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<BusImage> getImages() {
        return images;
    }

    public void setImages(List<BusImage> images) {
        this.images = images;
    }

    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }
}
