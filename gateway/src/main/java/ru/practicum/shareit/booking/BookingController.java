package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;


	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
		log.info("Get booking userId={}, bookingId={}", userId, bookingId);
		return bookingClient.get(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
								   @RequestParam(defaultValue = "ALL") BookingState state) {
		log.info("Get Allbooking userId={},state={}", userId, state);
		return bookingClient.getAll(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnersBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
											 @RequestParam(defaultValue = "ALL") BookingState state) {
		log.info("Get OwnersBooking userId={},state={}", userId, state);
		return bookingClient.getOwnersBooking(userId, state);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
							 @Valid @RequestBody BookingCreateDto bookingCreateDto) {

		log.info("Creating booking {}, userId={}", userId, bookingCreateDto);
		return bookingClient.create(userId, bookingCreateDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> changeBookingRequestStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
												 @PathVariable Long bookingId,
												 @RequestParam boolean approved) {
		log.info("Change request status={},bookingId={}", approved, bookingId);
		return bookingClient.changeBookingRequestStatus(userId, bookingId, approved);
	}
}
