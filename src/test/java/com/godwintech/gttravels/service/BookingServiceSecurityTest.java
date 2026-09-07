package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.exception.ForbiddenException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingServiceSecurityTest {

  @Test
  void verifyCustomerOwnership_allowsOwner() throws Exception {
    BookingService service = new BookingService(
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    User owner = new User();
    owner.setId(1L);

    User requester = new User();
    requester.setId(1L);

    Booking booking = new Booking();
    booking.setPassenger(owner);

    Method method = BookingService.class.getDeclaredMethod(
        "verifyCustomerOwnership", Booking.class, User.class);
    method.setAccessible(true);

    assertDoesNotThrow(() -> {
      try {
        method.invoke(service, booking, requester);
      } catch (Exception e) {
        throw e.getCause();
      }
    });
  }

  @Test
  void verifyCustomerOwnership_rejectsOtherUser() throws Exception {
    BookingService service = new BookingService(
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    User owner = new User();
    owner.setId(1L);

    User requester = new User();
    requester.setId(2L);

    Booking booking = new Booking();
    booking.setPassenger(owner);

    Method method = BookingService.class.getDeclaredMethod(
        "verifyCustomerOwnership", Booking.class, User.class);
    method.setAccessible(true);

    assertThrows(ForbiddenException.class, () -> {
      try {
        method.invoke(service, booking, requester);
      } catch (Exception e) {
        throw e.getCause();
      }
    });
  }

  @Test
  void failedStatus_existsForExpiredBookings() {
    assertDoesNotThrow(() -> BookingStatus.valueOf("FAILED"));
  }
}
