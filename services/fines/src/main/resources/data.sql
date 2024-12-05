-- Insert mock data for fines
INSERT INTO fines (FINES_ID, PLATE_NUMBER, AMOUNT)
VALUES
-- Fines for reserved student, faculty, and staff spots
(1, 'DEF456', 50.00),  -- Fine for student
(2, 'MNO345', 75.00),  -- Fine for faculty
(3, 'VWX234', 30.00),  -- Fine for staff

-- Fines for visitor with voucher and occupied spot
(4, 'ABC123', 100.00), -- Fine for visitor with voucher
(5, 'JKL012', 80.00),  -- Fine for visitor with voucher

-- Fines for visitor without voucher and occupied spot
(6, 'YZA567', 60.00),  -- Fine for visitor without voucher
(7, 'HIJ456', 90.00) -- Fine for visitor without voucher
;