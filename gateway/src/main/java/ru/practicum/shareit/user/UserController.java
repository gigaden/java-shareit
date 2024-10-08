package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Collection;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
	private final BookingClient bookingClient;
	private final UserClient userClient;

//	@GetMapping
//	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
//			@RequestParam(name = "state", defaultValue = "all") String stateParam,
//			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
//			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
//		BookingState state = BookingState.from(stateParam)
//				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
//		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
//		return bookingClient.getBookings(userId, state, from, size);
//	}

//	@PostMapping
//	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
//			@RequestBody @Valid BookItemRequestDto requestDto) {
//		log.info("Creating booking {}, userId={}", requestDto, userId);
//		return bookingClient.bookItem(userId, requestDto);
//	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto) {
		log.info("Creating user {}", userDto);
		return userClient.create(userDto);
	}

//	@GetMapping("/{bookingId}")
//	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
//			@PathVariable Long bookingId) {
//		log.info("Get booking {}, userId={}", bookingId, userId);
//		return bookingClient.getBooking(userId, bookingId);
//	}


	@GetMapping
	public ResponseEntity<Object> getAll() {
		log.info("Get all users");
		return userClient.getAll();
	}

	@GetMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> get(@PathVariable long userId) {
		log.info("Get user, userId={}", userId);
		return userClient.get(userId);
	}


	@PutMapping
	public ResponseEntity<Object> update(@Valid @RequestBody UserDto userDto) {
		log.info("Update user ={}", userDto);
		return userClient.update(userDto);
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<Object> patch(@PathVariable long userId, @Valid @RequestBody UserDto userDto) {
		log.info("Patch user, userId={}", userId);
		return userClient.patch(userId, userDto);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Object>  delete(@PathVariable long userId) {
		log.info("Delete user, userId={}", userId);
		return userClient.delete(userId);
	}
}
