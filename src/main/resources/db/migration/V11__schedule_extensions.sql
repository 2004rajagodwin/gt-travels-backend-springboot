UPDATE schedules
SET journey_date = DATE(departure_time)
WHERE journey_date IS NULL;

CREATE INDEX idx_schedules_trip_status
    ON schedules(trip_status);

CREATE INDEX idx_routes_source
    ON routes(source);

CREATE INDEX idx_routes_destination
    ON routes(destination);