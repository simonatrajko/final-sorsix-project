INSERT INTO service_category (name) VALUES
                                        ('Cleaning'),
                                        ('Plumbing'),
                                        ('Tutoring');

INSERT INTO users (
    username, password, email, full_name, profile_image, location,
    user_type, years_of_experience, bio, languages
) VALUES (
             'provider1', 'password', 'provider1@mail.com', 'Elena Provider', '', 'Skopje',
             'PROVIDER', 7, 'Specialist in house cleaning', 'English,Macedonian'
         );

INSERT INTO users (
    username, password, email, full_name, profile_image, location,
    user_type, preferred_contact_method, notification_preferences
) VALUES (
             'seeker1', 'password', 'seeker1@mail.com', 'Marko Seeker', '', 'Bitola',
             'SEEKER', 'email', 'ALL'
         );

INSERT INTO service (
    title, description, price, duration, category_id, provider_id
) VALUES (
             'Apartment Deep Cleaning',
             'Complete cleaning including kitchen, bathroom, floors and windows',
             80.00,
             120,
             (SELECT id FROM service_category WHERE name = 'Cleaning'),
             (SELECT id FROM users WHERE username = 'provider1')
         );

INSERT INTO schedule_slot (
    start_time, end_time, slot_id, status, provider_id
) VALUES
      (
          NOW() + INTERVAL '1 day',
          NOW() + INTERVAL '1 day 2 hours',
          1,
          'AVAILABLE',
          (SELECT id FROM users WHERE username = 'provider1')
      ),
      (
          NOW() + INTERVAL '2 days',
          NOW() + INTERVAL '2 days 2 hours',
          2,
          'AVAILABLE',
          (SELECT id FROM users WHERE username = 'provider1')
      );


INSERT INTO booking (
    created_at, client_id, provider_id, service_id, slot_id, status, is_recurring
)
VALUES (
           NOW(),
           (SELECT id FROM users WHERE username = 'seeker1'),
           (SELECT id FROM users WHERE username = 'provider1'),
           (SELECT id FROM service WHERE title = 'Apartment Deep Cleaning'),
           (SELECT id FROM schedule_slot WHERE slot_id = 1),
           'PENDING',
           false
       );
