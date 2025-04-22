# final-sorsix-project
### ✅ Booking Validations Overview (for README)

This section documents the key business logic validations implemented in the `BookingService` to ensure reliable and conflict-free scheduling between ServiceSeekers and ServiceProviders.

---

### 🔒 1. `validateNoDuplicateBooking(seeker, slotId)`
**Purpose:** Prevents a ServiceSeeker from booking the same time slot more than once.

- Ensures one user cannot submit multiple bookings for the same slot.
- Skips cancelled bookings.

**Throws:** `IllegalStateException` if a duplicate exists.

---

### 🔒 2. `validateSlotNotAlreadyConfirmed(slotId)`
**Purpose:** Prevents overbooking of a time slot.

- A time slot can only have **one confirmed** booking.
- Ensures that only one booking can be accepted by a provider per slot.

**Throws:** `IllegalStateException` if any booking for the slot is already `CONFIRMED`.

---

### 🔒 3. `validateProviderAvailability(providerId, slotId)`
**Purpose:** Ensures a ServiceProvider is not double-booked.

- Even if a slot appears available, this checks that **the same provider** is not confirmed for the same time in another booking.
- Protects the provider's schedule across multiple services.

**Throws:** `IllegalStateException` if a `CONFIRMED` booking by the same provider exists for the slot.

---

### 🔁 4. `hasProviderConflict(providerId, slotId)` (used in `completeBooking`)
**Purpose:** Prevents creating a recurring booking if the provider is already booked for that next slot.

- Called internally before auto-generating a recurring booking.

**Returns:** `true` if provider has a conflict.

---

### 📅 Overall Booking Creation Logic
In `createBooking(...)`, all three main validations are executed:
```kotlin
validateNoDuplicateBooking(...)
validateSlotNotAlreadyConfirmed(...)
validateProviderAvailability(...)
```

---

These validations work together to guarantee:
- ServiceSeekers don’t double-book
- Slots aren’t overbooked
- Providers don’t overlap bookings
- Recurring bookings are safe

✅ Keeping this logic centralized makes the system consistent and maintainable.
